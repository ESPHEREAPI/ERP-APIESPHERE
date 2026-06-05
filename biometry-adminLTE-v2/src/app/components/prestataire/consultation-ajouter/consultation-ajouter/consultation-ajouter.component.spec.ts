import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultationAjouterComponent } from './consultation-ajouter.component';

describe('ConsultationAjouterComponent', () => {
  let component: ConsultationAjouterComponent;
  let fixture: ComponentFixture<ConsultationAjouterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultationAjouterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConsultationAjouterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
