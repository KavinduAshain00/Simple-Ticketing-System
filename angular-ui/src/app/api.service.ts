import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  saveConfiguration(config: any) {
    return this.http.post(`${this.baseUrl}/config`, config);
  }

  getAllConfigurations() {
    return this.http.get(`${this.baseUrl}/config`);
  }

  startSimulation(config: any) {
    return this.http.post(`${this.baseUrl}/simulation/start`, config);
  }
}
