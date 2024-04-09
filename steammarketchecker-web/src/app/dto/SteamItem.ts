export class SteamItem {
  id: number;
  steamItemId: number;
  name: string;
  minPrice: number;
  medianPrice: number;
  parseQueueId: number;
  parseDate: number;
  forceUpdate: boolean;
  steamItemType: SteamItemType;

  constructor() {
  }
}

export enum SteamItemType {
  SKIN = "SKIN", STICKER = "STICKER"
}