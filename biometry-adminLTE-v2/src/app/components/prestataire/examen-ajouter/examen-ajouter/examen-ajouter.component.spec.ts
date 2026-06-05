import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExamenAjouterComponent } from './examen-ajouter.component';

describe('ExamenAjouterComponent', () => {
  let component: ExamenAjouterComponent;
  let fixture: ComponentFixture<ExamenAjouterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExamenAjouterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExamenAjouterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
