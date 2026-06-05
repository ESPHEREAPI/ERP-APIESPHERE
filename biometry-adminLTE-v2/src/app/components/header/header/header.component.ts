import { Component, ElementRef, HostListener, OnInit, Renderer2 } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { AuthService } from '../../../auth/auth.service';
import { User } from '../../../models/user';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule, TranslateModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  user: User | null = null;
  consultationCount = 0;
  ordonnanceCount = 0;
  examenCount = 0;

  dropdowns: { [key: string]: boolean } = {
    consultations: false,
    ordonnances: false,
    examens: false,
    user: false
  };

  constructor(
    private renderer: Renderer2,
    private router: Router,
    private elementRef: ElementRef,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getStoredUser();
  }

  getInitiales(): string {
    if (!this.user) return '?';
    const p = this.user.prenom?.charAt(0) || '';
    const n = this.user.nom?.charAt(0) || '';
    return (p + n).toUpperCase() || this.user.login?.substring(0, 2).toUpperCase() || '?';
  }

  toggleSidebar(event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    const body = document.body;
    if (window.innerWidth < 768) {
      body.classList.toggle('sidebar-open');
    } else {
      body.classList.toggle('sidebar-collapse');
    }
  }

  toggleDropdown(dropdown: string, event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    Object.keys(this.dropdowns).forEach(k => {
      if (k !== dropdown) this.dropdowns[k] = false;
    });
    this.dropdowns[dropdown] = !this.dropdowns[dropdown];
  }

  closeAllDropdowns(): void {
    Object.keys(this.dropdowns).forEach(k => this.dropdowns[k] = false);
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.closeAllDropdowns();
    }
  }

  logout(): void {
    this.authService.logout().subscribe(() => {
      this.router.navigate(['/login']);
    });
  }

  goToConsultations(): void {
    this.closeAllDropdowns();
    this.router.navigate(['/public/admin/consultation']);
  }

  goToOrdonnances(): void {
    this.closeAllDropdowns();
    this.router.navigate(['/public/admin/ordonnance']);
  }

  goToExamens(): void {
    this.closeAllDropdowns();
    this.router.navigate(['/public/admin/examen']);
  }

  toggleControlSidebar(): void {
    document.body.classList.toggle('control-sidebar-open');
  }
}
