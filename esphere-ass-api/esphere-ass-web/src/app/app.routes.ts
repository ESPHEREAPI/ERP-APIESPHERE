import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AcceuilComponent } from './features/acceuil/acceuil.component';
import { AuthGuard } from './core/auth/auth.guard';
import { ProfilGuard } from './core/auth/profil.guard';
import { AdminLayoutComponentComponent } from './shared/admin-layout.component/admin-layout.component.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { UserListComponent } from './features/user-manager/user-list/user-list.component';
import { RoleGuard } from './core/auth/role-guard';
import { UserRoleComponent } from './features/user-manager/user-role/user-role.component';
import { UserFormComponent } from './features/user-manager/user-form/user-form.component';
import { UserDetailComponent } from './features/user-manager/user-detail/user-detail.component';
import { GeneralSettingsComponent } from './features/auth/general-settings/general-settings.component';
import { SecuritySettingsComponent } from './features/auth/security-settings/security-settings.component';
import { LoginComponent } from './features/auth/login/login.component';
import { LoginPlayloadComponent } from './features/login-payload/login-playload/login-playload.component';
import { CertificateListComponent } from './features/certificates/certificate-list/certificate-list.component';
import { DashboardPayloadComponent } from './features/dashboard-payload/dashboard-payload/dashboard-payload.component';
import { CertificateFormComponent } from './features/certificates-form/certificate-form/certificate-form.component';
import { TelechargementComponent } from './features/certificates-pdf/telechargement/telechargement.component';
import { AdminAgenceComponent } from './features/admin-agence/admin-agence.component';
import { StockDashboardComponent } from './features/stock/stock-dashboard/stock-dashboard.component';
import { StockListComponent } from './features/stock/stock-list/stock-list.component';
import { StockHistoriqueComponent } from './features/stock/stock-historique/stock-historique.component';


export const routes: Routes = [

  // Route HOME - Protégée mais HORS du layout ⭐
  {
    path: 'home',
    component: AcceuilComponent,
    canActivate: [AuthGuard]  // ← Protégée par AuthGuard
  },

  {
    path: 'login-payload',
    component: LoginPlayloadComponent
  },
  {


    path: '',
    canActivate: [AuthGuard],
    component: AdminLayoutComponentComponent,
    children: [

      {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [ProfilGuard],
        data: { profilsAutorises: ['CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'] }
      },

      // { path: 'users', component: UsersComponent },
      {
        path: 'users',
        component: UserListComponent,
        canActivate: [ProfilGuard, RoleGuard],
        data: {
          profilsAutorises: ['ADMINISTRATEUR'],
          permissions: ['READ'],
          permissionCheckMethod: 'any'
        }
      },
      {
        path: 'role',
        component: UserRoleComponent,
        canActivate: [RoleGuard],
        data: {
          permissions: ['READ'],
          permissionCheckMethod: 'any'
        }
      },

      {
        path: 'create',
        component: UserFormComponent,
        canActivate: [RoleGuard],
        data: {
          permissions: ['WRITE'],
          permissionCheckMethod: 'any'
        }
      },

      {
        path: 'edit/:id',
        component: UserFormComponent,
        canActivate: [RoleGuard],
        data: {
          permissions: ['UPDATE'],
          permissionCheckMethod: 'any'
        }
      },
      {
        path: 'detail/:id',
        component: UserDetailComponent,
        canActivate: [RoleGuard],
        data: {
          permissions: ['READ'],
          permissionCheckMethod: 'any'
        }
      },
      {
        path: 'general',
        component: GeneralSettingsComponent,
        canActivate: [RoleGuard],
        data: {
          permissions: ['READ'],
          permissionCheckMethod: 'any'
        }
      },
      {
        path: 'security',
        component: SecuritySettingsComponent,
        canActivate: [AuthGuard, RoleGuard],
        data: {
          permissions: ['READ'],
          permissionCheckMethod: 'any'
        }
      }, 
      {
        path: 'certificates',
        children: [
         
          {
            path: '',
            component: CertificateListComponent,
            //pathMatch: 'full',
            data: { title: 'Liste des certificats' }
          },
          {
            path: 'validation',
            component: CertificateFormComponent,
            data: { title: 'nouveaux-certificates' }
          },
          {
            path: 'downloads',
            component: TelechargementComponent,
            data: { title: 'Téléchargements PDF' }
          }
        ]
      },
      {
        path: 'admin-agences',
        component: AdminAgenceComponent,
        canActivate: [ProfilGuard],
        data: { profilsAutorises: ['ADMINISTRATEUR'] }
      },
      {
        path: 'dashboard-payload',
        component: DashboardPayloadComponent,
        canActivate: [ProfilGuard],
        data: { profilsAutorises: ['CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'] }
      },
      {
        path: 'stock',
        children: [
          {
            path: '',
            component: StockDashboardComponent,
            canActivate: [ProfilGuard],
            data: { profilsAutorises: ['CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'] }
          },
          {
            path: 'liste',
            component: StockListComponent,
            canActivate: [ProfilGuard],
            data: { profilsAutorises: ['CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'] }
          },
          {
            path: 'historique',
            component: StockHistoriqueComponent,
            canActivate: [ProfilGuard],
            data: { profilsAutorises: ['CHEF_BUREAU_AGENT', 'CHEF_BUREAU_DIRECT_SIEGE', 'ADMINISTRATEUR'] }
          },
        ]
      }

      //{ path: 'settings/general', component: GeneralSettingsComponent },
      // { path: 'settings/security', component: SecuritySettingsComponent },
    ]
  },
  { path: 'login', component: LoginComponent },
  // Route wildcard
  {
    path: '**',
    redirectTo: '/home'
  },


];



@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }