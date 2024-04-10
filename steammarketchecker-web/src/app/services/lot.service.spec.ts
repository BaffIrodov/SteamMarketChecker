import { TestBed } from "@angular/core/testing";
import { ActiveNameService } from "./active-name.service";
import { SteamItemService } from "./steam-item.service";
import { LotService } from "./lot.service";

describe("LotService", () => {
  let service: LotService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LotService);
  });

  it("should be created", () => {
    expect(service).toBeTruthy();
  });
});
