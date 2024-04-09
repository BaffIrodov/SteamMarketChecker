import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveNameComponent } from './active-name.component';

describe('ActiveNameComponent', () => {
  let component: ActiveNameComponent;
  let fixture: ComponentFixture<ActiveNameComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ActiveNameComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActiveNameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
