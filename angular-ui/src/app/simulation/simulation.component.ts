import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';

@Component({
  selector: 'app-simulation',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatListModule],
  templateUrl: './simulation.component.html',
  styleUrls: ['./simulation.component.css'],
})
export class SimulationComponent implements OnInit {
  logs: string[] = [];
  poolSize: number = 0;
  remainingTickets: number = 0;
  isSimulationRunning: boolean = false;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    // Subscribe to server-sent events
    const eventSource = new EventSource('http://localhost:8080/api/updates/stream');
    eventSource.onmessage = (event) => {
      const message = event.data;
      this.logs.push(message);

      // Update pool size and remaining tickets dynamically
      const poolMatch = message.match(/Pool size: (\d+)/);
      const remainingMatch = message.match(/Remaining tickets: (\d+)/);

      if (poolMatch) this.poolSize = parseInt(poolMatch[1], 10);
      if (remainingMatch) this.remainingTickets = parseInt(remainingMatch[1], 10);
    };
  }

  startSimulation() {
    const config = {
      totalTickets: 100, // Adjust as needed
      maxPoolSize: 10,
      vendorReleaseTime: 2000,
      customerBuyTime: 1500,
    };

    this.http.post('http://localhost:8080/api/simulation/start', config).subscribe({
      next: () => {
        this.isSimulationRunning = true;
        this.logs.push('Simulation started...');
      },
      error: (err) => {
        console.error('Error starting simulation', err);
        this.logs.push('Error starting simulation.');
      },
    });
  }
}
