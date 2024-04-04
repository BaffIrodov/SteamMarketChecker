import { Component, EventEmitter, Input, Output } from "@angular/core";
import { MessageService } from "primeng/api";
import { DefaultChildService } from "../../services/default-child.service";
import { DefaultChild } from "../../dto/DefaultChild";

@Component({
  selector: "app-default-child-dialog",
  templateUrl: "./default-child-dialog.component.html",
  styleUrls: ["./default-child-dialog.component.scss"]
})
export class DefaultChildDialogComponent {

  @Input("openDialog") visible: boolean = false;
  @Input("item") item: DefaultChild = new DefaultChild();
  @Input("editMode") editMode: boolean;
  @Input("defaultParentId") defaultParentId: number;
  @Output() submit = new EventEmitter<any>();
  @Output() visibleChange = new EventEmitter<any>();
  dialogTitle = "Регистрация ребенка";
  products: any[] = [];

  constructor(private defaultChildService: DefaultChildService,
              public messageService: MessageService) {
  }

  async ngOnInit() {
    if (this.editMode) {
      this.dialogTitle = "Редактирование ребенка";
    } else {
      this.item = new DefaultChild();
      this.dialogTitle = "Регистрация ребенка";
    }
    if (!!this.defaultParentId) {
      this.item.defaultParentId = this.defaultParentId;
    }
  }

  async onSubmit($event: any) {
    if ($event !== null) { // null передается, если закрыть форму без сохранения на крестик
      if (this.editMode) {
        await this.updateDefaultChild($event);
      } else {
        await this.createDefaultChild($event);
      }
    }
    this.submit.emit();
    this.visible = false;
  }

  async createDefaultChild(defaultChild: DefaultChild) {
    try {
      await this.defaultChildService.createDefaultChild(defaultChild);
      this.messageService.add({
        severity: "success",
        summary: "Успех!",
        detail: "Дефолт ребенок заведен",
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

  async updateDefaultChild(defaultChild: DefaultChild) {
    try {
      await this.defaultChildService.updateDefaultChild(defaultChild.id, defaultChild);
      this.messageService.add({
        severity: "success",
        summary: "Успех!",
        detail: "Дефолт ребенок обновлен",
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
