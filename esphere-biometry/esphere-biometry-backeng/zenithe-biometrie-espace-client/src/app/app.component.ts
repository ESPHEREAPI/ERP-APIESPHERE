import { Component, OnInit } from '@angular/core';


import { PrimeNG } from 'primeng/config';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  constructor(private primeng: PrimeNG) { }
  ngOnInit(): void {
    this.primeng.ripple.set(true);
  }
  title = 'Biometry-Backend';
}
