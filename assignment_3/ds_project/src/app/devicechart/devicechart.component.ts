import { Component, OnInit} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Chart } from 'chart.js/auto';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { NgModule } from '@angular/core';
import { DevicesService } from '../services/devices.service';
// import { BrowserModule } from '@angular/platform-browser';

@Component({
  selector: 'app-devicechart',
  standalone: true,
  imports: [CommonModule, NgIf, FormsModule],  
  providers: [HttpClient, DevicesService],
  templateUrl: './devicechart.component.html',
  styleUrl: './devicechart.component.css'
})
export class DevicechartComponent implements OnInit{

  deviceId: number = 0;
  day: number = 0;
  chartData: number[] = [];
  chartInstance: Chart | null = null;

  constructor(private http: HttpClient, 
    private route: ActivatedRoute,     
    private devicesService: DevicesService,
    private router: Router) {}

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.deviceId = +id; 
        this.fetchChartData();
      } else {
        alert('Device ID not found in the URL.');
      }
    });
  }

fetchChartData() {

  const params = { id: this.deviceId, day: this.day };

  this.devicesService.getChartData(params).subscribe(
    (data) => {
      if (data && data.length > 0) {
        this.chartData = data;
        this.generateChart();
      } else {
        this.chartData = Array(144).fill(0); // Default empty data for 144 intervals
        this.generateChart();
        alert('No data available for the selected parameters.');
      }
    },
    (error) => {
      this.chartData = Array(144).fill(0); // Default empty data for errors
      this.generateChart();
      console.error('Error fetching data', error);
      alert('Error fetching data.');
    }
  );
}

  generateChart() {  // Destroy existing chart instance if it exists
    if (this.chartInstance) {
      this.chartInstance.destroy();
    }

    const labels = Array.from({ length: this.chartData.length }, (_, i) => {
      const hours = Math.floor((i * 10) / 60);
      const minutes = (i * 10) % 60;
      return `${hours.toString().padStart(2, '0')}:${minutes
        .toString()
        .padStart(2, '0')}`;
    });

    // Create a new chart instance
    this.chartInstance = new Chart('chart', {
      type: 'line',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'kWh Consumption',
            data: this.chartData,
            borderColor: 'rgba(75, 192, 192, 1)',
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
          },
        ],
      },
      options: {
        responsive: true,
        scales: {
          x: { title: { display: true, text: 'Time of Day' } },
          y: { title: { display: true, text: 'kWh' } },
        },
      },
    });
  }
}




