import { TestBed } from "@angular/core/testing";
import { ActiveNameService } from "./active-name.service";
import { SteamItemService } from "./steam-item.service";
import { LotService } from "./lot.service";
import { ParseQueueComponent } from "../tables/parse-queue/parse-queue.component";
import { ParseQueueService } from "./parse-queue.service";

describe("ParseQueueService", () => {
  let service: ParseQueueService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ParseQueueService);
  });

  it("should be created", () => {
    expect(service).toBeTruthy();
  });
});
