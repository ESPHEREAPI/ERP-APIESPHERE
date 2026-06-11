import { Component, OnInit } from '@angular/core';
import { Adherent } from '../../model/Adherent';
import { AdherentFilter } from '../../model/AdherentFilter';
import { AdherentService } from '../../services/adherent.service';
import { Router } from '@angular/router';
import { PageResponse } from '../../model/PageResponse';
import Swal from 'sweetalert2';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-adherent-list',
  standalone: true,
  imports: [CommonModule,FormsModule,
      ReactiveFormsModule],
  templateUrl: './adherent-list.component.html',
  styleUrl: './adherent-list.component.css'
})
export class AdherentListComponent implements OnInit{
adherents: Adherent[] = [];
  totalElements = 0;
  totalPages = 0;
  currentPage = 0;
  pageSize = 20;
  loading = false;

  // Filtres
  filter: AdherentFilter = {
    page: 0,
    size: 20,
    sortBy: 'assurePrincipal',
    sortDirection: 'ASC'
  };

  // Options de filtrage
  statutOptions = ['ACTIF', 'SUSPENDU', 'RESILIE'];
  sexeOptions = ['M', 'F'];
  enroleOptions = [
    { value: 'O', label: 'Enrôlé' },
    { value: 'N', label: 'Non enrôlé' }
  ];

  constructor(
    private adherentService: AdherentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadAdherents();
  }

  /**
   * Charge la liste des adhérents
   */
  loadAdherents(): void {
    this.loading = true;
    this.adherentService.searchAdherents(this.filter).subscribe({
      next: (response: PageResponse<Adherent>) => {
        this.adherents = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.currentPage = response.number;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des adhérents', error);
        this.showError('Erreur lors du chargement des adhérents');
        this.loading = false;
      }
    });
  }

  /**
   * Recherche avec filtres
   */
  onSearch(): void {
    this.filter.page = 0;
    this.loadAdherents();
  }

  /**
   * Réinitialise les filtres
   */
  resetFilters(): void {
    this.filter = {
      page: 0,
      size: 20,
      sortBy: 'assurePrincipal',
      sortDirection: 'ASC'
    };
    this.loadAdherents();
  }

  /**
   * Change de page
   */
  onPageChange(page: number): void {
    this.filter.page = page;
    this.loadAdherents();
  }

  /**
   * Change la taille de page
   */
  onPageSizeChange(size: number): void {
    this.filter.size = size;
    this.filter.page = 0;
    this.loadAdherents();
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
    this.loadAdherents();
  }

  /**
   * Voir le profil d'un adhérent
   */
  viewProfile(codeAdherent: string): void {
    console.log("code adherent",codeAdherent)
    this.router.navigate(['/adherents', codeAdherent]);
  }

  /**
   * Modifier un adhérent
   */
  editAdherent(codeAdherent: string): void {
    this.router.navigate(['/adherents', codeAdherent, 'edit']);
  }

  /**
   * Créer un nouvel adhérent
   */
  createAdherent(): void {
    this.router.navigate(['/adherents', 'create']);
  }

  /**
   * Suspendre un adhérent
   */
  suspendAdherent(adherent: Adherent): void {
    Swal.fire({
      title: 'Suspendre l\'adhérent ?',
      text: `Voulez-vous vraiment suspendre ${adherent.assurePrincipal} ?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#ffc107',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Oui, suspendre',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        this.adherentService.changeStatut(adherent.codeAdherent!, 'SUSPENDU').subscribe({
          next: () => {
            this.showSuccess('Adhérent suspendu avec succès');
            this.loadAdherents();
          },
          error: (error) => {
            console.error('Erreur lors de la suspension', error);
            this.showError('Erreur lors de la suspension de l\'adhérent');
          }
        });
      }
    });
  }

  /**
   * Activer un adhérent
   */
  activateAdherent(adherent: Adherent): void {
    this.adherentService.changeStatut(adherent.codeAdherent!, 'ACTIF').subscribe({
      next: () => {
        this.showSuccess('Adhérent activé avec succès');
        this.loadAdherents();
      },
      error: (error) => {
        console.error('Erreur lors de l\'activation', error);
        this.showError('Erreur lors de l\'activation de l\'adhérent');
      }
    });
  }

  /**
   * Supprimer un adhérent
   */
  deleteAdherent(adherent: Adherent): void {
    Swal.fire({
      title: 'Supprimer l\'adhérent ?',
      text: `Cette action supprimera définitivement ${adherent.assurePrincipal}`,
      icon: 'error',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        this.adherentService.deleteAdherent(adherent.codeAdherent!).subscribe({
          next: () => {
            this.showSuccess('Adhérent supprimé avec succès');
            this.loadAdherents();
          },
          error: (error) => {
            console.error('Erreur lors de la suppression', error);
            this.showError('Erreur lors de la suppression de l\'adhérent');
          }
        });
      }
    });
  }

  /**
   * Exporter en Excel
   */
  exportExcel(): void {
    this.adherentService.exportToExcel(this.filter).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `adherents_${new Date().getTime()}.xlsx`;
        link.click();
        window.URL.revokeObjectURL(url);
      },
      error: (error) => {
        console.error('Erreur lors de l\'export', error);
        this.showError('Erreur lors de l\'export Excel');
      }
    });
  }

  /**
   * Exporter en PDF
   */
  exportPdf(): void {
    this.adherentService.exportToPdf(this.filter).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `adherents_${new Date().getTime()}.pdf`;
        link.click();
        window.URL.revokeObjectURL(url);
      },
      error: (error) => {
        console.error('Erreur lors de l\'export', error);
        this.showError('Erreur lors de l\'export PDF');
      }
    });
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
   * Calcule l'âge à partir de la date de naissance
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
