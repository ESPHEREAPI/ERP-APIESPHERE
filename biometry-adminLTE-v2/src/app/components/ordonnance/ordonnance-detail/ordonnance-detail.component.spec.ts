import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrdonnanceDetailComponent } from './ordonnance-detail.component';

describe('OrdonnanceDetailComponent', () => {
  let component: OrdonnanceDetailComponent;
  let fixture: ComponentFixture<OrdonnanceDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrdonnanceDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrdonnanceDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
