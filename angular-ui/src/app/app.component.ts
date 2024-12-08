import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SimulationComponent } from '../app/simulation/simulation.component';

@Component({
    selector: 'app-root',
    imports: [CommonModule, SimulationComponent],
    template: `
    <div style="text-align:center">
      <h1>Ticket Pool Simulation</h1>
      <app-simulation></app-simulation>
    </div>
  `,
    styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'angular-ui';
}
