import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveNameDialogComponent } from './active-name-dialog.component';

describe('ActiveNameDialogComponent', () => {
  let component: ActiveNameDialogComponent;
  let fixture: ComponentFixture<ActiveNameDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ActiveNameDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActiveNameDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
