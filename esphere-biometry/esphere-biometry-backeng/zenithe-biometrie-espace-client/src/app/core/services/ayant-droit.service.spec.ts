import { TestBed } from '@angular/core/testing';

import { AyantDroitService } from './ayant-droit.service';

describe('AyantDroitService', () => {
  let service: AyantDroitService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AyantDroitService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
