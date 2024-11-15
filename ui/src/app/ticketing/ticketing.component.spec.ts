import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketingComponent } from './ticketing.component';

describe('TicketFormComponent', () => {
  let component: TicketingComponent;
  let fixture: ComponentFixture<TicketingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TicketingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
