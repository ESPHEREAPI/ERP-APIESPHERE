import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { HomeComponent } from './features/home/home.component';
import { ForgotPasswordComponent } from './features/auth/forgot-password/forgot-password.component';
import { ContactUsComponent } from './features/auth/contact-us/contact-us.component';
import { AuthGuard } from './core/auth/auth.guard';
import { AdminLayoutComponentComponent } from './shared/admin-layout.component/admin-layout.component.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { AdherentListComponent } from './features/adherent-list/adherent-list.component';
import { AdherentFormComponent } from './features/adherent-form/adherent-form.component';
import { AdherentProfileComponent } from './features/adherent-profile/adherent-profile.component';
import { DetailConsomationAdherentComponent } from './features/detail-consomation-adherent/detail-consomation-adherent.component';
import { UserRoleComponent } from './features/user-manager/user-role/user-role.component';
import { RoleGuard } from './core/auth/role-guard';
import { UserFormComponent } from './features/user-manager/user-form/user-form.component';
import { UserDetailComponent } from './features/user-manager/user-detail/user-detail.component';
import { GeneralSettingsComponent } from './features/general-settings/general-settings.component';
import { SecuritySettingsComponent } from './features/security-settings/security-settings.component';
import { SubscriberListComponent } from './features/souscripteur/subscriber-list/subscriber-list.component';
import { SubscriberPageComponent } from './features/souscripteur/subscriber-page/subscriber-page.component';
import { SubscriberDetailComponent } from './features/souscripteur/subscriber-detail/subscriber-detail.component';
import { ActivateComponent } from './features/auth/activate/activate.component';
import { CertificateListComponent } from './features/certificates/certificate-list/certificate-list.component';
import { DashboardPayloadComponent } from './features/dashboard-payload/dashboard-payload/dashboard-payload.component';
import { LoginPlayloadComponent } from './features/login-payload/login-playload/login-playload.component';
import { DashboardAdherentComponent } from './features/dashboard-adherent/dashboard-adherent.component';


export const routes: Routes = [
  // ✅ Route de login SANS protection (accessible à tous)
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'login-payload',
    component: LoginPlayloadComponent
  },

  // ✅ Route home publique
  {
    path: 'home',
    component: HomeComponent
  },

  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'contact-us', component: ContactUsComponent },
   {path: 'activate', component: ActivateComponent },

  // ✅ Routes protégées sous AdminLayout
  {
    path: '',
    canActivate: [AuthGuard], // Seulement AuthGuard ici
    component: AdminLayoutComponentComponent,
    children: [
      // Redirection par défaut vers dashboard
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },


       {
        path: 'dashboard',
        component: DashboardComponent
      },
      {
        path: 'dashboard-adherent',
        component: DashboardAdherentComponent
      },
        {
        path: 'dashboard-payload',
        component: DashboardPayloadComponent
      },
      {
        path: 'adherents',
        children: [
          {
            path: '',
            redirectTo: 'adherents-list',
            pathMatch: 'full'
          },
          {
            path: 'adherents-list',
            component: AdherentListComponent,
            data: { title: 'Liste des Adhérents' }
          },
          {
            path: 'create',
            component: AdherentFormComponent,
            data: { title: 'Créer un Adhérent' }
          },
          {
            path: ':id',
            component: AdherentProfileComponent,
            data: { title: 'Profil Adhérent' }
          },
          {
            path: ':id/edit',
            component: AdherentFormComponent,
            data: { title: 'Modifier un Adhérent' }
          }
        ]
      },
      {
        path: 'historique',
        component: DetailConsomationAdherentComponent,
        data: { title: 'historique un Adhérent' }
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
      { path: 'subscribers', component: SubscriberListComponent },
      { path: 'subscribers/new', component: SubscriberPageComponent },
      { path: 'subscribers/edit/:id', component: SubscriberPageComponent },
      { path: 'subscribers/:id', component: SubscriberDetailComponent }, 
      
      {
        path: 'security',
        component: SecuritySettingsComponent,
        canActivate: [RoleGuard],
        data: {
          permissions: ['READ'],
          permissionCheckMethod: 'any'
        }
      },
      {
        path: 'certificate-payload',
        component: CertificateListComponent
      },
    ]
  },

  // ✅ Route wildcard pour capturer les URLs invalides
  {
    path: '**',
    redirectTo: 'login'
  }
];