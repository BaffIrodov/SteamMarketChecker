export class ParseQueue {
  id: number;
  importance: number;
  parseType: ParseType;
  parseTarget: string;
  parseUrl: string;
  archive: boolean;
  attempt: number;

  constructor() {
  }
}

export enum ParseType {
  SKIN = "SKIN", STICKER = "STICKER", LOT = "LOT"
}