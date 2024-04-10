import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParseQueueComponent } from './parse-queue.component';

describe('ParseQueueComponent', () => {
  let component: ParseQueueComponent;
  let fixture: ComponentFixture<ParseQueueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ParseQueueComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ParseQueueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
