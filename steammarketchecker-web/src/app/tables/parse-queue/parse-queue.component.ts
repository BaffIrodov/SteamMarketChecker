import { Component, ViewChild } from "@angular/core";
import { CellClickedEvent, ColDef } from "ag-grid-community";
import { LoadingCellRendererComponent } from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import { AgGridAngular } from "ag-grid-angular";
import { Router } from "@angular/router";
import { HttpClient } from "@angular/common/http";
import { ConfirmationService, MessageService } from "primeng/api";
import { ParseQueueService } from "../../services/parse-queue.service";
import { ParseQueue } from "../../dto/ParseQueue";

@Component({
  selector: "app-parse-queue",
  templateUrl: "./parse-queue.component.html",
  styleUrls: ["./parse-queue.component.scss"]
})
export class ParseQueueComponent {

  selectedParseQueue: ParseQueue;
  dialogEditMode: boolean = false;
  filter: boolean = false;
  loading: boolean = false;
  showArchive: boolean = false;

  public columnDefs: ColDef[] = [
    { field: "id", headerName: "Идентификатор" },
    { field: "importance", headerName: "Важность" },
    { field: "parseType", headerName: "Тип парсинга" },
    { field: "parseTarget", headerName: "Цель парсинга" },
    { field: "parseUrl", headerName: "Полный url парсинга" },
    {
      field: "archive", headerName: "Архив", cellRenderer: (params: { archive: any; }) => {
        return params.archive ? `<input disabled="true" type="checkbox" checked />` : `<input disabled="true" type="checkbox" />`;
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
  public rowData!: ParseQueue[];

  @ViewChild(AgGridAngular) agGrid!: AgGridAngular;
  public overlayLoadingTemplate =
    "<span class=\"ag-overlay-loading-center\">Загрузка...</span>";

  constructor(public parseQueueService: ParseQueueService,
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
    await this.getAllParseQueues();
  }

  async getAllParseQueues() {
    this.agGrid.api.showLoadingOverlay();
    this.rowData = await this.parseQueueService.getAllParseQueues(this.showArchive);
    this.loading = false;
  }

  // Example of consuming Grid Event
  onCellClicked(e: CellClickedEvent): void {
    this.selectedParseQueue = e.data;
  }

  // Example using Grid's API
  clearSelection(): void {
    this.agGrid.api.deselectAll();
  }

  async checkboxPressed() {
    await this.getAllParseQueues();
  }
}
