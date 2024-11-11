import { Component } from '@angular/core';

@Component({
  selector: 'app-configuration',
  templateUrl: './configuration.component.html',
  styleUrls: ['./configuration.component.css']
})
export class ConfigurationComponent {
  config = {
    totalTickets: 100,
    ticketReleaseRate: 5,
    customerRetrievalRate: 3,
    maxTicketCapacity: 50
  };

  onSubmit() {
    // Perform validation and save configuration logic
    console.log('Configuration saved:', this.config);
    // Here, you could make a service call to store configuration in a backend server
  }
}
