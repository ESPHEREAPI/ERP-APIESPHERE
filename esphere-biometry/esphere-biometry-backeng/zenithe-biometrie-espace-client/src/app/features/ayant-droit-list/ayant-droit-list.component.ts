import { Component } from '@angular/core';
import { AyantDroit } from '../../model/AyantDroit';
import { AyantDroitFilter, LIEN_PARENTE_OPTIONS } from '../../model/AyantDroitFilter';
import { AyantDroitService } from '../../services/ayant-droit.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PageResponse } from '../../model/PageResponse';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-ayant-droit-list',
  standalone: true,
  imports: [],
  templateUrl: './ayant-droit-list.component.html',
  styleUrl: './ayant-droit-list.component.css'
})
export class AyantDroitListComponent {
ayantsDroit: AyantDroit[] = [];
  totalElements = 0;
  totalPages = 0;
  currentPage = 0;
  pageSize = 20;
  loading = false;

  // Filtre depuis l'adhérent parent (optionnel)
  codeAdherentFilter: string | null = null;

  // Filtres
  filter: AyantDroitFilter = {
    page: 0,
    size: 20,
    sortBy: 'nom',
    sortDirection: 'ASC'
  };

  // Options de filtrage
  statutOptions = ['ACTIF', 'SUSPENDU', 'RESILIE'];
  sexeOptions = ['M', 'F'];
  lienParenteOptions = LIEN_PARENTE_OPTIONS;

  constructor(
    private ayantDroitService: AyantDroitService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Vérifier si on filtre par adhérent
    this.route.queryParams.subscribe(params => {
      if (params['adherent']) {
        this.codeAdherentFilter = params['adherent'];
        this.filter.codeAdherent = this.codeAdherentFilter ?? "NON DEFINIR";
      }
    });

    this.loadAyantsDroit();
  }

  /**
   * Charge la liste des ayants droit
   */
  loadAyantsDroit(): void {
    this.loading = true;
    this.ayantDroitService.searchAyantsDroit(this.filter).subscribe({
      next: (response: PageResponse<AyantDroit>) => {
        this.ayantsDroit = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.currentPage = response.number;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement', error);
        this.showError('Erreur lors du chargement des ayants droit');
        this.loading = false;
      }
    });
  }

  /**
   * Recherche avec filtres
   */
  onSearch(): void {
    this.filter.page = 0;
    this.loadAyantsDroit();
  }

  /**
   * Réinitialise les filtres
   */
  resetFilters(): void {
    this.filter = {
      page: 0,
      size: 20,
      sortBy: 'nom',
      sortDirection: 'ASC',
      codeAdherent: this.codeAdherentFilter || undefined
    };
    this.loadAyantsDroit();
  }

  /**
   * Change de page
   */
  onPageChange(page: number): void {
    this.filter.page = page;
    this.loadAyantsDroit();
  }

  /**
   * Change la taille de page
   */
  onPageSizeChange(size: number): void {
    this.filter.size = size;
    this.filter.page = 0;
    this.loadAyantsDroit();
  }

  /**
   * Tri des colonnes
   */
  onSort(column: string): void {
    if (this.filter.sortBy === column) {
      this.filter.sortDirection = this.filter.sortDirection === 'ASC' ? 'DESC' : 'ASC';
    } else {
      this.filter.sortBy = column;
      this.filter.sortDirection = 'ASC';
    }
    this.loadAyantsDroit();
  }

  /**
   * Voir le profil
   */
  viewProfile(codeAyantDroit: string): void {
    this.router.navigate(['/ayants-droit', codeAyantDroit]);
  }

  /**
   * Modifier
   */
  editAyantDroit(codeAyantDroit: string): void {
    this.router.navigate(['/ayants-droit', codeAyantDroit, 'edit']);
  }

  /**
   * Créer un nouvel ayant droit
   */
  createAyantDroit(): void {
    if (this.codeAdherentFilter) {
      this.router.navigate(['/ayants-droit', 'create'], {
        queryParams: { adherent: this.codeAdherentFilter }
      });
    } else {
      this.router.navigate(['/ayants-droit', 'create']);
    }
  }

  /**
   * Suspendre
   */
  suspendAyantDroit(ayantDroit: AyantDroit): void {
    Swal.fire({
      title: 'Suspendre l\'ayant droit ?',
      text: `Voulez-vous vraiment suspendre ${ayantDroit.nom} ?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#ffc107',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Oui, suspendre',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        this.ayantDroitService.changeStatut(ayantDroit.codeAyantDroit!, 'SUSPENDU').subscribe({
          next: () => {
            this.showSuccess('Ayant droit suspendu avec succès');
            this.loadAyantsDroit();
          },
          error: (error) => {
            console.error('Erreur', error);
            this.showError('Erreur lors de la suspension');
          }
        });
      }
    });
  }

  /**
   * Activer
   */
  activateAyantDroit(ayantDroit: AyantDroit): void {
    this.ayantDroitService.changeStatut(ayantDroit.codeAyantDroit!, 'ACTIF').subscribe({
      next: () => {
        this.showSuccess('Ayant droit activé avec succès');
        this.loadAyantsDroit();
      },
      error: (error) => {
        console.error('Erreur', error);
        this.showError('Erreur lors de l\'activation');
      }
    });
  }

  /**
   * Supprimer
   */
  deleteAyantDroit(ayantDroit: AyantDroit): void {
    Swal.fire({
      title: 'Supprimer l\'ayant droit ?',
      text: `Cette action supprimera définitivement ${ayantDroit.nom}`,
      icon: 'error',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        this.ayantDroitService.deleteAyantDroit(ayantDroit.codeAyantDroit!).subscribe({
          next: () => {
            this.showSuccess('Ayant droit supprimé avec succès');
            this.loadAyantsDroit();
          },
          error: (error) => {
            console.error('Erreur', error);
            this.showError('Erreur lors de la suppression');
          }
        });
      }
    });
  }

  /**
   * Calcule l'âge
   */
  calculateAge(naissance: Date): number {
    const birthDate = new Date(naissance);
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }
    return age;
  }

  /**
   * Retourne la classe badge selon le statut
   */
  getStatutBadgeClass(statut: string): string {
    switch (statut) {
      case 'ACTIF':
        return 'badge-success';
      case 'SUSPENDU':
        return 'badge-warning';
      case 'RESILIE':
        return 'badge-danger';
      default:
        return 'badge-secondary';
    }
  }

  /**
   * Affiche un message de succès
   */
  private showSuccess(message: string): void {
    Swal.fire({
      icon: 'success',
      title: 'Succès',
      text: message,
      timer: 3000,
      showConfirmButton: false
    });
  }

  /**
   * Affiche un message d'erreur
   */
  private showError(message: string): void {
    Swal.fire({
      icon: 'error',
      title: 'Erreur',
      text: message
    });
  }
}
