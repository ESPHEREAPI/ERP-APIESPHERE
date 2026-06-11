import { Component, OnInit } from '@angular/core';
import { Adherent } from '../../model/Adherent';
import { ActivatedRoute, Router } from '@angular/router';
import { AdherentService } from '../../services/adherent.service';
import Swal from 'sweetalert2';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


@Component({
  selector: 'app-adherent-profile',
  standalone: true,
  imports: [CommonModule,FormsModule,
      ReactiveFormsModule],
  templateUrl: './adherent-profile.component.html',
  styleUrl: './adherent-profile.component.css'
})
export class AdherentProfileComponent implements OnInit {
 adherent: Adherent | null = null;
  codeAdherent: string = '';
  loading = false;
  
  // Onglets actifs
  activeTab = 'identity';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private adherentService: AdherentService
  ) {}

  ngOnInit(): void {
     this.route.params.subscribe(params => {
    this.codeAdherent = params['id'];
    console.log("Code adhérent :", this.codeAdherent);
    this.loadAdherent();
    });
  }
  

  /**
   * Charge les données de l'adhérent
   */
  loadAdherent(): void {
    this.loading = true;
    this.adherentService.getAdherentProfile(this.codeAdherent).subscribe({
      next: (adherent) => {
        this.adherent = adherent;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement du profil', error);
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Impossible de charger le profil de l\'adhérent'
        });
        this.loading = false;
        this.router.navigate(['/adherents']);
      }
    });
  }

  /**
   * Modifier l'adhérent
   */
  editAdherent(): void {
    this.router.navigate(['/adherents', this.codeAdherent, 'edit']);
  }

  /**
   * Retour à la liste
   */
  goBack(): void {
    this.router.navigate(['/adherents/adherents-list']);
  }

  /**
   * Change l'onglet actif
   */
  changeTab(tab: string): void {
    this.activeTab = tab;
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
   * Retourne la classe de tendance
   */
  getTendanceClass(tendance: string): string {
    switch (tendance) {
      case 'HAUSSE':
        return 'text-danger';
      case 'BAISSE':
        return 'text-success';
      case 'STABLE':
        return 'text-info';
      default:
        return 'text-muted';
    }
  }

  /**
   * Retourne l'icône de tendance
   */
  getTendanceIcon(tendance: string): string {
    switch (tendance) {
      case 'HAUSSE':
        return 'fas fa-arrow-up';
      case 'BAISSE':
        return 'fas fa-arrow-down';
      case 'STABLE':
        return 'fas fa-minus';
      default:
        return 'fas fa-minus';
    }
  }

  /**
   * Retourne la classe de statut du plafond
   */
  getPlafondStatutClass(statut: string): string {
    switch (statut) {
      case 'DISPONIBLE':
        return 'bg-success';
      case 'ATTENTION':
        return 'bg-warning';
      case 'EPUISE':
        return 'bg-danger';
      default:
        return 'bg-secondary';
    }
  }

  /**
   * Formater un montant
   */
  formatMontant(montant: number): string {
    return new Intl.NumberFormat('fr-FR', {
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(montant);
  }

  /**
   * Imprimer la carte
   */
  printCard(): void {
    Swal.fire({
      icon: 'info',
      title: 'Impression de la carte',
      text: 'Fonctionnalité en cours de développement'
    });
  }

  /**
   * Envoyer un email
   */
  sendEmail(): void {
    if (this.adherent?.email) {
      window.location.href = `mailto:${this.adherent.email}`;
    }
  }

  /**
   * Appeler le téléphone
   */
  callPhone(): void {
    if (this.adherent?.telephone) {
      window.location.href = `tel:${this.adherent.telephone}`;
    }
  }

  /**
   * Suspend l'adhérent
   */
  suspendAdherent(): void {
    if (!this.adherent) return;

    Swal.fire({
      title: 'Suspendre l\'adhérent ?',
      text: `Voulez-vous vraiment suspendre ${this.adherent.assurePrincipal} ?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#ffc107',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Oui, suspendre',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        this.adherentService.changeStatut(this.codeAdherent, 'SUSPENDU').subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Succès',
              text: 'Adhérent suspendu avec succès',
              timer: 3000,
              showConfirmButton: false
            });
            this.loadAdherent();
          },
          error: (error) => {
            console.error('Erreur lors de la suspension', error);
            Swal.fire({
              icon: 'error',
              title: 'Erreur',
              text: 'Erreur lors de la suspension de l\'adhérent'
            });
          }
        });
      }
    });
  }

  /**
   * Active l'adhérent
   */
  activateAdherent(): void {
    if (!this.adherent) return;

    this.adherentService.changeStatut(this.codeAdherent, 'ACTIF').subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: 'Adhérent activé avec succès',
          timer: 3000,
          showConfirmButton: false
        });
        this.loadAdherent();
      },
      error: (error) => {
        console.error('Erreur lors de l\'activation', error);
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Erreur lors de l\'activation de l\'adhérent'
        });
      }
    });
  }
}
