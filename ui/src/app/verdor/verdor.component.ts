import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { TicketingService } from '../ticketing.service';  // Import the service

@Component({
  selector: 'app-verdor',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './verdor.component.html',
  styleUrls: ['./verdor.component.css']
})
export class VerdorComponent {
  vendor: FormGroup;
  availableTickets: number = 100; // Define availableTickets as a property

  constructor(private ticketService: TicketingService) {
    this.vendor = new FormGroup({
      ticketType: new FormControl('', Validators.required),
      quantity: new FormControl(1, [Validators.required, Validators.min(1)]),
      price: new FormControl(0, [Validators.required, Validators.min(1)]),
    });

    // Optionally, get available tickets from the service
    this.ticketService.availableTickets$.subscribe((tickets) => {
      this.availableTickets = tickets;
    });
  }

  addTicket() {
    if (this.vendor.valid) {
      const { ticketType, quantity, price } = this.vendor.value;

      // Logic for adding tickets to the system
      if (quantity > 0) {
        console.log('Tickets Added:', { ticketType, quantity, price });

        // Call the service to add tickets
        this.ticketService.addTickets(quantity);

        // Optionally update the local availableTickets
        this.availableTickets += quantity;
      } else {
        alert('Quantity must be greater than 0.');
      }
    } else {
      alert('Please fill out all fields correctly.');
    }
  }
}
