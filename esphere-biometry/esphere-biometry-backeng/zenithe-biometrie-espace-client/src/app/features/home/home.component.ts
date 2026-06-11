import { CommonModule } from '@angular/common';
import { Component, HostListener, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

 currentLanguage = 'Français';
  isPortrait = false;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.checkOrientation();
  }

  @HostListener('window:resize')
  onResize(): void {
    this.checkOrientation();
  }

  private checkOrientation(): void {
    this.isPortrait = window.innerHeight > window.innerWidth;
  }

  changeLanguage(lang: string): void {
    this.currentLanguage = lang;
    // Logique de changement de langue
    if (lang === 'English') {
      // Naviguer vers la version anglaise
      this.router.navigate(['/en']);
    }
  }

  navigateTo(route: string): void {
    this.router.navigate([route]);
  }
}
