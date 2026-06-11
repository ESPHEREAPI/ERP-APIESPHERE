import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { delay, map, Observable, of } from 'rxjs';
import { environment } from '../../../environments/environment.prod';
import { Subscriber } from '../models/Subscriber';
import { PolicyCheck } from '../models/PolicyCheck';
import { PaginatedResponse } from '../models/PaginatedResponse';
import { SubscriberFilter } from '../models/SubscriberFilter';
import { ActivationLinkRequest } from '../models/ActivationLinkRequest';
import { ActivationResponse } from '../models/ActivationReponse';

@Injectable({
  providedIn: 'root'
})
export class SouscripteurService {

  private readonly apiUrl = `/subscriber`;
 //private readonly policyApiUrl = `${environment.apiUrl}/policies`;

  // Mock data for demonstration
  private mockSubscribers: Subscriber[] = [
    {
      id: 1,
      policyNumber: 'POL-2024-001',
      fullName: 'Jean-Baptiste Mboumba',
      phoneNumber: '+237 690 123 456',
      email: 'jb.mboumba@email.com',
      username: 'jb.mboumba',
      active: true,
      passwordMode: 'manual',
      createdAt: new Date('2024-01-15'),
    },
    {
      id: 2,
      policyNumber: 'POL-2024-002',
      fullName: 'Marie-Claire Essomba',
      phoneNumber: '+237 677 456 789',
      email: 'mc.essomba@email.com',
      username: 'mc.essomba',
      active: false,
      passwordMode: 'activation_link',
      activationSentAt: new Date('2024-02-01'),
      activationExpiry: new Date('2024-02-03'),
      activationDuration: 48,
      createdAt: new Date('2024-02-01'),
    },
    {
      id: 3,
      policyNumber: 'POL-2024-003',
      fullName: 'Paul Nkolo Bekono',
      phoneNumber: '+237 655 789 012',
      email: 'p.nkolo@email.com',
      username: 'p.nkolo',
      active: true,
      passwordMode: 'manual',
      createdAt: new Date('2024-03-10'),
    },
  ];

  private mockPolicies: Record<string, string> = {
    'POL-2024-001': 'Jean-Baptiste Mboumba',
    'POL-2024-002': 'Marie-Claire Essomba',
    'POL-2024-003': 'Paul Nkolo Bekono',
    'POL-2024-004': 'Alain Rodrigue Talla',
    'POL-2024-005': 'Sandrine Fouda Ngo',
    'POL-2025-001': 'Emmanuel Ndongo',
    'POL-2025-002': 'Christiane Abena Mfou',
  };

  constructor(private http: HttpClient) {}

  /**
   * Vérifie l'existence d'un numéro de police et retourne le nom du souscripteur
   */
  checkPolicy(policyNumber: string): Observable<PolicyCheck> {
    // In production: 
    return this.http.get<PolicyCheck>(`${this.apiUrl}/policies/check/${policyNumber}`);
    /**return of(null).pipe(
      delay(800),
      map(() => {
        const fullName = this.mockPolicies[policyNumber.toUpperCase()];
        if (fullName) {
          return { policyNumber, fullName, exists: true };
        }
        return { policyNumber, fullName: '', exists: false };
      })
    );**/
  }

  /**
   * Récupère tous les souscripteurs avec pagination et filtres
   */
  getAll(filter: SubscriberFilter = {}): Observable<PaginatedResponse<Subscriber>> {
    // In production:
     let params = new HttpParams();
     if (filter.search) params = params.set('search', filter.search);
     if (filter.active !== undefined) params = params.set('isActive', filter.active.toString());
    params = params.set('page', (filter.page || 1).toString());
     params = params.set('limit', (filter.limit || 10).toString());
    return this.http.get<PaginatedResponse<Subscriber>>(`${this.apiUrl}/all`, { params });

    //return of(null).pipe(
      //delay(500),
     // map(() => {
       // let data = [...this.mockSubscribers];

       // if (filter.search) {
         // const s = filter.search.toLowerCase();
          //data = data.filter(
           // (sub) =>
            //  sub.fullName.toLowerCase().includes(s) ||
            // sub.email.toLowerCase().includes(s) ||
             // sub.policyNumber.toLowerCase().includes(s) ||
            //  sub.username.toLowerCase().includes(s)
          //);
        //}

        //if (filter.isActive !== undefined) {
         // data = data.filter((sub) => sub.isActive === filter.isActive);
       // }

        //const page = filter.page || 1;
        //const limit = filter.limit || 10;
       // const total = data.length;
       // const start = (page - 1) * limit;
        //data = data.slice(start, start + limit);

       // return { data, total, page, limit };
    //  })
   // );
  }

