import { Injectable } from '@angular/core';
import { firstValueFrom } from "rxjs";
import { DefaultParent } from "../dto/DefaultParent";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { ConfigService } from "../config/config.service";
import { BaseService } from "./base.service";
import { DefaultChild } from "../dto/DefaultChild";
import { ActiveName } from "../dto/ActiveName";
import { SteamItem, SteamItemType } from "../dto/SteamItem";

@Injectable({
  providedIn: 'root'
})
export class SteamItemService extends BaseService {

  private path: string = "steam-items";

  constructor(private http: HttpClient,
              public router: Router,
              public override configService: ConfigService) {
    super(configService);
  }

  async getAllSteamItems(steamItemType: SteamItemType) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<SteamItem[]>(url + `/${this.path}/all`, {
      params: {
        steamItemType: steamItemType
      }
    }));
  }

  async forceUpdateSteamItem(id: number) {
    const url = await this.getBackendUrl();
    await firstValueFrom(this.http.get(url + `/${this.path}/${id}/force-update`));
  }
}
