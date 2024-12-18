import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';
import { Device } from '../../types';
import { HttpHeaders, HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DevicesService {
  string = 'http://localhost/api/devices';
  constructor(private apiService: ApiService,private http: HttpClient) { }

  getDevices = (url: string, params: any): Observable<Device[]> => {
    return this.apiService.get(url, {
      params,
      responseType: 'json'
    });
  }

  getChartData = (params: any): Observable<number[]> => {
    return this.apiService.get('http://localhost/api/monitor/seeChart', {
      params,
      responseType: 'json'
    });
  }
  
  fetchDevices(): Observable<Device[]> { 
    return this.http.get(this.string+'/all', {
      responseType: 'json'
    }) as Observable<Device[]>;
  }

  addDevice = (device: any) => {
    return this.apiService.post(this.string+'/bind', device, {
      responseType: 'json'
    });
  }
  

  getDeviceById = (id: number) => {
    return this.apiService.get(this.string+`/getDeviceById`, {
      params: {
        id
      },
      responseType: 'json'
    });
  }

  deleteDevice = (id: number) => {
    return this.apiService.delete(this.string+`/delete`, id);
  }
  
  updateDevice = (device: any) => {
    return this.apiService.put(this.string+`/update`, {
      responseType: 'json'
    },
    device);
  }

  getDeviceByEmail = (email: string) => {
    return this.apiService.get<Device[]>(this.string+`/getDevicesByEmail`, {
      params: {
        email
      },
      responseType: 'json'
    });
  } 
}


