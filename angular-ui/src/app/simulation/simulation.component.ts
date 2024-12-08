import { Component, OnInit, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';
import { PLATFORM_ID } from '@angular/core';

@Component({
  selector: 'app-simulation',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatListModule],
  templateUrl: './simulation.component.html',
  styleUrls: ['./simulation.component.css']
})
export class SimulationComponent implements OnInit {
  logs: string[] = [];
  poolSize: number = 0;
  remainingTickets: number = 0;
  isSimulationRunning: boolean = false;
  private platformId = inject(PLATFORM_ID);

  constructor(private http: HttpClient) {}

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      // Only run WebSocket in the browser
      const socket = new WebSocket('ws://localhost:8081/api/updates/ws');
      
      socket.onopen = () => {
        console.log('WebSocket connection established.');
      };
      
      socket.onmessage = (event) => {
        const message = event.data;
        this.logs.push(message);

        // Update pool size and remaining tickets dynamically
        const poolMatch = message.match(/Pool size: (\d+)/);
        const remainingMatch = message.match(/Remaining tickets: (\d+)/);

        if (poolMatch) this.poolSize = parseInt(poolMatch[1], 10);
        if (remainingMatch) this.remainingTickets = parseInt(remainingMatch[1], 10);
      };

      socket.onerror = (error) => {
        console.error('WebSocket error:', error);
      };

      socket.onclose = (event) => {
        console.log('WebSocket connection closed:', event);
      };
    } else {
      console.warn('WebSocket is not available in SSR');
    }
  }

  startSimulation() {
    const config = {
      totalTickets: 20, // Adjust as needed
      maxPoolSize: 10,
      vendorReleaseTime: 1000,
      customerBuyTime: 8000,
    };
  
    this.isSimulationRunning = true;
    this.logs = []; // Reset logs when starting a new simulation
  
    this.http.post('http://localhost:8081/api/simulation/start', config).subscribe({
      next: () => {
        this.logs.push('Simulation started...');
      },
      error: (err) => {
        console.error('Error starting simulation', err);
        this.logs.push('Error starting simulation. Please check the backend for issues.');
      },
    });
  }
  
}
