import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { TranslateModule } from '@ngx-translate/core';
import { LangueService } from '../../../services/langue.service';

interface ConsultationBon {
  id: number;
  visiteId: string;
  prestataireNom: string;
  codeAdherent: string;
  codeAyantDroit: string | null;
  nomAdherent: string;
  nomAyantDroit: string | null;
  souscripteur: string;
  taux: number;
  typeConsultation: string;
  natureConsultation: string;
  montantValide: number;
  partZenithe: number;
  partAssure: number;
  date: string;
  etatConsultation: string;
}

@Component({
  selector: 'app-consultation-bon',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl: './consultation-bon.component.html',
  styleUrl: './consultation-bon.component.css'
})
export class ConsultationBonComponent implements OnInit {

  bon: ConsultationBon | null = null;
  isLoading = true;
  erreur = '';

  get codeCourt(): string {
    if (!this.bon?.visiteId) return '';
    const parts = this.bon.visiteId.split('_');
    return parts[parts.length - 1] || this.bon.visiteId;
  }

  get dateHeure(): string {
    if (!this.bon?.date) return '';
    return new Date(this.bon.date).toLocaleDateString('fr-FR', {
      day: '2-digit', month: '2-digit', year: 'numeric',
      hour: '2-digit', minute: '2-digit'
    });
  }

  // Si ayant droit → c'est le malade ; sinon c'est l'assuré lui-même
  get malade(): string {
    if (!this.bon) return '';
    return this.bon.nomAyantDroit || this.bon.nomAdherent;
  }

  get tauxLabel(): string {
    if (!this.bon?.taux) return '—';
    return this.bon.taux + '%';
  }

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private langueService: LangueService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.http.get<ConsultationBon>(`/validations/consultations/${id}`)
      .subscribe({
        next: (data) => {
          this.bon = data;
          this.isLoading = false;
          setTimeout(() => window.print(), 700);
        },
        error: (err) => {
          const status = err?.status;
          if (status === 401 || status === 403) {
            this.erreur = `Erreur ${status} : session expirée ou accès refusé. Reconnectez-vous.`;
          } else if (status === 404) {
            this.erreur = `Erreur 404 : consultation introuvable (id=${id}).`;
          } else if (status === 500) {
            this.erreur = `Erreur 500 : erreur serveur. Vérifiez que le service validation est démarré.`;
          } else if (status === 0) {
            this.erreur = `Impossible de joindre le serveur (service validation arrêté ?).`;
          } else {
            this.erreur = `Erreur ${status} : impossible de charger le bon.`;
          }
          this.isLoading = false;
        }
      });
  }

  formatMontant(val: number): string {
    if (!val && val !== 0) return '0';
    return val.toLocaleString('fr-FR');
  }

  imprimer(): void {
    window.print();
  }
}
