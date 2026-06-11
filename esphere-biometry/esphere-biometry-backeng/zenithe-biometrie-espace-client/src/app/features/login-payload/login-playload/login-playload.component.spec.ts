import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginPlayloadComponent } from './login-playload.component';

describe('LoginPlayloadComponent', () => {
  let component: LoginPlayloadComponent;
  let fixture: ComponentFixture<LoginPlayloadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginPlayloadComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginPlayloadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
