import { Component, OnInit, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';
import { FormsModule } from '@angular/forms';
import { PLATFORM_ID } from '@angular/core';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-simulation',
  standalone: true,
  imports: [CommonModule, FormsModule, MatCardModule, MatButtonModule, MatListModule],
  templateUrl: './simulation.component.html',
  styleUrls: ['./simulation.component.css']
})
export class SimulationComponent implements OnInit {
  logs: string[] = [];
  poolSize: number = 0;
  remainingTickets: number = 0;
  isSimulationRunning: boolean = false;
  isConfigurationModalOpen: boolean = false;

  config = {
    totalTickets: 0,
    vendorReleaseTime: 0,
    customerBuyTime: 0,
    maxPoolSize: 0
  };

  private platformId = inject(PLATFORM_ID);

  constructor(private apiService: ApiService) {} // Inject the ApiService

  ngOnInit() {
    this.loadLastConfiguration(); // Load configuration on initialization

    if (isPlatformBrowser(this.platformId)) {
      const socket = new WebSocket('ws://localhost:8081/api/updates/ws');

      socket.onopen = () => console.log('WebSocket connection established.');

      socket.onmessage = (event) => {
        const message = event.data;
        this.logs.push(message);

        const poolMatch = message.match(/Pool size: (\d+)/);
        const remainingMatch = message.match(/Remaining tickets: (\d+)/);

        if (poolMatch) this.poolSize = parseInt(poolMatch[1], 10);
        if (remainingMatch) this.remainingTickets = parseInt(remainingMatch[1], 10);
      };

      socket.onerror = (error) => console.error('WebSocket error:', error);

      socket.onclose = (event) => console.log('WebSocket connection closed:', event);
    } else {
      console.warn('WebSocket is not available in SSR');
    }
  }

  loadLastConfiguration() {
    this.apiService.getLastConfigurations().subscribe({
      next: (config: any) => {
        this.config = {
          totalTickets: config.totalTickets ?? 0,
          vendorReleaseTime: config.vendorReleaseTime ?? 0,
          customerBuyTime: config.customerBuyTime ?? 0,
          maxPoolSize: config.maxPoolSize ?? 0
        };
      },
      error: (err) => console.error('Error fetching last configuration:', err)
    });
  }

  startSimulation() {
    this.isSimulationRunning = true;
    this.logs = []; // Reset logs when starting a new simulation

    this.apiService.startSimulation(this.config).subscribe({
      next: () => this.logs.push('Simulation started...'),
      error: (err) => {
        console.error('Error starting simulation', err);
        this.logs.push('Error starting simulation. Please check the backend for issues.');
      }
    });
  }

  stopSimulation() {
    this.isSimulationRunning = false;
    this.logs.push('Simulation stopped...');
    this.apiService.saveConfiguration({}).subscribe({
      next: () => console.log('Simulation stopped successfully'),
      error: (err) => console.error('Error stopping simulation', err)
    });
  }

  openConfiguration() {
    this.isConfigurationModalOpen = true;
  }

  closeConfiguration() {
    this.isConfigurationModalOpen = false;
  }

  saveConfiguration() {
    console.log('Configuration saved:', this.config);
    this.apiService.saveConfiguration(this.config).subscribe({
      next: () => console.log('Configuration saved successfully'),
      error: (err) => console.error('Error saving configuration:', err)
    });
    this.closeConfiguration();
  }
}
