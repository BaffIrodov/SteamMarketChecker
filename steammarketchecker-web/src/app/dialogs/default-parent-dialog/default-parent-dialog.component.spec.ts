import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DefaultParentDialogComponent } from './default-parent-dialog.component';

describe('RequestDialogComponent', () => {
  let component: DefaultParentDialogComponent;
  let fixture: ComponentFixture<DefaultParentDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DefaultParentDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DefaultParentDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
