import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UsersService } from '../services/users.service';
import { DevicesService } from '../services/devices.service';
import { Device, User } from '../../types'; 
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-user-consumtion',
  standalone: true,
  imports: [CommonModule, NgIf, NgFor], 
  providers: [UsersService, DevicesService],
  templateUrl: './user-consumtion.component.html',
  styleUrl: './user-consumtion.component.css'
})

export class UserConsumtionComponent implements OnInit {
  email: string = '';
  devices: Device[] = [];

  constructor(
    private usersService: UsersService,
    private devicesService: DevicesService,
    private router: Router,
    private cookieService: CookieService
  ) {}

  ngOnInit(): void {
    this.getCurrentEmail();
  }

  //modify this function to get the current email of the user from cookies
  getCurrentEmail(): void {
    try {
      // Get the email from the cookies
      const emailFromCookie = this.cookieService.get('email');
  
      if (emailFromCookie) {
        this.email = emailFromCookie; // Set the email from the cookie
        this.fetchDevicesByEmail(this.email); // Fetch devices for the given email
      } else {
        throw new Error('Email not found in cookies');
      }
    } catch (error) {
      console.error('Error fetching current email:', error as string);
      this.router.navigate(['/login']); // Redirect to login if there's an error
    }
  }
  

  fetchDevicesByEmail( email: string ): void {
    this.devicesService.getDeviceByEmail(email).subscribe(
      (data) => {
        this.devices = data as Device[]; // Store the fetched devices
      },
      (error) => {
        console.error('Error fetching devices:', error as string);
      }
    );
  }
}
