import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-simulation',
  templateUrl: './simulation.component.html',
  styleUrls: ['./simulation.component.css']
})
export class SimulationComponent implements OnInit {
  logs: string[] = [];
  poolSize: number = 0;
  remainingTickets: number = 0;
  isSimulationRunning: boolean = false;
  private socket: WebSocket | null = null;

  constructor(private http: HttpClient) {}  // Inject HttpClient

  ngOnInit() {
    this.socket = new WebSocket('ws://localhost:8081/api/updates/ws');

    this.socket.onmessage = (event) => {
      const message = event.data;
      this.logs.push(message);

      // Update pool size and remaining tickets dynamically
      const poolMatch = message.match(/Pool size: (\d+)/);
      const remainingMatch = message.match(/Remaining tickets: (\d+)/);

      if (poolMatch) this.poolSize = parseInt(poolMatch[1], 10);
      if (remainingMatch) this.remainingTickets = parseInt(remainingMatch[1], 10);
    };

    this.socket.onopen = () => {
      console.log('WebSocket connection established');
    };

    this.socket.onerror = (error) => {
      console.error('WebSocket error:', error);
    };

    this.socket.onclose = () => {
      console.log('WebSocket connection closed');
    };
  }

  startSimulation() {
    const config = {
      totalTickets: 20,
      maxPoolSize: 10,
      vendorReleaseTime: 1000,
      customerBuyTime: 8000,
    };

    this.isSimulationRunning = true;
    this.logs = []; // Reset logs when starting a new simulation

    // Make an HTTP request to start the simulation on the backend
    this.http.post('http://localhost:8081/api/simulation/start', config).subscribe({
      next: () => {
        this.logs.push('Simulation started...');
      },
      error: (err: any) => {
        console.error('Error starting simulation', err);
        this.logs.push('Error starting simulation. Please check the backend for issues.');
      },
    });
  }
}
