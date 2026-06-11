import { TestBed } from '@angular/core/testing';

import { DetailConsomationService } from './detail-consomation.service';

describe('DetailConsomationService', () => {
  let service: DetailConsomationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DetailConsomationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
