import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { ConfigService } from "../config/config.service";
import { BaseService } from "./base.service";
import { firstValueFrom } from "rxjs";
import { DefaultParent } from "../dto/DefaultParent";

@Injectable({
  providedIn: "root"
})
export class DefaultParentService extends BaseService {

  constructor(private http: HttpClient,
              public router: Router,
              public override configService: ConfigService) {
    super(configService);
  }

  async getAllDefaultParent(showArchive: boolean) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<DefaultParent[]>(url + "/default-parent/all", {
      params: {
        showArchive: showArchive
      }
    }));
  }

  async getDefaultParent(id: number) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<DefaultParent>(url + `/default-parent/${id}`));
  }

  async createDefaultParent(event: DefaultParent) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.post(url + "/default-parent/create", event));
  }

  async updateDefaultParent(id: number, event: DefaultParent) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.put(url + `/default-parent/${id}/update`, event));
  }

  async archiveEvent(id: number) {
    const url = await this.getBackendUrl();
    await firstValueFrom(this.http.delete(url + `/default-parent/${id}/archive`));
  }
}
