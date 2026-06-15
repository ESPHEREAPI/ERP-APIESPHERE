import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminAgenceComponent } from './admin-agence.component';

describe('AdminAgenceComponent', () => {
  let component: AdminAgenceComponent;
  let fixture: ComponentFixture<AdminAgenceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminAgenceComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminAgenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
