import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface MenuItem {
  id: number;
  pereId: number;
  nomModule: string;
  nomAction: string;
  classImage: string;
  numeroOrdre: number;
  type: string;
}

export interface MenuTree {
  menu: MenuItem;
  children: MenuTree[];
}

@Injectable({ providedIn: 'root' })
export class AdminPermissionService {

  constructor(private http: HttpClient) {}

  getAllMenus(): Observable<MenuItem[]> {
    return this.http.get<MenuItem[]>('/auth/permissions/menus');
  }

  getPermissionsByProfil(profilId: number): Observable<number[]> {
    return this.http.get<number[]>(`/auth/permissions/profil/${profilId}`);
  }

  savePermissions(profilId: number, menuIds: number[]): Observable<any> {
    return this.http.put(`/auth/permissions/profil/${profilId}`, menuIds);
  }

  buildTree(menus: MenuItem[]): MenuTree[] {
    const map = new Map<number, MenuTree>();
    const roots: MenuTree[] = [];

    menus.forEach(m => map.set(m.id, { menu: m, children: [] }));

    menus.forEach(m => {
      const node = map.get(m.id)!;
      if (m.pereId && m.pereId !== 0 && map.has(m.pereId)) {
        map.get(m.pereId)!.children.push(node);
      } else {
        roots.push(node);
      }
    });

    return roots;
  }
}
