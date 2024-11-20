import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api'; // Base URL of the backend API

  constructor(private http: HttpClient) {}

  // Add Ticket API
  addTicket(ticket: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/tickets/add`, ticket);
  }

  purchaseTicketWithCustomer(purchaseData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/tickets/purchaseWithCustomer`, purchaseData);
  }

  
  // Other API methods can remain here
}
