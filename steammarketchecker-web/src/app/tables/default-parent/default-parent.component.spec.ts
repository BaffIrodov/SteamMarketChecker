import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DefaultParentComponent } from './default-parent.component';

describe('EventComponent', () => {
  let component: DefaultParentComponent;
  let fixture: ComponentFixture<DefaultParentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DefaultParentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DefaultParentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
