import { Component, ViewChild } from "@angular/core";
import { CellClickedEvent, ColDef, ValueGetterParams } from "ag-grid-community";
import { LoadingCellRendererComponent } from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import { AgGridAngular } from "ag-grid-angular";
import { Router } from "@angular/router";
import { HttpClient } from "@angular/common/http";
import { ConfirmationService, MessageService } from "primeng/api";
import { Lot } from "../../dto/Lot";
import { LotService } from "../../services/lot.service";
import { ActualCurrencyRelation } from "../../dto/ActualCurrencyRelation";

@Component({
  selector: "app-lot",
  templateUrl: "./lot.component.html",
  styleUrls: ["./lot.component.scss"]
})
export class LotComponent {
  selectedLot: Lot;
  actualCurrencyRelation: ActualCurrencyRelation;
  dialogEditMode: boolean = false;
  filter: boolean = false;
  loading: boolean = false;
  showOnlyActual: boolean = false;
  showOnlyCompleteness: boolean = false;
  showOnlyProfitability: boolean = false;
  openDetailingDialog: boolean = false;
  openActualCurrencyRelationDialog: boolean = false;
  hideDates: boolean = true;
  normalizeToCurrency: boolean = true;
  roundPrices: boolean = true;
  itemsIdsWithZeroPrice: number[] = [];

  public columnDefs: ColDef[] = [
    // { field: "id", headerName: "Идентификатор" },
    { field: "skin.name", headerName: "Название айтема" },
    {
      field: "completeness",
      headerName: "Все цены известны",
      cellRenderer: (params: { completeness: any; }) => {
        return params.completeness ? `<input disabled="true" type="checkbox" checked />` : `<input disabled="true" type="checkbox" />`;
      }
    },
    {
      field: "profitability", headerName: "Выгодная покупка",
      cellRenderer: (params: { profitability: any; }) => {
        return params.profitability ? `<input disabled="true" type="checkbox" checked />` : `<input disabled="true" type="checkbox" />`;
      }
    },
    // {
    //   field: "actual", headerName: "Актуально", cellRenderer: (params: { actual: any; }) => {
    //     return params.actual ? `<input disabled="true" type="checkbox" checked />` : `<input disabled="true" type="checkbox" />`;
    //   }
    // },
    { field: "positionInListing", headerName: "Позиция в таблице" },
    {
      field: "profit", headerName: "Чистый профит (с комиссией)",
      cellRenderer: (data: { value: number }) => {
        return data.value ? data.value + (this.normalizeToCurrency ? " руб" : " УЕ стима") : "";
      }
    },
    { field: "profitPercent", headerName: "Профит в процентах" },
    {
      field: "convertedPrice", headerName: "Конвертированная цена",
      cellRenderer: (data: { value: number }) => {
        return data.value ? data.value + (this.normalizeToCurrency ? " руб" : " УЕ стима") : "";
      }
    },
    {
      field: "realPrice", headerName: "Реальная цена", cellRenderer: (data: { value: number }) => {
        return data.value ? data.value + (this.normalizeToCurrency ? " руб" : " УЕ стима") : "";
      }
    },
    {
      field: "priceCalculatingDate",
      headerName: "Дата последнего расчета стоимости",
      hide: this.hideDates,
      cellRenderer: (data: { value: number }) => {
        return data.value ? (new Date(data.value * 1000)).toLocaleString() : "";
      }
    },
    {
      field: "parseDate",
      headerName: "Дата последнего парсинга",
      hide: this.hideDates,
      cellRenderer: (data: { value: number }) => {
        return data.value ? (new Date(data.value * 1000)).toLocaleString() : "";
      }
    },
    { field: "stickersAsString", headerName: "Стикеры в виде строки" }
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
    await this.getActualCurrencyRelation();
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
    this.rowData.forEach(lot => {
      if (!!lot.profit && !!lot.convertedPrice) {
        lot.profitPercent = ((lot.profit / lot.convertedPrice) * 100).toFixed(2) + "%";
      }
      if (this.normalizeToCurrency && this.actualCurrencyRelation) {
        if (lot.convertedPrice) lot.convertedPrice = lot.convertedPrice / this.actualCurrencyRelation.relation;
        if (lot.realPrice) lot.realPrice = lot.realPrice / this.actualCurrencyRelation.relation;
        if (lot.profit) lot.profit = lot.profit / this.actualCurrencyRelation.relation;
      }
      if (this.roundPrices) {
        if (lot.convertedPrice) lot.convertedPrice = Number.parseFloat(lot.convertedPrice.toFixed(0));
        if (lot.realPrice) lot.realPrice = Number.parseFloat(lot.realPrice.toFixed(0));
        if (lot.profit) lot.profit = Number.parseFloat(lot.profit.toFixed(0));
      }
    });
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

  getLabelForDetailButton(): string {
    if (this.selectedLot && this.selectedLot?.stickers) {
      return "Подробности (стикеров: " + this.selectedLot?.stickers?.length + ")";
    }
    return "Подробности";
  }

  getLabelForCurrencyButton(): string {
    if (!this.actualCurrencyRelation) {
      return "Связь валюты (нет актуального значения)";
    }
    return "Связь валюты";
  }

  async getActualCurrencyRelation() {
    this.actualCurrencyRelation = await this.lotService.getActualCurrencyRelation();
  }

  async checkboxQueryParametersPressed() {
    await this.getAllLots();
  }

  async onDetailingDialogSubmit($event: any) {
    this.openDetailingDialog = false;
  }

  async onCurrencyDialogSubmit($event: any) {
    this.openActualCurrencyRelationDialog = false;
  }

  getAllLotsWithZeroPriceItems() {
    const lotsWithZeroPriceItems = this.rowData?.filter(e => {
      return ((e.skin.minPrice == 0))
        || (e.stickers.find(r => r.minPrice == 0) != undefined);
    });
    this.itemsIdsWithZeroPrice = lotsWithZeroPriceItems?.map(e => e.id);
    return !!lotsWithZeroPriceItems && lotsWithZeroPriceItems.length > 0;
  }
}
