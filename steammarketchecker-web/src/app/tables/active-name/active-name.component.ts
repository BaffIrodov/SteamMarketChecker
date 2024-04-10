import { Component, ViewChild } from "@angular/core";
import { DefaultParent } from "../../dto/DefaultParent";
import { CellClickedEvent, ColDef } from "ag-grid-community";
import { LoadingCellRendererComponent } from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import { AgGridAngular } from "ag-grid-angular";
import { DefaultParentService } from "../../services/default-parent.service";
import { Router } from "@angular/router";
import { HttpClient } from "@angular/common/http";
import { ConfirmationService, MessageService } from "primeng/api";
import { ActiveNameService } from "../../services/active-name.service";
import { ActiveName } from "../../dto/ActiveName";

@Component({
  selector: "app-active-name",
  templateUrl: "./active-name.component.html",
  styleUrls: ["./active-name.component.scss"]
})
export class ActiveNameComponent {
  selectedActiveName: ActiveName;
  dialogEditMode: boolean = false;
  filter: boolean = false;
  openDialog: boolean = false;
  loading: boolean = false;
  showArchive: boolean = false;

  public columnDefs: ColDef[] = [
    { field: "id", headerName: "Идентификатор" },
    { field: "itemName", headerName: "Название" },
    { field: "parseItemCount", headerName: "Количество айтемов для парсинга" },
    { field: "parsePeriod", headerName: "Период парсинга" },
    {
      field: "lastParseDate",
      headerName: "Дата последнего парсинга",
      cellRenderer: (data: { value: number }) => {
        return data.value ? (new Date(data.value * 1000)).toLocaleString() : "";
      }
    },
    {
      field: "forceUpdate", headerName: "Принудительный апдейт", cellRenderer: (params: { forceUpdate: any; }) => {
        return params.forceUpdate ? `<input disabled="true" type="checkbox" checked />` : `<input disabled="true" type="checkbox" />`
      }
    },
    {
      field: "archive", headerName: "Архив", hide: !this.showArchive, cellRenderer: (params: { archive: any; }) => {
        return params.archive ? `<input disabled="true" type="checkbox" checked />` : `<input disabled="true" type="checkbox" />`
      }
    }
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

  public loadingCellRenderer: any = LoadingCellRendererComponent;
  public loadingCellRendererParams: any = {
    loadingMessage: "Загрузка..."
  };

  // Data that gets displayed in the grid
  public rowData!: ActiveName[];

  @ViewChild(AgGridAngular) agGrid!: AgGridAngular;
  public overlayLoadingTemplate =
    "<span class=\"ag-overlay-loading-center\">Загрузка...</span>";

  constructor(public activeNameService: ActiveNameService,
              public router: Router,
              public http: HttpClient,
              private confirmationService: ConfirmationService,
              private messageService: MessageService) {
  }

  async ngOnInit() {
    this.loading = true;
  }

  showFilter() {
    this.filter = !this.filter;
    const columnDefs = this.agGrid.api.getColumnDefs();
    if (columnDefs) {
      columnDefs.forEach((colDef: any, index: number) => {
        colDef.floatingFilter = this.filter;
      });
      this.agGrid.api.setColumnDefs(columnDefs);
      this.agGrid.api.refreshHeader();
    }
  }

  async onGridReady(grid: any) {
    this.agGrid = grid;
    await this.getAllActiveNames();
  }

  async getAllActiveNames() {
    this.agGrid.api.showLoadingOverlay();
    this.rowData = await this.activeNameService.getAllActiveNames(this.showArchive);
    this.loading = false;
  }

  // Example of consuming Grid Event
  onCellClicked(e: CellClickedEvent): void {
    this.selectedActiveName = e.data;
  }

  // Example using Grid's API
  clearSelection(): void {
    this.agGrid.api.deselectAll();
  }

  async onDialogSubmit($event: any) {
    this.openDialog = false;
    if ($event) {
      await this.getAllActiveNames();
    }
  }

  createActiveName() {
    this.openDialog = true;
    this.dialogEditMode = false;
  }

  updateActiveName() {
    if (this.selectedActiveName) {
      this.openDialog = true;
      this.dialogEditMode = true;
    }
  }

  archiveActiveName() {
    const archiveMessage = this.selectedActiveName.archive ? "Вернуть позицию из архива?" : "Отправить позицию в архив?";
    const successDetail = this.selectedActiveName.archive ? "Позиция переведена из архива" : "Позиция переведена в архив";
    this.confirmationService.confirm({
      message: archiveMessage,
      accept: async () => {
        try {
          await this.activeNameService.archiveActiveName(this.selectedActiveName.id);
          this.messageService.add({
            severity: "success",
            summary: "Успех!",
            detail: successDetail,
            life: 5000
          });
          await this.getAllActiveNames();
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

  forceUpdateActiveName() {
    const archiveMessage = "Апдейтнуть позицию?";
    const successDetail = "Позиция будет апдейтнута";
    this.confirmationService.confirm({
      message: archiveMessage,
      accept: async () => {
        try {
          await this.activeNameService.forceUpdateActiveName(this.selectedActiveName.id);
          this.messageService.add({
            severity: "success",
            summary: "Успех!",
            detail: successDetail,
            life: 5000
          });
          await this.getAllActiveNames();
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
    await this.getAllActiveNames();
  }
}
