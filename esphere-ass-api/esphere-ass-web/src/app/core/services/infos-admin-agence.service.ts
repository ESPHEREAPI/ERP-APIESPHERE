import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  InfosAdminAgence,
  ApiResponse,
  PageResponse
} from '../model/infos-admin-agence.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class InfosAdminAgenceService {

 // private readonly BASE_URL = 'http://localhost:8080/api/v1/admin-agences';
   //private readonly BASE_URL = '${environment.apiUrl}/gateway-proxy/api/esphere-ass-microservice-admin/admin-agences';
   private readonly BASE_URL = `${environment.apiUrl}/gateway-proxy/api/esphere-ass-microservice-admin/admin-agences`;

  constructor(private http: HttpClient) {}

  getAll(
    search = '',
    page = 0,
    size = 10,
    sortBy = 'id',
    direction = 'desc'
  ): Observable<ApiResponse<PageResponse<InfosAdminAgence>>> {
    const params = new HttpParams()
      .set('search', search)
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy)
      .set('direction', direction);
    return this.http.get<ApiResponse<PageResponse<InfosAdminAgence>>>(
      this.BASE_URL, { params }
    );
  }

  getById(id: number): Observable<ApiResponse<InfosAdminAgence>> {
    return this.http.get<ApiResponse<InfosAdminAgence>>(`${this.BASE_URL}/${id}`);
  }

  create(dto: InfosAdminAgence): Observable<ApiResponse<InfosAdminAgence>> {
    return this.http.post<ApiResponse<InfosAdminAgence>>(this.BASE_URL, dto);
  }

  update(id: number, dto: InfosAdminAgence): Observable<ApiResponse<InfosAdminAgence>> {
    return this.http.put<ApiResponse<InfosAdminAgence>>(`${this.BASE_URL}/${id}`, dto);
  }

  delete(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.BASE_URL}/${id}`);
  }

  getByUsername(username: string): Observable<ApiResponse<InfosAdminAgence>> {
    return this.http.get<ApiResponse<InfosAdminAgence>>(
      `${this.BASE_URL}/by-username/${encodeURIComponent(username)}`
    );
  }
}
