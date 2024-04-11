import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LotDetailingDialogComponent } from './lot-detailing-dialog.component';

describe('LotDetailingDialogComponent', () => {
  let component: LotDetailingDialogComponent;
  let fixture: ComponentFixture<LotDetailingDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LotDetailingDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LotDetailingDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
