import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class SessionManagerService{
  private sessionTimeout: number = 60000; //15 // 30 minutes 1800000
  private lastActivity: number = Date.now();
  constructor(private router: Router) {
    this.startSessionTimer();
  }

  private startSessionTimer() {
    setInterval(() => {
      if (Date.now() - this.lastActivity > this.sessionTimeout) {
        this.logout();
      }
    }, 360000); // Check session   pour 1h
  }

  public registerActivity() {
    this.lastActivity = Date.now();
  }

  public logout() {
    // Perform logout logic here, such as:
    // - Clear authentication token
    // - Remove user data from local storage
    // - Redirect to login page
    this.router.navigate(['/login']);
  }
}
