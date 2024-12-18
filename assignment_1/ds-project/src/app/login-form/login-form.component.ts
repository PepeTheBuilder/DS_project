import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UsersService } from '../services/users.service';
import { User } from '../../types';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [FormsModule, CommonModule],
  providers: [UsersService],
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css'] 
})
export class LoginFormComponent {
  
  loginObj: Login;
  errorMessage: string = 'nothing';

  constructor(private usersService: UsersService, private router: Router) {
    this.loginObj = new Login();
  }

  onSubmit() {
    this.errorMessage = ''; // Reset the error message on submit

    this.usersService.loginUser(this.loginObj).subscribe(
      (response: string) => {
        console.log("API Response:", response); // Debugging line to check API response

        if (response.includes('admin')) {
          console.log("Navigating to admin page"); // Debug log
          this.router.navigate(['/admin']);
        } else if (response.includes('user')) {
          console.log("Navigating to user home page"); // Debug log
          this.router.navigate(['/user']);
        } else {
          this.errorMessage = 'Unexpected response from the server.';
          console.log("Unexpected response:", response); // Debug log
        }
      },
      (error) => {
        this.errorMessage = 'Invalid email or password';
        alert(this.errorMessage); 
        console.error("Error:", error); 
      }
    );
  }
  
}

export class Login {
  email: string;
  password: string;
  constructor() {
    this.email = '';
    this.password = '';
  }
}
