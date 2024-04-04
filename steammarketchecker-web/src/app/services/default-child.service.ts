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

  constructor(private http: HttpClient,
              public router: Router,
              public override configService: ConfigService) {
    super(configService);
  }

  async getAllDefaultChildren(showArchive: boolean) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<DefaultParent[]>(url + "/default-child/all", {
      params: {
        showArchive: showArchive
      }
    }));
  }

  async getDefaultChildrenByDefaultParentId(id: number, showArchive: boolean) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<DefaultChild[]>(url + `/default-child/${id}`, {
      params: {
        showArchive: showArchive
      }
    }));
  }

  async createDefaultChild(eventStage: DefaultChild) {
    console.log(eventStage);
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.post<DefaultChild[]>(url + "/default-child/create", eventStage));
  }

  async updateDefaultChild(id: number, eventStage: DefaultChild) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.put<DefaultChild[]>(url + `/default-child/${id}/update`, eventStage));
  }

  async archiveDefaultChild(id: number) {
    const url = await this.getBackendUrl();
    await firstValueFrom(this.http.delete(url + `/default-child/${id}/archive`));
  }
}
