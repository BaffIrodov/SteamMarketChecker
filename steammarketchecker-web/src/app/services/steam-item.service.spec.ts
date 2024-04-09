import { TestBed } from "@angular/core/testing";
import { ActiveNameService } from "./active-name.service";
import { SteamItemService } from "./steam-item.service";

describe("SteamItemService", () => {
  let service: SteamItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SteamItemService);
  });

  it("should be created", () => {
    expect(service).toBeTruthy();
  });
});
