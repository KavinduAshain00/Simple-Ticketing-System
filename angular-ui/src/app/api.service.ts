import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private baseUrl = 'http://localhost:8081/api'; // Base URL of the backend API

  constructor(private http: HttpClient) {}

  // Add Ticket API
  addTicket(ticket: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/tickets/add`, ticket);
  }

  purchaseTicketWithCustomer(purchaseData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/tickets/purchaseWithCustomer`, purchaseData);
  }

  // -------------------- Customer APIs --------------------

  // Create a new customer
  createCustomer(customer: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/customers/create`, customer);
  }

  // Get customer by ID
  getCustomerById(customerId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/customers/${customerId}`);
  }

  getCustomerByEmail(email: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/customers/${email}`);
  }

  // Update a customer
  updateCustomer(customerId: number, customerData: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/customers/${customerId}`, customerData);
  }

  // Delete a customer
  deleteCustomer(customerId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/customers/${customerId}`);
  }

  // Get all customers (with optional pagination)
  getAllCustomers(page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get(`${this.baseUrl}/customers`, { params });
  }

  // Other API methods can remain here
}
