import { Routes } from '@angular/router';
import { TicketingComponent } from './ticketing/ticketing.component';
import { HomeComponent } from './home/home.component';
import { VendorComponent } from './vendor/vendor.component';
import { LoginComponent } from './login/login.component';
import { AdminpanelComponent } from './adminpanel/adminpanel.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'admin', component: AdminpanelComponent },
  { path: 'home', component: HomeComponent },
  { path: 'vendor', component: VendorComponent }, // Ensure correct path
  { path: 'ticket', component: TicketingComponent },
];
