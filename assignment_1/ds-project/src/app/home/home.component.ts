import { Component } from '@angular/core';
import { DevicesService } from '../services/devices.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  constructor(
    private devicesService: DevicesService

  ) { }
  

}
