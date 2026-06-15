import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { PrimeNG } from 'primeng/config';
import { LanguageService } from './core/services/language.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  constructor(
    private primeng: PrimeNG,
    private languageService: LanguageService
  ) {}

  ngOnInit(): void {
    this.primeng.ripple.set(true);
    this.languageService.init();
  }

  title = 'adminlte-angular-app';
}
