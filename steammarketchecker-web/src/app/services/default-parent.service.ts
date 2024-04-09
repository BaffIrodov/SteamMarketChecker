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

  private path: string = "default-parent";

  constructor(private http: HttpClient,
              public router: Router,
              public override configService: ConfigService) {
    super(configService);
  }

  async getAllDefaultParent(showArchive: boolean) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<DefaultParent[]>(url + `/${this.path}/all`, {
      params: {
        showArchive: showArchive
      }
    }));
  }

  async getDefaultParent(id: number) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<DefaultParent>(url + `/${this.path}/${id}`));
  }

  async createDefaultParent(defaultParent: DefaultParent) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.post(url + `/${this.path}/create`, defaultParent));
  }

  async updateDefaultParent(id: number, defaultParent: DefaultParent) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.put(url + `/${this.path}/${id}/update`, defaultParent));
  }

  async archiveDefaultParent(id: number) {
    const url = await this.getBackendUrl();
    await firstValueFrom(this.http.delete(url + `/${this.path}/${id}/archive`));
  }
}
