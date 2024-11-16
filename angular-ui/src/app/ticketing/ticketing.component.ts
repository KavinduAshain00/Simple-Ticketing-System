
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../api.service'; // Import ApiService
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-ticketing',
  standalone: true,
  templateUrl: './ticketing.component.html',
  styleUrls: ['./ticketing.component.css'],
  imports: [CommonModule, ReactiveFormsModule]  // Import necessary modules like ReactiveFormsModule
})
export class TicketingComponent {
  ticketForm: FormGroup;
  notificationMessage: string = '';
  customerId: number = 12345; // Example hardcoded customerId, replace with real data if needed
  availableTickets: number = 100; // Static value for available tickets

  constructor(private fb: FormBuilder, private apiService: ApiService) {
    this.ticketForm = this.fb.group({
      ticketType: ['', Validators.required],
      quantity: ['', [Validators.required, Validators.min(1), Validators.max(10)]],
      time: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.ticketForm.valid) {
      const { ticketType, quantity, time } = this.ticketForm.value;

      // Ensure there are enough tickets
      if (quantity > this.availableTickets) {
        this.notificationMessage = `Not enough tickets available. Only ${this.availableTickets} tickets left.`;
        return;
      }

      const purchaseData = {
        customerId: this.customerId,
        ticketType,
        quantity,
        time
      };

      // Simulate a successful purchase with ApiService
      this.apiService.purchaseTicketWithCustomer(purchaseData).subscribe({
        next: (response) => {
          this.availableTickets -= quantity; // Deduct the purchased tickets from available tickets
          this.notificationMessage = response.message || `Successfully purchased ${quantity} ${ticketType} ticket(s) for ${time}!`;
          this.ticketForm.reset();
        },
        error: (err) => {
          console.error('Error purchasing ticket:', err);
          this.notificationMessage = err.error.message || 'An error occurred. Please try again.';
        }
      });
    } else {
      this.notificationMessage = 'Please fill in the form correctly before submitting.';
    }
  }

  closeModal(): void {
    this.notificationMessage = ''; // Close the modal by clearing the message
  }
}
