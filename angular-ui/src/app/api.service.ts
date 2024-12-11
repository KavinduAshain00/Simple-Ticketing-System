import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private baseUrl = 'http://localhost:8081/api';

  constructor(private http: HttpClient) {}

  /**
   * Fetches the last saved configuration from the backend.
   * @returns An Observable with the configuration data.
   */
  getAllConfigurations(): Observable<any> {
    return this.http.get(`${this.baseUrl}/config/all`);
  }

  getLastConfigurations(): Observable<any> {
    return this.http.get(`${this.baseUrl}/config/last`);
  }
  /**
   * Saves the current configuration to the backend.
   * @param config The configuration object to save.
   * @returns An Observable of the save operation result.
   */
  saveConfiguration(config: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/config`, config);
  }

  /**
   * Starts the simulation with the given configuration.
   * @param config The configuration to use for starting the simulation.
   * @returns An Observable of the start operation result.
   */
  startSimulation(config: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/simulation/start`, config);
  }

  /**
   * Stops the current simulation.
   * @returns An Observable of the stop operation result.
   */
  stopSimulation(): Observable<any> {
    return this.http.post(`${this.baseUrl}/simulation/stop`, {});
  }
}
