import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { interval, Observable, startWith, switchMap } from 'rxjs';
import { Notifications } from '../models/notifications';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  // /notifications/** → Gateway → esphere-notification-service:8088
  private readonly apiUrl = `/notifications`;

  constructor(private http: HttpClient) { }

  getNotifications(): Observable<Notifications> {
    return interval(30000).pipe(
      startWith(0),
      switchMap(() => this.http.get<Notifications>(`${this.apiUrl}/mes-alertes`))
    );
  }

  markAsRead(type: string, id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${type}/${id}/read`, {});
  }
}