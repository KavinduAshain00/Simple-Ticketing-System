import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms'; // Import FormBuilder and FormGroup
import { ApiService } from '../api.service'; // Service for API calls
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-vendor',
  standalone: true,
  templateUrl: './vendor.component.html',
  styleUrls: ['./vendor.component.css'],
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule] 
})
export class VendorComponent {
  vendor: FormGroup;
  availableTickets = 100; // Example value

  constructor(private fb: FormBuilder, private apiService: ApiService) {
    this.vendor = this.fb.group({
      ticketType: ['', Validators.required],
      quantity: [
        '',
        [Validators.required, Validators.min(1), Validators.max(this.availableTickets)],
      ],
    });
  }

  addTicket() {
    if (this.vendor.valid) {
      const ticketData = this.vendor.value;
      this.apiService.addTicket(ticketData).subscribe(
        (response) => {
          console.log('Ticket added successfully:', response);
          alert('Ticket added successfully!');
          this.vendor.reset(); // Reset the form after success
        },
        (error) => {
          console.error('Error adding ticket:', error);
          alert('Failed to add ticket.');
        }
      );
    }
  }
}
