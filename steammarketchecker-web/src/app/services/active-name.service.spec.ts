import { TestBed } from "@angular/core/testing";
import { ActiveNameService } from "./active-name.service";

describe("EventStageService", () => {
  let service: ActiveNameService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ActiveNameService);
  });

  it("should be created", () => {
    expect(service).toBeTruthy();
  });
});
