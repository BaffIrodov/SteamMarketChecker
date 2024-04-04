import { Component, Input, ViewChild } from "@angular/core";
import { CellClickedEvent, ColDef, GridReadyEvent } from "ag-grid-community";
import { AgGridAngular } from "ag-grid-angular";
import { ConfirmationService, MessageService } from "primeng/api";
import { LoadingCellRendererComponent } from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import { DefaultParent } from "../../dto/DefaultParent";
import { DefaultChild } from "../../dto/DefaultChild";
import { DefaultChildService } from "../../services/default-child.service";

@Component({
  selector: "app-default-child",
  templateUrl: "./default-child.component.html",
  styleUrls: ["./default-child.component.scss"]
})
export class DefaultChildComponent {
  private _defaultParent: DefaultParent;
  filter: boolean = false;

  public get defaultParent(): DefaultParent {
    return this._defaultParent;
  }

  @Input("defaultParent")
  public set defaultParent(value: DefaultParent) {
    this._defaultParent = value;
    this.getAllDefaultChildrenByDefaultParentIdFromApi();
    this.selectedDefaultChild = new DefaultChild();
  }

  selectedDefaultChild: DefaultChild;

  showArchive = false;

  public columnDefs: ColDef[] = [
    { field: "id", headerName: "Идентификатор" },
    { field: "defaultParentId", headerName: "Идентификатор ивента" },
    { field: "name", headerName: "Название" },
    {field: 'archive', headerName: 'Архив', hide: !this.showArchive, cellRenderer: (params: { archive: any; }) => {
        return `<input disabled="true" type='checkbox' checked />`;
      } }
  ];

  // DefaultColDef sets props common to all Columns
  public defaultColDef: ColDef = {
    editable: false,
    sortable: true,
    flex: 1,
    minWidth: 100,
    filter: true,
    resizable: true,
    floatingFilter: this.filter
  };

  styles: {};
  // Data that gets displayed in the grid
  public rowData: DefaultChild[];

  editMode: boolean = false;
  openDialog: boolean = false;
  @ViewChild(AgGridAngular) agGrid!: AgGridAngular;
  public overlayLoadingTemplate = "<div class=\"loading-text\"> <span>L</span> <span>O</span> <span>A</span> <span>D</span> <span>I</span> <span>N</span> <span>G</span> </div> ";

  constructor(public defaultChildService: DefaultChildService,
              private confirmationService: ConfirmationService,
              private messageService: MessageService) {
  }

  public loadingCellRenderer: any = LoadingCellRendererComponent;
  public loadingCellRendererParams: any = {
    loadingMessage: "Загузка..."
  };

  async onGridReady(params: GridReadyEvent) {
    if (this.defaultParent && this.defaultParent.id) {
      await this.getAllDefaultChildrenByDefaultParentIdFromApi();
    }
  }

  // Example of consuming Grid Event
  onCellClicked(e: CellClickedEvent): void {
    this.selectedDefaultChild = e.data;
  }

  async getAllDefaultChildrenByDefaultParentIdFromApi() {
    if (this.defaultParent && this.defaultParent.id) {
      this.agGrid.api.showLoadingOverlay();
      this.rowData = await this.defaultChildService.getDefaultChildrenByDefaultParentId(this.defaultParent.id, this.showArchive);
    }
  }

  showFilter() {
    this.filter = !this.filter;
    const columnDefs = this.agGrid.api.getColumnDefs();
    columnDefs?.forEach((colDef: any, index: number) => {
      colDef.floatingFilter = this.filter;
    });
    if (columnDefs) {
      this.agGrid.api.setColumnDefs(columnDefs);
    }
    this.agGrid.api.refreshHeader();
  }

  async onDialogSubmit($event: any) {
    this.openDialog = false;
    await this.getAllDefaultChildrenByDefaultParentIdFromApi();
  }

  createRequestPosition() {
    this.openDialog = true;
    this.editMode = false;
  }

  updateRequestPosition() {
    this.openDialog = true;
    this.editMode = true;
  }

  archiveRequestPosition() {
    this.confirmationService.confirm({
      message: "Отправить позицию в архив?",
      accept: async () => {
        try {
          await this.defaultChildService.archiveDefaultChild(this.selectedDefaultChild.id);
          this.messageService.add({
            severity: "success",
            summary: "Успех!",
            detail: "Позиция переведена в архив",
            life: 5000
          });
          await this.getAllDefaultChildrenByDefaultParentIdFromApi();
        } catch (e: any) {
          this.messageService.add({
            severity: "error",
            summary: "Ошибка...",
            detail: e.error.message,
            life: 5000
          });
        }
      },
      reject: () => {
        // can implement on cancel
      }
    });
  }

  async showArchivePressed() {
    if (this.agGrid) {
      this.agGrid.columnApi.setColumnVisible("archive", this.showArchive);
    }
    await this.getAllDefaultChildrenByDefaultParentIdFromApi();
  }
}
