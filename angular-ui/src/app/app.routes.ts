import { Routes } from '@angular/router';
import { TicketingComponent } from './ticketing/ticketing.component';
import { HomeComponent } from './home/home.component';
import { VendorComponent } from './vendor/vendor.component';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'vendor', component: VendorComponent }, // Ensure correct path
  { path: 'ticket', component: TicketingComponent },
];
