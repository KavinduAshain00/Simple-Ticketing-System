import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { SimulationComponent } from './app/simulation/simulation.component';

bootstrapApplication(SimulationComponent, {
  providers: [provideHttpClient(withFetch())],
}).catch((err) => console.error(err));
