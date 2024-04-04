import { Component, OnInit, ViewChild } from "@angular/core";
import { CellClickedEvent, ColDef } from "ag-grid-community";
import { LoadingCellRendererComponent } from "../../platform/loading-cell-renderer/loading-cell-renderer.component";
import { AgGridAngular } from "ag-grid-angular";
import { Router } from "@angular/router";
import { HttpClient } from "@angular/common/http";
import { ConfirmationService, MessageService } from "primeng/api";
import { DefaultParent } from "../../dto/DefaultParent";
import { DefaultParentService } from "../../services/default-parent.service";

@Component({
  selector: 'app-default-parent',
  templateUrl: './default-parent.component.html',
  styleUrls: ['./default-parent.component.scss']
})
export class DefaultParentComponent implements OnInit {
  request: DefaultParent[] = [];
  selectedEvents: DefaultParent[] = [];
  selectedDefaultParent: DefaultParent;
  dialogEditMode: boolean = false;
  filter: boolean = false;
  openDialog: boolean = false;
  loading: boolean = false;
  showArchive: boolean = false;

  public columnDefs: ColDef[] = [
    { field: "id", headerName: "Идентификатор" },
    { field: "name", headerName: "Название" },
    // {field: 'releaseDate', headerName: 'Дата' , hide: this.showArchive, cellRenderer: (data: { value: string | number | Date; }) => {
    //     return data.value ? (new Date(data.value)).toLocaleDateString() : '';
    //   }},
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
    floatingFilter: this.filter,
  };

  public loadingCellRenderer: any = LoadingCellRendererComponent;
  public loadingCellRendererParams: any = {
    loadingMessage: 'Загрузка...',
  };

  // Data that gets displayed in the grid
  public rowData!: any[];

  isProductionPlanMode = false;
  @ViewChild(AgGridAngular) agGrid!: AgGridAngular;
  public overlayLoadingTemplate =
    '<span class="ag-overlay-loading-center">Загрузка...</span>';

  constructor(public defaultParentService: DefaultParentService,
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
      columnDefs.forEach((colDef:any, index:number)=> {
        colDef.floatingFilter = this.filter;
      });
      this.agGrid.api.setColumnDefs(columnDefs);
      this.agGrid.api.refreshHeader();
    }
  }

  async checkRequest(selected: any) {
    if (selected.length == 1) {
      // this.requestPositions = await this.requestService.getRequestPositionById(selected[0].id);
    } else {
      // this.requestPositions = [];
    }
  }

  async refreshDetail() {
    // this.requestPositions = await this.requestService.getRequestPositionById(this.selectedRequests[0].id);
  }

  async refreshMain() {
    // this.request = await this.requestService.getRequests(this.showArchive);
  }

  async onGridReady(grid: any) {
    this.agGrid = grid;
    await this.getAllDefaultParentsFromApi();
  }

  async getAllDefaultParentsFromApi() {
    this.agGrid.api.showLoadingOverlay();
    this.rowData = await this.defaultParentService.getAllDefaultParent(this.showArchive);
    this.loading = false;
  }

  // Example of consuming Grid Event
  onCellClicked(e: CellClickedEvent): void {
    this.selectedDefaultParent = e.data;
  }

  // Example using Grid's API
  clearSelection(): void {
    this.agGrid.api.deselectAll();
  }

  async onDialogSubmit($event: any) {
    this.openDialog = false;
    if ($event) {
      await this.getAllDefaultParentsFromApi();
    }
  }

  createDefaultParent() {
    this.openDialog = true;
    this.dialogEditMode = false
  }

  updateDefaultParent() {
    if (this.selectedDefaultParent) {
      this.openDialog = true;
      this.dialogEditMode = true;
    }
  }

  archiveRequest() {
    this.confirmationService.confirm({
      message: 'Отправить позицию в архив?',
      accept: async () => {
        try {
          await this.defaultParentService.archiveEvent(this.selectedDefaultParent.id);
          this.messageService.add({
            severity: 'success',
            summary: 'Успех!',
            detail: 'Позиция переведена в архив',
            life: 5000
          });
          await this.getAllDefaultParentsFromApi();
        } catch (e: any) {
          this.messageService.add({
            severity: 'error',
            summary: 'Ошибка...',
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
      this.agGrid.columnApi.setColumnVisible('archive', this.showArchive);
    }
    await this.getAllDefaultParentsFromApi();
  }
}
