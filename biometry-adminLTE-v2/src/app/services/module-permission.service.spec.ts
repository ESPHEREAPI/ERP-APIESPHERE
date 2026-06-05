import { TestBed } from '@angular/core/testing';

import { ModulePermissionService } from './module-permission.service';

describe('ModulePermissionService', () => {
  let service: ModulePermissionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ModulePermissionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
