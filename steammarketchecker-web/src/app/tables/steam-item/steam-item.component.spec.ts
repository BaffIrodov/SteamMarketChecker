import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SteamItemComponent } from './steam-item.component';

describe('SteamItemComponent', () => {
  let component: SteamItemComponent;
  let fixture: ComponentFixture<SteamItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SteamItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SteamItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
