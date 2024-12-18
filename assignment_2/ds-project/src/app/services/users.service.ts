import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';
import { User } from '../../types';
import { map } from 'rxjs/operators';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})

export class UsersService {
  string = 'http://localhost:8081/api/users';

  constructor(private apiService: ApiService,
              private http: HttpClient,
              private cookieService: CookieService
            ) { }

  // register
  registerUser(user: User): Observable<any> {
    return this.apiService.post('/register', { responseType: 'json' }, user);
  }

  // Login and save cookie
  loginUser(user: { email: string; password: string }): Observable<string> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<string>(this.string + '/login', user, {
      headers,
      responseType: 'text' as 'json',
    }).pipe(
      map((response: any) => {
        this.cookieService.set('email', user.email, { path: '/', expires:1});
        return response;
      })
    );
  }

  //getAllUsers
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.string+'/getAllUsers', {
      responseType: 'json'
    });
  }


  // Update User
  updateUser(user: User): Observable<User> {
    return this.http.post<User>(this.string + '/update', user, {
      responseType: 'json',
      withCredentials: true
    });
  }

  // Delete User
  deleteUser(id: number): Observable<any> {
    return this.apiService.delete(this.string + '/delete', id);
  }

  // Is Admin
  isAdmin(): Observable<boolean> {
    return this.http.get<boolean>(this.string + '/isAdmin', {
      responseType: 'json',
      withCredentials: true
    });
  }

  // Get Current Email
  getCurrentEmail(): Observable<User> {
    return this.http.get<User>(this.string + '/getCurrentEmail', {
      responseType: 'json',
      withCredentials: true
    });
  }

  // Helper to get cookie in the service
  getSessionCookie(): string {
    return this.cookieService.get('email');
  }

}