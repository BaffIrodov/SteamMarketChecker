import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";

import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { LoginFormComponent } from "./login-form/login-form.component";
import { MessageModule } from "primeng/message";
import { MessagesModule } from "primeng/messages";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { ButtonModule } from "primeng/button";
import { TableModule } from "primeng/table";
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { NavigationComponent } from "./navigation/navigation.component";
import { AdminComponent } from "./admin/admin.component";
import { ToastModule } from "primeng/toast";
import { DialogModule } from "primeng/dialog";
import { SelectButtonModule } from "primeng/selectbutton";
import { ContextMenuModule } from "primeng/contextmenu";
import { ConfigService } from "./config/config.service";
import { MultiSelectModule } from "primeng/multiselect";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { SpeedDialModule } from "primeng/speeddial";
import { SplitterModule } from "primeng/splitter";
import { SidebarModule } from "primeng/sidebar";
import { ToolbarModule } from "primeng/toolbar";
import { InputSwitchModule } from "primeng/inputswitch";
import { BlockUIModule } from "primeng/blockui";
import { CheckboxModule } from "primeng/checkbox";
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { AnalyticsComponent } from "./tables/analytics/analytics.component";
import { ChartModule } from "primeng/chart";
import { AuthGuard } from "./config/auth.guard";
import { AgGridModule } from "ag-grid-angular";
import { DialogComponent } from "./platform/dialog/dialog.component";
import { DialogRowComponent } from "./platform/dialog-row/dialog-row.component";
import { CalendarModule } from "primeng/calendar";
import { ChipsModule } from "primeng/chips";
import { InputTextareaModule } from "primeng/inputtextarea";
import { ConfirmationService, MessageService } from "primeng/api";
import { DefaultParentDialogComponent } from "./dialogs/default-parent-dialog/default-parent-dialog.component";
import { DefaultChildDialogComponent } from "./dialogs/default-child-dialog/default-child-dialog.component";
import { DropdownModule } from "primeng/dropdown";
import { AdminDialogComponent } from "./dialogs/admin-dialog/admin-dialog.component";
import { HttpHelperInterceptor } from "./interceptors/http-helper.interceptor";
import { LoadingCellRendererComponent } from "./platform/loading-cell-renderer/loading-cell-renderer.component";
import { RegistrationFormComponent } from "./registration-form/registration-form.component";
import { UserDialogComponent } from "./dialogs/user-dialog/user-dialog.component";
import { PasswordModule } from "primeng/password";
import { NgOptimizedImage } from "@angular/common";
import { RadioButtonModule } from "primeng/radiobutton";
import { KnobModule } from "primeng/knob";
import { InputNumberModule } from "primeng/inputnumber";
import { SplitButtonModule } from "primeng/splitbutton";
import { PasswordRecoverFormComponent } from "./password-recover-form/password-recover-form.component";
import { MenubarModule } from "primeng/menubar";
import { MenuModule } from "primeng/menu";
import { DefaultParentComponent } from "./tables/default-parent/default-parent.component";
import { DefaultChildComponent } from "./tables/default-child/default-child.component";

@NgModule({
  declarations: [
    AppComponent,
    LoginFormComponent,
    NavigationComponent,
    AdminComponent,
    AnalyticsComponent,
    DialogComponent,
    DialogRowComponent,
    DefaultParentDialogComponent,
    DefaultChildDialogComponent,
    AdminDialogComponent,
    LoadingCellRendererComponent,
    RegistrationFormComponent,
    UserDialogComponent,
    PasswordRecoverFormComponent,
    DefaultParentComponent,
    DefaultChildComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    MessageModule,
    MessagesModule,
    FormsModule,
    ButtonModule,
    TableModule,
    ToastModule,
    DialogModule,
    SelectButtonModule,
    ContextMenuModule,
    BrowserAnimationsModule,
    MultiSelectModule,
    SpeedDialModule,
    SplitterModule,
    SidebarModule,
    ToolbarModule,
    InputSwitchModule,
    BlockUIModule,
    CheckboxModule,
    ConfirmDialogModule,
    ChartModule,
    AgGridModule,
    CalendarModule,
    ChipsModule,
    InputTextareaModule,
    ReactiveFormsModule,
    DropdownModule,
    PasswordModule,
    NgOptimizedImage,
    RadioButtonModule,
    KnobModule,
    InputNumberModule,
    SplitButtonModule,
    MenubarModule,
    MenuModule
  ],
  providers:
    [ConfigService,
      AuthGuard,
      MessageService,
      ConfirmationService,
      {
        provide: HTTP_INTERCEPTORS,
        useClass: HttpHelperInterceptor,
        multi: true
      }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
