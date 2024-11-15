import { Routes } from '@angular/router';
import { TicketingComponent } from './ticketing/ticketing.component';
import { HomeComponent } from './home/home.component';
import { VerdorComponent } from './verdor/verdor.component';

export const routes: Routes = [

  {
    path: "",redirectTo: "home", pathMatch:'full'
  },
  {
    path: "home",component:HomeComponent
  },
  {
    path: "vendor", component:VerdorComponent
  },
  {
    path:"ticket", component:TicketingComponent
  }
];
