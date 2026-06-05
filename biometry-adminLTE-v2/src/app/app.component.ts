import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LangueService } from './services/langue.service';
import { SessionManagerService } from './services/session-manager-service.';
import { NetworkService } from './services/network.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})

export class AppComponent implements OnInit{
  title = 'biometry-adminLTE-v2';
   constructor(private languageService: LangueService,sessionManagerService:SessionManagerService,private networkService: NetworkService ){

  }

    ngOnInit(): void {
    // Optionnel : Observer les changements de connexion
    this.networkService.isOnline$.subscribe(isOnline => {
      if (!isOnline) {
        console.warn('⚠️ Connexion Internet perdue');
      } else {
        console.log('✅ Connexion Internet rétablie');
      }
    });
  }
}

