import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TicketingService {
  // Assume availableTickets is being tracked via a BehaviorSubject
  private availableTicketsSubject = new BehaviorSubject<number>(100); // Starting with 100 tickets
  availableTickets$ = this.availableTicketsSubject.asObservable();  // Observable for subscribers

  constructor() { }

  // Method to add tickets
  addTickets(quantity: number) {
    if (quantity > 0) {
      const currentTickets = this.availableTicketsSubject.value;
      this.availableTicketsSubject.next(currentTickets + quantity); // Update available tickets
    } else {
      console.error('Cannot add 0 or negative tickets');
    }
  }

  // Method to subtract tickets (e.g., for purchasing)
  subtractTickets(quantity: number) {
    const currentTickets = this.availableTicketsSubject.value;
    if (currentTickets >= quantity) {
      this.availableTicketsSubject.next(currentTickets - quantity); // Update available tickets
    } else {
      console.error('Not enough tickets available.');
    }
  }
}
