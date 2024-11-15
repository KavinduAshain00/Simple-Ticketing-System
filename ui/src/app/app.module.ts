import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common'; // Import CommonModule
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { TicketingComponent } from './ticketing/ticketing.component';

@NgModule({
  declarations: [
    TicketingComponent
  ],
  imports: [
    BrowserModule,
    CommonModule, // Add CommonModule here
    FormsModule,
    RouterModule.forRoot([
      { path: '', component: AppComponent },
      { path: 'ticketing', component: TicketingComponent }
    ])
  ],
  providers: [],
})
export class AppModule { }
