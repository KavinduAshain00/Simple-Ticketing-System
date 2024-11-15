import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { TicketingService } from '../ticketing.service'; // Import the service

@Component({
  selector: 'app-ticketing',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './ticketing.component.html',
  styleUrls: ['./ticketing.component.css']
})
export class TicketingComponent {
  ticketForm: FormGroup;
  notificationMessage: string = '';

  // Change 'private' to 'public' so it's accessible in the template
  constructor(private fb: FormBuilder, public ticketService: TicketingService) {  // Made 'ticketService' public
    this.ticketForm = this.fb.group({
      ticketType: ['', Validators.required],
      quantity: ['', [Validators.required, Validators.min(1), Validators.max(10)]],
      time: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.ticketForm.valid) {
      const { ticketType, quantity, time } = this.ticketForm.value;

      try {
        this.ticketService.subtractTickets(quantity);
        this.notificationMessage = `Successfully purchased ${quantity} ${ticketType} ticket(s) for ${time}!`;
        this.ticketForm.reset();
      } catch (error) {
        this.notificationMessage = 'Not enough tickets available.';
      }
    } else {
      this.notificationMessage = 'Please fill in the form correctly before submitting.';
    }
  }

  closeModal(): void {
    this.notificationMessage = '';  // Close the modal by clearing the message
  }
}
