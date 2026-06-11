import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubscriberDetailComponent } from './subscriber-detail.component';

describe('SubscriberDetailComponent', () => {
  let component: SubscriberDetailComponent;
  let fixture: ComponentFixture<SubscriberDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubscriberDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubscriberDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
