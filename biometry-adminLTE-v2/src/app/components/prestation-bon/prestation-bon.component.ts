import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { TranslateModule } from '@ngx-translate/core';
import { LangueService } from '../../services/langue.service';
import { AuthService } from '../../auth/auth.service';

interface LigneBon {
  nom: string;
  typeExamen: string;
  taux: number;
  montantValide: number;
  quantite: number;
  total: number;
}

interface PrestationBon {
  id: number;
  visiteId: string;
  codeCourt: string;
  naturePrestation: string;
  date: string;
  prestataireNom: string;
  nomAdherent: string;
  nomAyantDroit: string | null;
  malade: string;
  souscripteur: string;
  lignes: LigneBon[];
  montantTotal: number;
  partZenithe: number;
  partAssure: number;
}

@Component({
  selector: 'app-prestation-bon',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl: './prestation-bon.component.html',
  styleUrl: './prestation-bon.component.css'
})
export class PrestationBonComponent implements OnInit {

  bon: PrestationBon | null = null;
  isLoading = true;
  erreur = '';

  get titre(): string {
    const n = this.bon?.naturePrestation?.toLowerCase() || '';
    if (n === 'ordonnance') return 'bon_titre_ordonnance';
    if (n === 'examen')     return 'bon_titre_examen';
    return 'bon_titre_ordonnance';
  }

  get listeTitre(): string {
    const n = this.bon?.naturePrestation?.toLowerCase() || '';
    return n === 'ordonnance' ? 'bon_liste_medicaments' : 'bon_liste_examens';
  }

  get isOrdonnance(): boolean {
    return this.bon?.naturePrestation?.toLowerCase() === 'ordonnance';
  }

  get dateHeure(): string {
    if (!this.bon?.date) return '';
    return new Date(this.bon.date).toLocaleDateString('fr-FR', {
      day: '2-digit', month: '2-digit', year: 'numeric',
      hour: '2-digit', minute: '2-digit'
    });
  }

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private langueService: LangueService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Seuls les prestataires peuvent imprimer les bons
    const profil = this.authService.getStoredUser()?.profilCode || '';
    if (['SERVICE_SANTE', 'SUP_ADMIN'].includes(profil)) {
      this.router.navigate(['/public/admin/ordonnance']);
      return;
    }

    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.http.get<PrestationBon>(`/validations/prestations/${id}/bon`).subscribe({
      next: (data) => {
        this.bon = data;
        this.isLoading = false;
        setTimeout(() => window.print(), 700);
      },
      error: (err) => {
        const status = err?.status;
        if (status === 401 || status === 403) {
          this.erreur = `Erreur ${status} : session expirée. Reconnectez-vous.`;
        } else if (status === 404) {
          this.erreur = `Erreur 404 : prestation introuvable (id=${id}).`;
        } else if (status === 500) {
          this.erreur = `Erreur 500 : erreur serveur. Vérifiez que le service est démarré.`;
        } else if (status === 0) {
          this.erreur = `Impossible de joindre le serveur.`;
        } else {
          this.erreur = `Erreur ${status} : impossible de charger le bon.`;
        }
        this.isLoading = false;
      }
    });
  }

  fmt(val: number): string {
    if (!val && val !== 0) return '0';
    return val.toLocaleString('fr-FR');
  }

  imprimer(): void {
    window.print();
  }
}
