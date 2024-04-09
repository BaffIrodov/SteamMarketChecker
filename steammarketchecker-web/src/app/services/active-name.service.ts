import { Injectable } from '@angular/core';
import { firstValueFrom } from "rxjs";
import { DefaultParent } from "../dto/DefaultParent";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { ConfigService } from "../config/config.service";
import { BaseService } from "./base.service";
import { DefaultChild } from "../dto/DefaultChild";
import { ActiveName } from "../dto/ActiveName";

@Injectable({
  providedIn: 'root'
})
export class ActiveNameService extends BaseService {
  
  private path: string = "active-names";

  constructor(private http: HttpClient,
              public router: Router,
              public override configService: ConfigService) {
    super(configService);
  }

  async getAllActiveNames(showArchive: boolean) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<ActiveName[]>(url + `/${this.path}/all`, {
      params: {
        showArchive: showArchive
      }
    }));
  }

  async createActiveName(activeName: ActiveName) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.post<ActiveName[]>(url + `/${this.path}/create`, activeName));
  }

  async updateActiveName(id: number, activeName: ActiveName) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.put<ActiveName[]>(url + `/${this.path}/${id}/update`, activeName));
  }

  async archiveActiveName(id: number) {
    const url = await this.getBackendUrl();
    await firstValueFrom(this.http.delete(url + `/${this.path}/${id}/archive`));
  }
}
