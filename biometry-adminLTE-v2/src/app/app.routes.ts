import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login/login.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout/admin-layout.component';

import { DashboardComponent } from './pages/dashboard/dashboard/dashboard.component';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';
import { NetworkErrorComponent } from './pages/errors/network-error/network-error.component';
import { HttpErrorComponent } from './pages/errors/http-error/http-error.component';
import { PublicGuard } from './auth/public.guard';
import { AuthGuard } from './auth/auth.guard';
import { ConsultationComponent } from './components/consultation/consultation.component';
import { OrdonnanceComponent } from './components/ordonnance/ordonnance.component';
import { ExamenComponent } from './components/examen/examen.component';
import { EmployeComponent } from './components/employe/employe.component';
import { ProfilComponent } from './components/profil/profil.component';
import { PermissionComponent } from './components/permission/permission.component';
import { InternauteComponent } from './components/internaute/internaute.component';
import { MenuComponent } from './components/menu/menu.component';
import { ParametreComponent } from './components/parametre/parametre.component';
import { VilleComponent } from './components/ville/ville.component';
import { PrestataireComponent } from './components/prestataire/prestataire.component';
import { OrdonnanceDetailComponent } from './components/ordonnance/ordonnance-detail/ordonnance-detail.component';
import { ExamenDetailComponent } from './components/examen/examen-detail/examen-detail.component';
import { OtpGuard } from './auth/otp.guard';
import { visiteInfoResolver } from './services/visite-info.resolver';
import { OrdonnanceAjouterComponent } from './components/prestataire/ordonnance-ajouter/ordonnance-ajouter.component';
import { ExamenAjouterComponent } from './components/prestataire/examen-ajouter/examen-ajouter/examen-ajouter.component';
import { ConsultationAjouterComponent } from './components/prestataire/consultation-ajouter/consultation-ajouter/consultation-ajouter.component';

export const routes: Routes = [


  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [PublicGuard]  // ⭐ NOUVEAU
  },
  {
    path: 'forgot-password',
    component: ForgotPasswordComponent,
    canActivate: [PublicGuard]  // ⭐ NOUVEAU
  },
  {
    path: 'network-error',
    component: NetworkErrorComponent,
    //  canActivate: [PublicGuard]  // ⭐ NOUVEAU
  },
  {
    path: 'error',
    component: HttpErrorComponent
  },
 // ── Routes prestataire — layout AdminLTE + OtpGuard ───
{
  path: 'public/admin',
  component: AdminLayoutComponent,
   canActivate: [OtpGuard],
  // PAS de AuthGuard ici — OtpGuard gère l'auth
  children: [
    {
      path: 'consultation/ajouter/:visiteCode',
      component: ConsultationAjouterComponent,
    //  canActivate: [OtpGuard],
      resolve: { visiteInfo: visiteInfoResolver },
      data: {
        breadcrumb:   'Ajouter consultation',
        pageTitle:    'Ajouter une consultation',
        pageSubtitle: 'Saisie prestataire'
      }
    },
    {
      path: 'ordonnance/ajouter/:visiteCode',
      component: OrdonnanceAjouterComponent,
    // canActivate: [OtpGuard],
      resolve: { visiteInfo: visiteInfoResolver },
      data: {
        breadcrumb:   'Ajouter ordonnance',
        pageTitle:    'Ajouter une ordonnance',
        pageSubtitle: 'Saisie prestataire'
      }
    },
    {
      path: 'examen/ajouter/:visiteCode',
      component: ExamenAjouterComponent,
     // canActivate: [OtpGuard],
      resolve: { visiteInfo: visiteInfoResolver },
      data: {
        breadcrumb:   'Ajouter examens/actes',
        pageTitle:    'Ajouter les examens/actes',
        pageSubtitle: 'Saisie prestataire'
      }
    }
  ]
},

