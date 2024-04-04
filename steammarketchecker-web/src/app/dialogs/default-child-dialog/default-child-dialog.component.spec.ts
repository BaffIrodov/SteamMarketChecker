import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DefaultChildDialogComponent } from './default-child-dialog.component';

describe('RequestPositionDialogComponent', () => {
  let component: DefaultChildDialogComponent;
  let fixture: ComponentFixture<DefaultChildDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DefaultChildDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DefaultChildDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
