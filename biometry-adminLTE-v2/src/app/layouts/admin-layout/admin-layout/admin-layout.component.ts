// src/app/layouts/admin-layout/admin-layout.component.ts

import { Component, OnInit, OnDestroy, Renderer2, HostListener } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../../../components/header/header/header.component';
import { SidebarComponent } from '../../../components/sidebar/sidebar/sidebar.component';
import { FooterComponent } from '../../../components/footer/footer/footer.component';
import { ContentHeaderComponent } from '../../../components/content-header/content-header/content-header.component';
import { ControlSidebarComponent } from '../../../components/control-sidebar/control-sidebar/control-sidebar.component';

import { User } from '../../../models/user';
import { AuthService } from '../../../auth/auth.service';


declare var $: any;

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    HeaderComponent,
    SidebarComponent,
    FooterComponent,
    ContentHeaderComponent,
    ControlSidebarComponent
  ],
  templateUrl: './admin-layout.component.html',
  styleUrls: ['./admin-layout.component.css']
})
export class AdminLayoutComponent implements OnInit, OnDestroy {
  user: User | null = null;
  userSession: any ;

  constructor(
    private renderer: Renderer2,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Ajouter les classes AdminLTE au body
    this.renderer.addClass(document.body, 'hold-transition');
    this.renderer.addClass(document.body, 'skin-blue');
    this.renderer.addClass(document.body, 'sidebar-mini');
    
    // Sur desktop, ne pas réduire par défaut
    if (window.innerWidth >= 768) {
      this.renderer.removeClass(document.body, 'sidebar-collapse');
    } else {
      // Sur mobile, fermer par défaut
      this.renderer.removeClass(document.body, 'sidebar-open');
    }

    // Récupérer l'utilisateur connecté
    const currentUser = this.authService.getStoredUser();
    console.log("user.....",currentUser);
    if (currentUser) {
      this.user = currentUser;
    }
  }

  ngOnDestroy(): void {
    // Nettoyer les classes du body
    this.renderer.removeClass(document.body, 'hold-transition');
    this.renderer.removeClass(document.body, 'skin-blue');
    this.renderer.removeClass(document.body, 'sidebar-mini');
    this.renderer.removeClass(document.body, 'sidebar-collapse');
    this.renderer.removeClass(document.body, 'sidebar-open');
  }

  @HostListener('window:resize')
  onResize() {
    const body = document.body;
    if (window.innerWidth < 768) {
      this.renderer.removeClass(body, 'sidebar-open');
      this.renderer.removeClass(body, 'sidebar-collapse');
    }
  }
}
