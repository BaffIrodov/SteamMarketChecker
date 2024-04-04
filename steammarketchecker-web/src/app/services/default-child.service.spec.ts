import { TestBed } from '@angular/core/testing';

import { DefaultChildService } from './default-child.service';

describe('EventStageService', () => {
  let service: DefaultChildService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DefaultChildService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
