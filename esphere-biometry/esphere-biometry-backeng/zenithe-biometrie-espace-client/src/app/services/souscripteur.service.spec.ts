import { TestBed } from '@angular/core/testing';

import { SouscripteurService } from './souscripteur.service';

describe('SouscripteurService', () => {
  let service: SouscripteurService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SouscripteurService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
