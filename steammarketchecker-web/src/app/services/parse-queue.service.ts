import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { ConfigService } from "../config/config.service";
import { BaseService } from "./base.service";
import { ParseQueue } from "../dto/ParseQueue";

@Injectable({
  providedIn: "root"
})
export class ParseQueueService extends BaseService {

  private path: string = "parse-queues";

  constructor(private http: HttpClient,
              public router: Router,
              public override configService: ConfigService) {
    super(configService);
  }

  async getAllParseQueues(archive: boolean) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<ParseQueue[]>(url + `/${this.path}/all`, {
      params: {
        archive: archive
      }
    }));
  }
}
