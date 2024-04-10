import { Component, ViewChild } from "@angular/core";
import { SteamItem, SteamItemType } from "../../dto/SteamItem";
import { CellClickedEvent, ColDef, ValueGetterParams } from "ag-grid-community";
import { LoadingCellRendererComponent } from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import { AgGridAngular } from "ag-grid-angular";
import { SteamItemService } from "../../services/steam-item.service";
import { Router } from "@angular/router";
import { HttpClient } from "@angular/common/http";
import { ConfirmationService, MessageService } from "primeng/api";
import { Lot } from "../../dto/Lot";
import { LotService } from "../../services/lot.service";

@Component({
  selector: 'app-lot',
  templateUrl: './lot.component.html',
  styleUrls: ['./lot.component.scss']
})
export class LotComponent {
  selectedLot: Lot;
  dialogEditMode: boolean = false;
  filter: boolean = false;
  loading: boolean = false;
  showOnlyActual: boolean = false;
  showOnlyCompleteness: boolean = false;
  showOnlyProfitability: boolean = false;

  getProfitPercent(): (params: ValueGetterParams) => string {
    return (params: ValueGetterParams): string => {
      return (params?.data?.profit / params?.data?.realPrice).toString();
    }
  }

  public columnDefs: ColDef[] = [
    { field: "id", headerName: "Идентификатор" },
    { field: "skin.name", headerName: "Название айтема" },
    {
      field: "completeness", headerName: "Все составляющие с актуальной ценой", cellRenderer: (params: { completeness: any; }) => {
        return params.completeness ? `<input disabled="true" type="checkbox" checked />` : `<input disabled="true" type="checkbox" />`
      }
    },
    {
      field: "profitability", headerName: "Выгодная покупка", cellRenderer: (params: { profitability: any; }) => {
        return params.profitability ? `<input disabled="true" type="checkbox" checked />` : `<input disabled="true" type="checkbox" />`
      }
    },
    {
      field: "actual", headerName: "Актуально", cellRenderer: (params: { actual: any; }) => {
        return params.actual ? `<input disabled="true" type="checkbox" checked />` : `<input disabled="true" type="checkbox" />`
      }
    },
    { field: "profit", headerName: "Чистый профит" },
    { valueGetter: this.getProfitPercent, headerName: "Профит в процентах" },
    { field: "convertedPrice", headerName: "Конвертированная цена" },
    { field: "realPrice", headerName: "Реальная цена" },
    {
      field: "priceCalculatingDate",
      headerName: "Дата последнего расчета стоимости",
      cellRenderer: (data: { value: number }) => {
        return data.value ? (new Date(data.value * 1000)).toLocaleString() : "";
      }
    },
    {
      field: "parseDate",
      headerName: "Дата последнего парсинга",
      cellRenderer: (data: { value: number }) => {
        return data.value ? (new Date(data.value * 1000)).toLocaleString() : "";
      }
    },
    { field: "stickersAsString", headerName: "Стикеры в виде строки" },
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
  public rowData!: Lot[];

  @ViewChild(AgGridAngular) agGrid!: AgGridAngular;
  public overlayLoadingTemplate =
    "<span class=\"ag-overlay-loading-center\">Загрузка...</span>";

  constructor(public lotService: LotService,
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
    await this.getAllLots();
  }

  async getAllLots() {
    this.agGrid.api.showLoadingOverlay();
    this.rowData = await this.lotService.getAllLots(this.showOnlyActual, this.showOnlyCompleteness, this.showOnlyProfitability);
    this.loading = false;
  }

  // Example of consuming Grid Event
  onCellClicked(e: CellClickedEvent): void {
    this.selectedLot = e.data;
  }

  // Example using Grid's API
  clearSelection(): void {
    this.agGrid.api.deselectAll();
  }

  async checkboxPressed() {
    await this.getAllLots();
  }
}
