import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActualCurrencyRelationDialogComponent } from './actual-currency-relation-dialog.component';

describe('ActualCurrencyRelationDialogComponent', () => {
  let component: ActualCurrencyRelationDialogComponent;
  let fixture: ComponentFixture<ActualCurrencyRelationDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ActualCurrencyRelationDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActualCurrencyRelationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