  /**
   * Récupère un souscripteur par son ID
   */
  getById(id: number): Observable<Subscriber> {
    // In production:
     return this.http.get<Subscriber>(`${this.apiUrl}/${id}`);
   /** return of(null).pipe(
      delay(400),
      map(() => {
        const sub = this.mockSubscribers.find((s) => s.id === id);
        if (!sub) throw new Error(`Subscriber ${id} not found`);
        return { ...sub };
      })
    );*/
  }

  /**
   * Crée un nouveau souscripteur
   */
  create(subscriber: Omit<Subscriber, 'id' | 'createdAt' | 'updatedAt'>): Observable<Subscriber> {
    // In production: 
    return this.http.post<Subscriber>(`${this.apiUrl}/create`, subscriber);
    /**return of(null).pipe(
      delay(1000),
      map(() => {
        const newSub: Subscriber = {
          ...subscriber,
          id: Math.max(...this.mockSubscribers.map((s) => s.id!), 0) + 1,
          createdAt: new Date(),
          updatedAt: new Date(),
        };
        this.mockSubscribers.push(newSub);
        return { ...newSub };
      })
    );**/
  }

  /**
   * Met à jour un souscripteur existant
   */
  update(id: number, subscriber: Partial<Subscriber>): Observable<Subscriber> {
    // In production:
     return this.http.put<Subscriber>(`${this.apiUrl}/${id}`, subscriber);
   /**  return of(null).pipe(
      delay(800),
      map(() => {
        const idx = this.mockSubscribers.findIndex((s) => s.id === id);
        if (idx === -1) throw new Error(`Subscriber ${id} not found`);
        this.mockSubscribers[idx] = {
          ...this.mockSubscribers[idx],
          ...subscriber,
          id,
          updatedAt: new Date(),
        };
        return { ...this.mockSubscribers[idx] };
      })
    );*/
  }

  /**
   * Supprime un souscripteur
   */
  delete(id: number): Observable<void> {
    // In production: return this.http.delete<void>(`${this.apiUrl}/${id}`);
    return of(null).pipe(
      delay(600),
      map(() => {
        const idx = this.mockSubscribers.findIndex((s) => s.id === id);
        if (idx === -1) throw new Error(`Subscriber ${id} not found`);
        this.mockSubscribers.splice(idx, 1);
      })
    );
  }

  /**
   * Active ou désactive un souscripteur
   */
  toggleStatus(id: number, active: boolean): Observable<Subscriber> {

    return this.update(id, { active });
  }

  /**
   * Envoie un lien d'activation par email
   */
  sendActivationLink(request: ActivationLinkRequest): Observable<ActivationResponse> {
    // In production: 
    return this.http.post<ActivationResponse>(`${this.apiUrl}/${request.subscriberId}/send-activation`, request);
   /**  return of(null).pipe(
      delay(1200),
      map(() => {
        const expiry = new Date();
        expiry.setHours(expiry.getHours() + request.duration);

        const idx = this.mockSubscribers.findIndex((s) => s.id === request.subscriberId);
        if (idx !== -1) {
          this.mockSubscribers[idx].activationSentAt = new Date();
          this.mockSubscribers[idx].activationExpiry = expiry;
          this.mockSubscribers[idx].activationDuration = request.duration;
          this.mockSubscribers[idx].activationToken = `TOKEN-${Date.now()}`;
        }

        return {
          message: `Lien d'activation envoyé à ${request.email}`,
          expiry,
        };
      })
    );**/
  }

  /**
   * Réinitialise le mot de passe manuellement
   */
  resetPassword(id: number, newPassword: string): Observable<{ message: string }> {
    // In production: 
    return this.http.post< { message: string }>(`${this.apiUrl}/${id}/reset-password`, { newPassword });
    /**return of(null).pipe(
      delay(800),
      map(() => ({ message: 'Mot de passe réinitialisé avec succès' }))
    );*/
  }

 resetPasswordByEmail(email: string, newPassword: string): Observable<{ message: string }> {
    // In production: 
    return this.http.post< { message: string }>(`${this.apiUrl}/email/${email}/reset-password`, { newPassword });
    /**return of(null).pipe(
      delay(800),
      map(() => ({ message: 'Mot de passe réinitialisé avec succès' }))
    );*/
  }
  /**
   * Vérifie si un nom d'utilisateur est disponible
   */
  checkUsernameAvailability(username: string, excludeId?: number): Observable<boolean> {
    return of(null).pipe(
      delay(500),
      map(() => {
        return !this.mockSubscribers.some(
          (s) => s.username === username && s.id !== excludeId
        );
      })
    );
  }


}
