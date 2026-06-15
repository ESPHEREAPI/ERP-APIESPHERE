import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardPayloadComponent } from './dashboard-payload.component';

describe('DashboardPayloadComponent', () => {
  let component: DashboardPayloadComponent;
  let fixture: ComponentFixture<DashboardPayloadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardPayloadComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardPayloadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
