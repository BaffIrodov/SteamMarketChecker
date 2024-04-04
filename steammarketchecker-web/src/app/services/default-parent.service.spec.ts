import { TestBed } from '@angular/core/testing';

import { DefaultParentService } from './default-parent.service';

describe('EventService', () => {
  let service: DefaultParentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DefaultParentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