// ── Routes protégées — layout AdminLTE + AuthGuard ─────
{
  path: 'public/admin',
  component: AdminLayoutComponent,
  canActivate: [AuthGuard],
  children: [
    {
      path: 'accueil',
      component: DashboardComponent,
      data: {
        breadcrumb:   'BREADCRUMB.DASHBOARD',
        pageSubtitle: 'PAGE.DASHBOARD.SUBTITLE'
      }
    },
    {
      path: 'consultation',
      component: ConsultationComponent,
      data: {
        breadcrumb:   'BREADCRUMB.CONSULTATION',
        pageTitle:    'PAGE.CONSULTATION.TITLE',
        pageSubtitle: 'PAGE.CONSULTATION.SUBTITLE'
      }
    },
    {
      path: 'ordonnance',
      component: OrdonnanceComponent,
      data: {
        breadcrumb:   'BREADCRUMB.ORDONNANCE',
        pageTitle:    'PAGE.ORDONNANCE.TITLE',
        pageSubtitle: 'PAGE.ORDONNANCE.SUBTITLE'
      }
    },
    {
      path: 'ordonnance/:prestationId',
      component: OrdonnanceDetailComponent,
      data: {
        breadcrumb:   'Détail ordonnance',
        pageTitle:    'Détail ordonnance',
        pageSubtitle: 'Validation des médicaments'
      }
    },
    {
      path: 'examen',
      component: ExamenComponent,
      data: {
        breadcrumb:   'BREADCRUMB.EXAMEN',
        pageTitle:    'PAGE.EXAMEN.TITLE',
        pageSubtitle: 'PAGE.EXAMEN.SUBTITLE'
      }
    },
    {
      path: 'examen/:prestationId',
      component: ExamenDetailComponent,
      data: {
        breadcrumb:   'Détail examen',
        pageTitle:    'Détail examen',
        pageSubtitle: 'Validation des examens'
      }
    },
    {
      path: 'administration/employe',
      component: EmployeComponent,
      data: {
        breadcrumb:   'BREADCRUMB.EMPLOYE',
        pageTitle:    'PAGE.EMPLOYE.TITLE',
        pageSubtitle: 'PAGE.EMPLOYE.SUBTITLE'
      }
    },
    {
      path: 'administration/profil',
      component: ProfilComponent,
      data: {
        breadcrumb:   'BREADCRUMB.PROFIL',
        pageTitle:    'PAGE.PROFIL.TITLE',
        pageSubtitle: 'PAGE.PROFIL.SUBTITLE'
      }
    },
    {
      path: 'administration/permission',
      component: PermissionComponent,
      data: {
        breadcrumb:   'BREADCRUMB.PERMISSION',
        pageTitle:    'PAGE.PERMISSION.TITLE',
        pageSubtitle: 'PAGE.PERMISSION.SUBTITLE'
      }
    },
    {
      path: 'administration/prestataire',
      component: PrestataireComponent,
      data: {
        breadcrumb:   'BREADCRUMB.PRESTATAIRE',
        pageTitle:    'PAGE.PRESTATAIRE.TITLE',
        pageSubtitle: 'PAGE.PRESTATAIRE.SUBTITLE'
      }
    },
    {
      path: 'internaute',
      component: InternauteComponent,
      data: {
        breadcrumb:   'BREADCRUMB.INTERNAUTE',
        pageTitle:    'PAGE.INTERNAUTE.TITLE',
        pageSubtitle: 'PAGE.INTERNAUTE.SUBTITLE'
      }
    },
    {
      path: 'menu',
      component: MenuComponent,
      data: {
        breadcrumb:   'BREADCRUMB.MENU',
        pageTitle:    'PAGE.MENU.TITLE',
        pageSubtitle: 'PAGE.MENU.SUBTITLE'
      }
    },
    {
      path: 'parametre',
      component: ParametreComponent,
      data: {
        breadcrumb:   'BREADCRUMB.PARAMETRE',
        pageTitle:    'PAGE.PARAMETRE.TITLE',
        pageSubtitle: 'PAGE.PARAMETRE.SUBTITLE'
      }
    },
    {
      path: 'regionalisation/ville',
      component: VilleComponent,
      data: {
        breadcrumb:   'BREADCRUMB.VILLE',
        pageTitle:    'PAGE.VILLE.TITLE',
        pageSubtitle: 'PAGE.VILLE.SUBTITLE'
      }
    }
  ]
},
  {
    path: '**',
    component: HttpErrorComponent,
    data: { errorCode: 404 }
  }
];
