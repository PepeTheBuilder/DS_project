import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { ChatComponent } from '../chat/chat.component';
import { Client, Frame } from 'stompjs';
import { CookieService } from 'ngx-cookie-service';
import { userInfo } from 'os';

@Injectable({
  providedIn: 'root',
})
export class StompService {
  public stompClient: Client;
  private isConnected = false; // Track connection status
  public messages: Message[] = [];
  private messagesSubject: BehaviorSubject<Message[]> = new BehaviorSubject<Message[]>(this.messages);

  constructor(private cookieService: CookieService) {

    console.log('Trying connecting to WebSocket');
    const email = this.cookieService.get('email');
    const socketUrl = `http://localhost/api/chat?email=${encodeURIComponent(email)}`;
    const socket = new SockJS(socketUrl);
    this.stompClient = Stomp.over(socket);

    // Enable debugging logs
    this.stompClient.debug = (msg: string) => {
      console.log(msg);
    };

    this.stompClient.connect({}, (frame) => {
      // Mark the connection as established
      this.isConnected = true;
      console.log('Connected: ' + frame);

      this.stompClient.subscribe('/topic/messages', (message) => {
        const msg = JSON.parse(message.body);
        console.log('Received: -=-=-=-=-=-=-=-=-=-=\n', msg);
        this.addMessage(msg);

      });

    }, (error) => {
      // Handle connection failure
      this.isConnected = false;
      console.error('Failed to connect to WebSocket:', error);
    });
        
  }

  send(app: string, data: any) {
    console.log('Sending message to ' + JSON.stringify(data));
    if (this.isConnected) {
      this.stompClient.send(app, {}, JSON.stringify(data));
    } else {
      console.error('WebSocket connection not ready');
    }
  }

  getMessages(): BehaviorSubject<Message[]> {
    return this.messagesSubject;
  }
  
  // Method to add a new message and notify subscribers
  public addMessage(message: Message) {
    this.messages.push(message); // Update the messages array
    this.messagesSubject.next(this.messages); // Emit the updated messages
  }

}

export interface Message {
  content: string;
  sender: string;    // Sender of the message
  receiver: string;  // Receiver of the message
}