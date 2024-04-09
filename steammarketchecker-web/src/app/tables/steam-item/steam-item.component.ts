import { Component, ViewChild } from "@angular/core";
import { CellClickedEvent, ColDef } from "ag-grid-community";
import { LoadingCellRendererComponent } from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import { AgGridAngular } from "ag-grid-angular";
import { Router } from "@angular/router";
import { HttpClient } from "@angular/common/http";
import { ConfirmationService, MessageService } from "primeng/api";
import { SteamItem, SteamItemType } from "../../dto/SteamItem";
import { SteamItemService } from "../../services/steam-item.service";

@Component({
  selector: 'app-steam-item',
  templateUrl: './steam-item.component.html',
  styleUrls: ['./steam-item.component.scss']
})
export class SteamItemComponent {
  selectedSteamItem: SteamItem;
  dialogEditMode: boolean = false;
  filter: boolean = false;
  loading: boolean = false;
  showSkins: boolean = false;

  public columnDefs: ColDef[] = [
    { field: "id", headerName: "Идентификатор" },
    { field: "steamItemId", headerName: "Идентификатор стима для айтема" },
    { field: "name", headerName: "Название айтема" },
    { field: "minPrice", headerName: "Минимальная цена" },
    { field: "medianPrice", headerName: "Медианная цена" },
    {
      field: "parseDate",
      headerName: "Дата последнего парсинга",
      cellRenderer: (data: { value: number }) => {
        return data.value ? (new Date(data.value * 1000)).toLocaleString() : "";
      }
    },
    {
      field: "forceUpdate", headerName: "Принудительный апдейт", cellRenderer: (params: { forceUpdate: any; }) => {
        return `<input disabled="true" type="checkbox" checked />`;
      }
    },
    { field: "steamItemType", headerName: "Тип айтема" },
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
  public rowData!: SteamItem[];

  @ViewChild(AgGridAngular) agGrid!: AgGridAngular;
  public overlayLoadingTemplate =
    "<span class=\"ag-overlay-loading-center\">Загрузка...</span>";

  constructor(public steamItemService: SteamItemService,
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
    await this.getAllSteamItems();
  }

  async getAllSteamItems() {
    this.agGrid.api.showLoadingOverlay();
    this.rowData = await this.steamItemService.getAllSteamItems(this.showSkins ? SteamItemType.SKIN : SteamItemType.STICKER);
    this.loading = false;
  }

  // Example of consuming Grid Event
  onCellClicked(e: CellClickedEvent): void {
    this.selectedSteamItem = e.data;
  }

  // Example using Grid's API
  clearSelection(): void {
    this.agGrid.api.deselectAll();
  }

  forceUpdateSteamItem() {
    const archiveMessage = "Апдейтнуть позицию?";
    const successDetail = "Позиция будет апдейтнута";
    this.confirmationService.confirm({
      message: archiveMessage,
      accept: async () => {
        try {
          await this.steamItemService.forceUpdateSteamItem(this.selectedSteamItem.id);
          this.messageService.add({
            severity: "success",
            summary: "Успех!",
            detail: successDetail,
            life: 5000
          });
          await this.getAllSteamItems();
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

  async showSkinsPressed() {
    await this.getAllSteamItems();
  }
}
