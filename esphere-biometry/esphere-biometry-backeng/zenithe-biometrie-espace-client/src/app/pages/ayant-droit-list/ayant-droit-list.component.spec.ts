import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AyantDroitListComponent } from './ayant-droit-list.component';

describe('AyantDroitListComponent', () => {
  let component: AyantDroitListComponent;
  let fixture: ComponentFixture<AyantDroitListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AyantDroitListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AyantDroitListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
