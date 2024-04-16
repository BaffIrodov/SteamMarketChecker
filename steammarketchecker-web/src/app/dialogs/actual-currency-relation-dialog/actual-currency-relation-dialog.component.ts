import { Component, EventEmitter, Input, Output } from "@angular/core";
import { Lot } from "../../dto/Lot";
import { MessageService } from "primeng/api";
import { ActualCurrencyRelation } from "../../dto/ActualCurrencyRelation";

@Component({
  selector: 'app-actual-currency-relation-dialog',
  templateUrl: './actual-currency-relation-dialog.component.html',
  styleUrls: ['./actual-currency-relation-dialog.component.scss']
})
export class ActualCurrencyRelationDialogComponent {
  @Input("openDialog") visible: boolean = false;
  @Input("actualCurrencyRelation") actualCurrencyRelation: ActualCurrencyRelation = new ActualCurrencyRelation();
  @Output() submit = new EventEmitter<any>();
  dialogTitle = "Связь рубля и условной единицы стима";

  constructor(public messageService: MessageService) {
  }

  async onSubmit($event: any) {
    this.submit.emit();
    this.visible = false;
  }
}
