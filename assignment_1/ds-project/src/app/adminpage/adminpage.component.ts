import { Component, OnInit } from '@angular/core';
import { catchError, take, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { UsersService } from '../services/users.service';
import { DevicesService } from '../services/devices.service';
import { FormsModule } from '@angular/forms';
import { Device, User } from '../../types';
import { CommonModule } from '@angular/common';
import { EMPTY, forkJoin } from 'rxjs';

@Component({
    selector: 'app-adminpage',
    standalone: true,
    imports: [FormsModule, CommonModule],
    providers: [UsersService, DevicesService],
    templateUrl: './adminpage.component.html',
    styleUrls: ['./adminpage.component.css']
})
export class AdminpageComponent implements OnInit {
    isAdmin: boolean = true;
    users: User[] = [];
    devices: Device[] = [];
    selectedUser: User = { id: 0, username: '', password: '', email: '', admin: false }; // Initial empty user
    selectedDevice: Device = { id: 0, name: '', location: '', email: '', maxkwh: 0 }; // Initial empty device
    email: string = ''; // Email for fetching devices when deleting

    constructor(
        private usersService: UsersService,
        private router: Router,
        private devicesService: DevicesService
    ) {}

    ngOnInit(): void {
        this.fetchUsers();
        this.fetchDevices();
    }

    checkAdminStatus(): void {
        this.usersService.isAdmin().subscribe(
            (isAdmin: boolean) => {
                if (!isAdmin) {
                    this.router.navigate(['/login']); // Redirect to login if not an admin
                } else {
                    this.isAdmin = true; // Set isAdmin to true if the user is an admin
                }
            },
            (error) => {
                console.error("Error checking admin status:", error);
                this.router.navigate(['/login']); // Redirect on error
            }
        );
    }

    fetchUsers(): void {
        this.usersService.getAllUsers().subscribe(
            (data: User[]) => {
                this.users = data; // Store the fetched users
            },
            (error) => {
                console.error("Error fetching users:", error);
            }
        );
    }

    fetchDevices(): void {
        this.devicesService.fetchDevices().subscribe(
            (data: Device[]) => {
                this.devices = data; // Store the fetched devices
            },
            (error) => {
                console.error("Error fetching devices:", error);
            }
        );
    }

    editUser(user: User): void {
        this.selectedUser = { ...user }; // Clone the user for editing
    }

    editDevice(device: Device): void {
        this.selectedDevice = { ...device }; // Clone the device for editing
    }

    updateUser(): void {
        this.usersService.updateUser(this.selectedUser).pipe(take(1)).subscribe(
            () => {
                console.log('User updated successfully');
                this.fetchUsers(); // Refresh users
            },
            (error) => {
                console.error("Error updating user:", error);
            }
        );
    }

    updateDevice(): void {
        this.devicesService.updateDevice(this.selectedDevice).subscribe(
            () => {
                console.log('Device updated successfully');
                this.fetchDevices(); // Refresh devices
            },
            (error) => {
                console.error("Error updating device:", error);
            }
        );
    }
    
    deleteUser(userId: number): void {
        if (confirm('Are you sure you want to delete this user?')) {
            // Find the user to get their email
            const userToDelete = this.users.find(user => user.id === userId);
    
            if (userToDelete) {
                // Filter devices that match the user's email
                const devicesToDelete = this.devices.filter(device => device.email === userToDelete.email);
    
                // Delete each device associated with the user's email
                const deleteDeviceObservables = devicesToDelete.map(device => 
                    this.devicesService.deleteDevice(device.id).pipe(
                        take(1),
                        tap(() => console.log(`Device with ID ${device.id} deleted successfully.`)),
                        catchError(error => {
                            console.error("Error deleting device:", error);
                            return EMPTY; // Return an empty observable on error
                        })
                    )
                );
    
                // Execute all delete requests for devices
                forkJoin(deleteDeviceObservables).subscribe(() => {
                    // After all devices are deleted, delete the user
                    this.usersService.deleteUser(userId).subscribe(
                        () => {
                            console.log('User deleted successfully');
                            this.fetchUsers(); // Refresh users
                        },
                        (error) => {
                            console.error("Error deleting user:", error);
                        }
                    );
                });
            } else {
                console.error("User not found.");
            }
        }
    }
    
    deleteDevice(deviceId: number): void {
        if (confirm('Are you sure you want to delete this device?')) {
            this.devicesService.deleteDevice(deviceId).subscribe(
                () => {
                    console.log('Device deleted successfully');
                    this.fetchDevices(); // Refresh devices
                },
                (error) => {
                    console.error("Error deleting device:", error);
                }
            );
        }
    }
}
