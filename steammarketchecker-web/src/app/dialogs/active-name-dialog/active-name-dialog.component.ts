import { Component, EventEmitter, Input, Output } from "@angular/core";
import { MessageService } from "primeng/api";
import { ActiveNameService } from "../../services/active-name.service";
import { ActiveName } from "../../dto/ActiveName";

@Component({
  selector: "app-active-name-dialog",
  templateUrl: "./active-name-dialog.component.html",
  styleUrls: ["./active-name-dialog.component.scss"]
})
export class ActiveNameDialogComponent {

  @Input("openDialog") visible: boolean = false;
  @Input("item") item: ActiveName = new ActiveName();
  @Input("editMode") editMode: boolean;
  @Output() submit = new EventEmitter<any>();
  dialogTitle = "";

  constructor(private activeNameService: ActiveNameService,
              public messageService: MessageService) {
  }

  async ngOnInit() {
    if (this.editMode) {
      this.dialogTitle = "Редактирование активной позиции";
    } else {
      this.item = new ActiveName();
      this.dialogTitle = "Создание активной позиции";
    }
  }

  async onSubmit($event: any) {
    if ($event !== null) { // null передается, если закрыть форму без сохранения на крестик
      if (this.editMode) {
        await this.updateActiveName($event);
      } else {
        await this.createActiveName($event);
      }
    }
    this.submit.emit();
    this.visible = false;
  }

  async createActiveName(activeName: ActiveName) {
    try {
      await this.activeNameService.createActiveName(activeName);
      this.messageService.add({
        severity: "success",
        summary: "Успех!",
        detail: "Активная позиция заведена",
        life: 5000
      });
    } catch (e: any) {
      this.messageService.add({
        severity: "error",
        summary: "Ошибка...",
        detail: e.error.message,
        life: 5000
      });
    }
  }

  async updateActiveName(activeName: ActiveName) {
    try {
      await this.activeNameService.updateActiveName(activeName.id, activeName);
      this.messageService.add({
        severity: "success",
        summary: "Успех!",
        detail: "Активная позиция обновлена",
        life: 5000
      });
    } catch (e: any) {
      this.messageService.add({
        severity: "error",
        summary: "Ошибка...",
        detail: e.error.message,
        life: 5000
      });
    }
  }
}
