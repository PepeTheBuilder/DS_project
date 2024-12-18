import { Component, OnInit, OnDestroy, ElementRef, ViewChild } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { BehaviorSubject, debounceTime, distinctUntilChanged, Observable } from 'rxjs';
import { StompService } from '../services/stomp-service.service';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CookieService } from 'ngx-cookie-service';
import { UsersService } from '../services/users.service';
// import { ChatComponent } from './chat.component';
import { HttpClient } from '@angular/common/http';  // Import HttpClient to make API calls


@Component({
  selector: 'chat',
  standalone: true,
  providers: [StompService, UsersService],
  imports: [
    FormsModule, CommonModule
  ],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css'],
})
export class ChatComponent implements OnInit, OnDestroy {
  public messages: Message[] = [];
  public submess: Message[] = [];
  public alreadycopied: Message[] = [];
  public newMessage: string = '';
  public sender: string = ''; // My email
  public receiver: string = 'User2'; // Example receiver, set dynamically if needed
  public emailFromCookie: string = '';
  public users: string[] = ['User1', 'User2']; // Example users
  @ViewChild('chatInput', { static: true }) chatInput!: ElementRef;

  private typing = false;
  private messageSubject = new BehaviorSubject<string>('');
  public message$ = this.messageSubject.asObservable();

  constructor(private stompService: StompService, private cookieService: CookieService, private userService: UsersService) {
    this.message$.subscribe((message) => {
      const isEmpty = message.trim() === ''; // true if the message is empty
      console.log('<><><><><><><><><>Is chat input empty?', isEmpty, this.typing);
      // Typing logic
      if (isEmpty && this.typing) {
        this.typing = false;
        console.log('Stopped typing...');
        this.markStopTyping();
      } else if (!isEmpty && !this.typing) {
        this.typing = true;
        console.log('Typing...');
        this.markTyping();
      }
    });
  }
  
  ngOnInit(): void {
    (window as any)['chatComponent'] = this; 
    // Subscribe to the chat topic
    // this.userService.getAllUsersEmail() add all users to the users array
    this.userService.getAllUsersEmail().subscribe((users) => {
      this.users = users;
    }); 

    this.sender = this.cookieService.get('email');

    // i have in the stompService the messages array that i can fetch and display in the chat component after i verify the sender and receiver is the same as my email

    // how can i sync the messages array from the stompService with the messages array in the chat component always?
    // i want to display the messages in the chat component in real time

    this.stompService.getMessages().subscribe((messages) => {
      this.submess = messages;  // Update the component's messages array
      this.copyMessages(this.submess, this.messages);
    });

  }

  onInputChange(): void {
    this.messageSubject.next(this.newMessage);
  }

  ngOnDestroy(): void {
    this.markStopTyping();
  }

  sendMessage(): void {
    if (this.newMessage.trim() && this.receiver) {
      const message = {
        sender: this.sender,
        receiver: this.receiver,
        content: this.newMessage,
      };
      this.markStopTyping();
      this.typing = false;
      this.alreadycopied.push(message);
      this.newMessage = '';  // Clear input field
      this.stompService.send('/topic/messages', message);  // Send the message to the topic
    } else {
      alert('Please enter a message and select a receiver.');
    }
  }

  sendMessageBroadcast(): void {
    if(this.sender === 'admin@gmail.com') {
      this.users.forEach(userEmail => {
        const message = {
          sender: this.sender,
          receiver: userEmail,
          content: this.newMessage,
        };
        this.stompService.send('/topic/messages', message);  // Send the message to the topic
      });
        this.typing = false;
        this.newMessage = '';  // Clear input field
    } else {
      alert('You are not authorized to send broadcast messages.');
    }
  }

  checkArg(message: Message) {
    return (message.sender === this.sender && message.receiver === this.receiver || message.sender === this.receiver && message.receiver === this.sender) 
    || (message.sender === 'TYPING' && message.receiver === this.sender && message.content === this.receiver)
    || (message.sender === 'STOP_TYPING' && message.receiver === this.sender && message.content === this.receiver)
    || (message.sender === 'SEEN'   && message.receiver === this.sender && message.content === this.receiver);
  }

  selectReceiver(user: string) {
    let swiched = false;
    if (this.receiver === user) {
      swiched = true;
    }
    this.receiver = user;
    console.log('Selected Receiver:', this.receiver);  
    this.messages = this.submess.filter((message) => (this.checkArg(message))); 

    if (swiched) {
      this.markMessageAsSeen();
    }
  }

  copyMessages(submess: Message[], messages: Message[]) {
    let newMessages = 0;
    for (let i = 0; i < submess.length; i++) {
      if (this.checkArg(submess[i])) {
        if (!this.alreadycopied.includes(submess[i])) {
          this.alreadycopied.push(submess[i]);
          messages.push(submess[i]);
          if (submess[i].sender === this.receiver) {
            newMessages++;
          }
        }
      }
    }
    if (newMessages > 0) {
      this.markMessageAsSeen();
    }
  }

  public markMessageAsSeen() {  
    // Check if the message meets the conditions
    const seenMessage: Message = {
      sender: 'SEEN',
      receiver: this.receiver,
      content: this.sender,
    };
     
    // Send the "SEEN" message to the speaking user
    this.stompService.send('/topic/messages', seenMessage); 
    console.log('SEEN Message:', seenMessage);
  
  }

  public markTyping() {
  
    const TYPINGMessage: Message = {
      sender: 'TYPING',
      receiver: this.receiver, 
      content: this.sender,
    };

    // Push the new "TYPING" message to the message array
    this.stompService.send('/topic/messages', TYPINGMessage); 
    console.log('TYPING Message:', TYPINGMessage);
  }

  public markStopTyping() {
    const TYPINGMessage: Message = {
      sender: 'STOP_TYPING',
      receiver: this.receiver, 
      content: this.sender,
    };

    // Push the new "TYPING" message to the message array
    this.stompService.send('/topic/messages', TYPINGMessage); 
    console.log('TYPING Message:', TYPINGMessage);
  }

}
  
export interface Message {
  content: string;
  sender: string;    // Sender of the message
  receiver: string;  // Receiver of the message
}
