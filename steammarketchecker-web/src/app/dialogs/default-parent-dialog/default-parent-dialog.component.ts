import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { DefaultParent } from "../../dto/DefaultParent";
import { MessageService } from "primeng/api";
import { DefaultParentService } from "../../services/default-parent.service";

@Component({
  selector: "app-default-parent-dialog",
  templateUrl: "./default-parent-dialog.component.html",
  styleUrls: ["./default-parent-dialog.component.scss"]
})
export class DefaultParentDialogComponent implements OnInit {

  @Input("openDialog") visible: boolean = false;
  @Input("item") item: DefaultParent = new DefaultParent();
  @Input("editMode") editMode: boolean;
  @Output() submit = new EventEmitter<any>();
  @Output() visibleChange = new EventEmitter<any>();
  dialogTitle = "Регистрация дефолт-родителя";

  constructor(private defaultParentService: DefaultParentService,
              public messageService: MessageService) {
  }

  ngOnInit(): void {
    if (this.editMode) {
      this.dialogTitle = "Редактирование дефолт-родителя";
    } else {
      this.item = new DefaultParent();
      this.dialogTitle = "Регистрация дефолт-родителя";
    }
  }

  async onSubmit($event: any) {
    if ($event !== null) { // null передается, если закрыть форму без сохранения на крестик
      if (this.editMode) {
        await this.updateDefaultParent($event);
      } else {
        await this.createDefaultParent($event);
      }
    }
    this.submit.emit($event);
    this.visible = false;
  }

  async createDefaultParent(defaultParent: DefaultParent) {
    try {
      await this.defaultParentService.createDefaultParent(defaultParent);
      this.messageService.add({
        severity: "success",
        summary: "Успех!",
        detail: "Дефолт родитель заведен",
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

  async updateDefaultParent(defaultParent: DefaultParent) {
    try {
      await this.defaultParentService.updateDefaultParent(defaultParent.id, defaultParent);
      this.messageService.add({
        severity: "success",
        summary: "Успех!",
        detail: "Дефолт родитель обновлён",
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
