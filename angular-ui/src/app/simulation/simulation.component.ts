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
  isLoadConfigurationPopupOpen: boolean = false;

  config = {
    totalTickets: 0,
    vendorReleaseTime: 0,
    customerBuyTime: 0,
    maxPoolSize: 0
  };

  configErrors: any = {
    totalTickets: '',
    vendorReleaseTime: '',
    customerBuyTime: '',
    maxPoolSize: ''
  };

  previousConfigurations: any[] = [];
  selectedConfiguration: any = null;

  private platformId = inject(PLATFORM_ID);

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.loadLastConfiguration();
    this.fetchPreviousConfigurations();

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

      socket.onerror = (error) => {
        console.error('WebSocket error:', error);
        alert('WebSocket Error A WebSocket error occurred.');
      };

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
      error: (err) => {
        console.error('Error fetching last configuration:', err);
        alert('Configuration Error Failed to load the last configuration.');
      }
    });
  }

  fetchPreviousConfigurations() {
    this.apiService.getAllConfigurations().subscribe({
      next: (configs: any[]) => {
        console.log('Fetched configurations:', configs);
        this.previousConfigurations = configs;
      },
      error: (err) => {
        console.error('Error fetching previous configurations:', err);
        alert('Configuration Error Failed to load previous configurations.');
      }
    });
  }

  validateConfiguration(): boolean {
    let isValid = true;
    this.configErrors = { totalTickets: '', vendorReleaseTime: '', customerBuyTime: '', maxPoolSize: '' };

    if (this.config.totalTickets <= 0) {
      this.configErrors.totalTickets = 'Total tickets must be greater than 0.';
      isValid = false;
    }
    if (this.config.vendorReleaseTime <= 0) {
      this.configErrors.vendorReleaseTime = 'Vendor release time must be greater than 0.';
      isValid = false;
    }
    if (this.config.customerBuyTime <= 0) {
      this.configErrors.customerBuyTime = 'Customer buy time must be greater than 0.';
      isValid = false;
    }
    if (this.config.maxPoolSize <= 0) {
      this.configErrors.maxPoolSize = 'Max pool size must be greater than 0.';
      isValid = false;
    }

    return isValid;
  }

  startSimulation() {
    if (!this.validateConfiguration()) {
      //this.logs.push('Configuration validation failed. Please fix the errors and try again.');
      alert('Simulation Error Failed to start the simulation.');
      return;
    }

    this.isSimulationRunning = true;
    this.logs = []; // Reset logs when starting a new simulation

    this.apiService.startSimulation(this.config).subscribe({
      next: () => this.logs.push('Simulation started...'),
      error: (err) => {
        console.error('Error starting simulation', err);
        this.logs.push('Error starting simulation. Please check the backend for issues.');
        alert('Simulation Error Failed to start the simulation.');
      }
    });
  }

  stopSimulation() {
    this.isSimulationRunning = false;
    //this.logs.push('Simulation stopped...');

    this.apiService.stopSimulation().subscribe({
      next: () => console.log('Simulation stopped successfully'),
      error: (err) => {
        console.error('Error stopping simulation', err);
        alert('Simulation Error Failed to stop the simulation.');
      }
    });
  }

  openConfiguration() {
    this.isConfigurationModalOpen = true;
  }

  closeConfiguration() {
    this.isConfigurationModalOpen = false;
  }

  openLoadConfigurationPopup() {
    this.isLoadConfigurationPopupOpen = true;
  }

  closeLoadConfigurationPopup() {
    this.isLoadConfigurationPopupOpen = false;
  }

  loadConfiguration(configuration: any) {
    this.config = {
      ...this.config,
      totalTickets: configuration.totalTickets ?? this.config.totalTickets,
      vendorReleaseTime: configuration.vendorReleaseTime ?? this.config.vendorReleaseTime,
      customerBuyTime: configuration.customerBuyTime ?? this.config.customerBuyTime,
      maxPoolSize: configuration.maxPoolSize ?? this.config.maxPoolSize
    };

    this.logs.push(`Configuration ID ${configuration.id} appended to current configuration.`);
    this.closeLoadConfigurationPopup();
  }

  saveConfiguration() {
    if (!this.validateConfiguration()) {
      //this.logs.push('Configuration validation failed. Please fix the errors and try again.');
      alert('Save Error Failed to save the configuration.');
      return;
    }

    this.logs.push('Saving configuration...');

    this.apiService.saveConfiguration(this.config).subscribe({
      next: () => this.logs.push('Configuration saved successfully.'),
      error: (err) => {
        console.error('Error saving configuration:', err);
        this.logs.push('Error saving configuration. Please try again.');
        alert('Save Error Failed to save the configuration.');
      }
    });

    this.closeConfiguration();
  }
  isCriticalLog(log: string): boolean {
    return log.includes('All tickets are sold. Simulation ending') || 
           log.includes('has no more tickets to add. Stopping.') ||
           log.includes('Simulation stopped');
  }
  
}
