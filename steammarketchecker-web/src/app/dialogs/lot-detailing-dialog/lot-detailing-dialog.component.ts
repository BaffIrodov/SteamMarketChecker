import { Component, EventEmitter, Input, Output } from "@angular/core";
import { MessageService } from "primeng/api";
import { Lot } from "../../dto/Lot";

@Component({
  selector: "app-lot-detailing-dialog",
  templateUrl: "./lot-detailing-dialog.component.html",
  styleUrls: ["./lot-detailing-dialog.component.scss"]
})
export class LotDetailingDialogComponent {

  @Input("openDialog") visible: boolean = false;
  @Input("lot") lot: Lot = new Lot();
  @Output() submit = new EventEmitter<any>();
  dialogTitle = "Просмотр содержимого лота";

  constructor(public messageService: MessageService) {
  }

  async onSubmit($event: any) {
    this.submit.emit();
    this.visible = false;
  }
}
