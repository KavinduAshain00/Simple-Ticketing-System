import { Component } from '@angular/core';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-configuration',
  templateUrl: './configuration.component.html',
})
export class ConfigurationComponent {
  config = {
    totalTickets: 0,
    maxPoolSize: 0,
    vendorReleaseTime: 0,
    customerBuyTime: 0,
  };

  constructor(private apiService: ApiService) {}

  saveConfig() {
    this.apiService.saveConfiguration(this.config).subscribe(() => {
      alert('Configuration saved!');
    });
  }
}
