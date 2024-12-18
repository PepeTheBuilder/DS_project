import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Options } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(
    private http: HttpClient

  ) { }

  get<T>(url: string, options: Options): Observable<T> {
    return this.http.get<T>(url, options) as Observable<T>;
  }
  
  post<T>(url: string,  options: Options, body?: any): Observable<T> {
    return this.http.post<T>(url, body, options) as Observable<T>;
  }

  put<T>(url: string,  options: Options, body?: any): Observable<T> {
    return this.http.put<T>(url, body, options) as Observable<T>;
  }

  delete<T>(url: string,  id: number): Observable<T> {
    return this.http.delete<T>(url, {body: id,
      withCredentials: true}) as Observable<T>;
  }

}
