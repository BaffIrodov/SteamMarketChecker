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
import { Lot } from "../dto/Lot";
import { ActualCurrencyRelation } from "../dto/ActualCurrencyRelation";

@Injectable({
  providedIn: 'root'
})
export class LotService extends BaseService {

  private path: string = "lots";

  constructor(private http: HttpClient,
              public router: Router,
              public override configService: ConfigService) {
    super(configService);
  }

  async getAllLots(onlyActual: boolean, onlyCompleteness: boolean, onlyProfitability: boolean) {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<Lot[]>(url + `/${this.path}/all`, {
      params: {
        onlyActual: onlyActual,
        onlyCompleteness: onlyCompleteness,
        onlyProfitability: onlyProfitability
      }
    }));
  }

  async getActualCurrencyRelation() {
    const url = await this.getBackendUrl();
    return await firstValueFrom(this.http.get<ActualCurrencyRelation>(url + `/${this.path}/currency`));
  }
}
