export class SteamItem {
  id: number;
  steamItemId: number;
  name: string;
  minPrice: number;
  medianPrice: number;
  parseQueueId: number;
  parseDate: number;
  parsePeriod: number;
  forceUpdate: boolean;
  steamItemType: SteamItemType;

  constructor() {
  }
}

export enum SteamItemType {
  SKIN = "SKIN", STICKER = "STICKER"
}