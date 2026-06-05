import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExamenDetailComponent } from './examen-detail.component';

describe('ExamenDetailComponent', () => {
  let component: ExamenDetailComponent;
  let fixture: ComponentFixture<ExamenDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExamenDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExamenDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
