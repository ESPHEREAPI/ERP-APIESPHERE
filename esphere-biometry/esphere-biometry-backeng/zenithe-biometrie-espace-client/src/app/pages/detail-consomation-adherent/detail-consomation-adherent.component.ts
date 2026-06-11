import { Component, ElementRef, ViewChild } from '@angular/core';
import { DetailConsomationService } from '../../services/detail-consomation.service';
import { DetailConsomationAdherent } from '../../model/DetailConsomationAdherent';
import { Adherent } from '../../model/Adherent';
import { AdherentService } from '../../services/adherent.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-detail-consomation-adherent',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './detail-consomation-adherent.component.html',
  styleUrl: './detail-consomation-adherent.component.css'
})
export class DetailConsomationAdherentComponent {
codeAdherent: string = '';
  dateDebut: string = '';
  dateFin: string = '';
  
  consomations: DetailConsomationAdherent[] = [];
  filteredData: DetailConsomationAdherent[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';
  
  currentPage: number = 1;
  itemsPerPage: number = 10;
  totalItems: number = 0;
  souscripteur="CAMTEL"

 

 @ViewChild('searchInput') searchInput!: ElementRef;

  adherent: Adherent[] = [];
  selectedAdherent: Adherent | null = null;
  searchdherent: string = '';
  showDropdown: boolean = false;



  constructor(
    private consomationService: DetailConsomationService,private adherentService:AdherentService
   
  ) {}

  ngOnInit(): void {
    this.loadSouscripteurs();
  }

  loadSouscripteurs(): void {
    this.consomationService.getSouscripteurs(this.souscripteur).subscribe({
      next: (response: Adherent[]) => {
        this.adherent = response;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des souscripteurs:', error);
        this.errorMessage = 'Erreur lors du chargement des souscripteurs';
      }
    });
  }

  toggleDropdown(): void {
    this.showDropdown = !this.showDropdown;
    if (this.showDropdown) {
      setTimeout(() => {
        this.searchInput?.nativeElement?.focus();
      }, 100);
    }
  }

  getFilteredSouscripteurs(): Adherent[] {
    return this.adherent.filter(s =>
      s.assurePrincipal.toLowerCase().includes(this.searchdherent.toLowerCase()) 
    );
  }

  selectSouscripteur(souscripteur: Adherent): void {
    this.selectedAdherent = souscripteur;
    this.searchdherent = '';
    this.showDropdown = false;
    this.clearError();
  }

  handleSearch(): void {
    if (!this.selectedAdherent) {
      this.errorMessage = 'Veuillez sélectionner un souscripteur';
      return;
    }

    if (!this.dateDebut || !this.dateFin) {
      this.errorMessage = 'Veuillez sélectionner une période complète';
      return;
    }

    if (new Date(this.dateDebut) > new Date(this.dateFin)) {
      this.errorMessage = 'La date de début doit être antérieure à la date de fin';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.currentPage = 1;

    // Conversion des dates au format attendu par le backend
    const dateDebut = new Date(this.dateDebut);
    const dateFin = new Date(this.dateFin);

    this.consomationService.getDetailConsomationAdherent(
      this.selectedAdherent.codeAdherent?? "",
      dateDebut,
      dateFin
    ).subscribe({
      next: (response: DetailConsomationAdherent[]) => {
        this.consomations = response;
        this.filteredData = response;
        this.totalItems = response.length;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erreur lors de la recherche:', error);
        this.errorMessage = 'Erreur lors de la récupération des données';
        this.isLoading = false;
      }
    });
  }

  handleReset(): void {
    this.selectedAdherent = null;
    this.searchdherent = '';
    this.dateDebut = '';
    this.dateFin = '';
    this.consomations = [];
    this.filteredData = [];
    this.errorMessage = '';
    this.currentPage = 1;
    this.showDropdown = false;
  }

  clearError(): void {
    this.errorMessage = '';
  }

  getEtatBadge(etat: string): string {
    const styles: { [key: string]: string } = {
      'Validé': 'bg-green-100 text-green-800 border-green-300',
      'En attente': 'bg-yellow-100 text-yellow-800 border-yellow-300',
      'Rejeté': 'bg-red-100 text-red-800 border-red-300'
    };
    return styles[etat] || 'bg-gray-100 text-gray-800';
  }

  calculateTotals() {
    return {
      totalMontant: this.filteredData.reduce((sum, item) => sum + (item.montant?? 0), 0),
      totalPriseEnCharge: this.filteredData.reduce((sum, item) => sum + (item.montantPriseEnCharge?? 0), 0),
      totalAyantDroit: this.filteredData.reduce((sum, item) => sum + (item.montantAyantDroit?? 0), 0)
    };
  }

  getPaginatedData(): DetailConsomationAdherent[] {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    return this.filteredData.slice(startIndex, startIndex + this.itemsPerPage);
  }

  getTotalPages(): number {
    return Math.ceil(this.totalItems / this.itemsPerPage);
  }

  nextPage(): void {
    if (this.currentPage < this.getTotalPages()) {
      this.currentPage++;
    }
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  exportToExcel(): void {
    // Implémentation de l'export en Excel
    // Utiliser une librairie comme xlsx
    console.log('Export en cours...');
  }

  formatCurrency(value: any): string {
    const numValue = value?.toNumber?.() || value || 0;
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XAF'
    }).format(numValue);
  }

  formatDate(date: Date | string): string {
    return new Intl.DateTimeFormat('fr-FR').format(new Date(date));
  }
}
