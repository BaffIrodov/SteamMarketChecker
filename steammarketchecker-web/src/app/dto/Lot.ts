import { SteamItem } from "./SteamItem";

export class Lot {
  id: number;
  listingId: number;
  assetId: number;
  skin: SteamItem;
  stickers: SteamItem[];
  completeness: boolean;
  profitability: boolean;
  actual: boolean;
  profit: number;
  convertedPrice: number;
  convertedFee: number;
  realPrice: number;
  priceCalculatingDate: number;
  parseDate: number;
  stickersAsString: string;

  constructor() {
  }
}