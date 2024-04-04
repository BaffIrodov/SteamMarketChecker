import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginFormComponent} from "./login-form/login-form.component";
import {NavigationComponent} from "./navigation/navigation.component";
import {AdminComponent} from "./admin/admin.component";
import {AuthGuard} from "./config/auth.guard";
import { AnalyticsComponent } from "./tables/analytics/analytics.component";
import {RegistrationFormComponent} from "./registration-form/registration-form.component";
import {PasswordRecoverFormComponent} from "./password-recover-form/password-recover-form.component";
import { DefaultParentComponent } from "./tables/default-parent/default-parent.component";

const routes: Routes = [
    {path: '', component: NavigationComponent, canActivate: [AuthGuard]},
    {path: 'login', component: LoginFormComponent},
    {path: 'registration', component: RegistrationFormComponent},
    {path: 'password-recover', component: PasswordRecoverFormComponent},
    {path: 'admin', component: AdminComponent,canActivate: [AuthGuard]},
    {path: 'event', component: DefaultParentComponent,canActivate: [AuthGuard]},
    {path: 'analytics', component: AnalyticsComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
