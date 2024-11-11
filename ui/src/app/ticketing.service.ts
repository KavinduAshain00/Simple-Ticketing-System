import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TicketingService {
  private apiUrl = 'http://localhost:8080/config';  // Replace with your backend endpoint

  constructor(private http: HttpClient) { }

  saveConfig(config: any): Observable<any> {
    return this.http.post(this.apiUrl, config);
  }
}
