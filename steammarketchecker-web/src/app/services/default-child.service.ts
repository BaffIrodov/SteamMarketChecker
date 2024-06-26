import { Injectable } from '@angular/core';
import { firstValueFrom } from "rxjs";
import { DefaultParent } from "../dto/DefaultParent";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { ConfigService } from "../config/config.service";
import { BaseService } from "./base.service";
import { DefaultChild } from "../dto/DefaultChild";

@Injectable({
  providedIn: 'root'
})
export class DefaultChildService extends BaseService {

  private path: string = "default-child";

  constructor(private http: HttpClient,
              public router: Router,
              public override configService: ConfigService) {
    super(configService);
  }

  async getAllDefaultChildren(showArchive: boolean) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<DefaultParent[]>(url + `/${this.path}/all`, {
      params: {
        showArchive: showArchive
      }
    }));
  }

  async getDefaultChildrenByDefaultParentId(id: number, showArchive: boolean) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<DefaultChild[]>(url + `/${this.path}/${id}`, {
      params: {
        showArchive: showArchive
      }
    }));
  }

  async createDefaultChild(defaultChild: DefaultChild) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.post<DefaultChild[]>(url + `/${this.path}/create`, defaultChild));
  }

  async updateDefaultChild(id: number, defaultChild: DefaultChild) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.put<DefaultChild[]>(url + `/${this.path}/${id}/update`, defaultChild));
  }

  async archiveDefaultChild(id: number) {
    const url = await this.getBackendUrl();
    await firstValueFrom(this.http.delete(url + `/${this.path}/${id}/archive`));
  }
}
