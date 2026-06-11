import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AdherentService } from '../../services/adherent.service';
import { Adherent } from '../../model/Adherent';
import Swal from 'sweetalert2';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-adherent-form',
  standalone: true,
  imports: [CommonModule,FormsModule,
      ReactiveFormsModule],
  templateUrl: './adherent-form.component.html',
  styleUrl: './adherent-form.component.css'
})
export class AdherentFormComponent {
adherentForm!: FormGroup;
  isEditMode = false;
  codeAdherent: string = '';
  loading = false;
  submitting = false;
  
  // Options pour les selects
  sexeOptions = [
    { value: 'M', label: 'Masculin' },
    { value: 'F', label: 'Féminin' }
  ];
  
  statutOptions = [
    { value: 'ACTIF', label: 'Actif' },
    { value: 'SUSPENDU', label: 'Suspendu' },
    { value: 'RESILIE', label: 'Résilié' }
  ];

  // Photo
  photoPreview: string | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private adherentService: AdherentService
  ) {}
onImprimeChange(event: Event) {
  const checked = (event.target as HTMLInputElement).checked;
  this.adherentForm.patchValue({ imprime: checked ? 'O' : 'N' });
}
  ngOnInit(): void {
    this.initForm();
    
    // Vérifier si on est en mode édition
    this.route.params.subscribe(params => {
      if (params['id'] && params['id'] !== 'create') {
        this.isEditMode = true;
        this.codeAdherent = params['id'];
        this.loadAdherent();
      }
    });
  }

  /**
   * Initialise le formulaire
   */
  initForm(): void {
    this.adherentForm = this.fb.group({
      // Informations personnelles
      assurePrincipal: ['', [Validators.required]],
      naissance: ['', [Validators.required]],
      sexe: ['M', [Validators.required, Validators.pattern('^[MF]$')]],
      matricule: [''],
      telephone: ['', [Validators.pattern('^[+]?[0-9]{8,15}$')]],
      email: ['', [Validators.email]],
      adresse: [''],
      ville: [''],
      
      // Informations de couverture
      souscripteur: ['', [Validators.required]],
      police: ['', [Validators.required]],
      effetPolice: [''],
      echeancePolice: [''],
      groupe: [null],
      taux: [100, [Validators.min(0), Validators.max(100)]],
      
      // Statut
      statut: ['ACTIF', [Validators.required]],
      enrole: ['N'],
      imprime: ['N']
    });
  }

  /**
   * Charge les données de l'adhérent en mode édition
   */
  loadAdherent(): void {
    this.loading = true;
    this.adherentService.getAdherentProfile(this.codeAdherent).subscribe({
      next: (adherent) => {
        this.patchFormValues(adherent);
        this.photoPreview = adherent.photo || null;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement', error);
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Impossible de charger les données de l\'adhérent'
        });
        this.loading = false;
        this.router.navigate(['/adherents']);
      }
    });
  }

  /**
   * Remplit le formulaire avec les données existantes
   */
  patchFormValues(adherent: Adherent): void {
    this.adherentForm.patchValue({
      assurePrincipal: adherent.assurePrincipal,
      naissance: this.formatDateForInput(adherent.naissance),
      sexe: adherent.sexe,
      matricule: adherent.matricule,
      telephone: adherent.telephone,
      email: adherent.email,
      adresse: adherent.adresse,
      ville: adherent.ville,
      souscripteur: adherent.souscripteur,
      police: adherent.police,
      effetPolice: adherent.effetPolice ? this.formatDateForInput(adherent.effetPolice) : '',
      echeancePolice: adherent.echeancePolice ? this.formatDateForInput(adherent.echeancePolice) : '',
      groupe: adherent.groupe,
      taux: adherent.taux,
      statut: adherent.statut,
      enrole: adherent.enrole || 'N',
      imprime: adherent.imprime || 'N'
    });
  }

  /**
   * Formate une date pour l'input HTML
   */
  formatDateForInput(date: Date): string {
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  /**
   * Gestion de l'upload de photo
   */
  onPhotoChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];
      
      // Vérifier le type de fichier
      if (!file.type.startsWith('image/')) {
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Veuillez sélectionner une image valide'
        });
        return;
      }
      
      // Vérifier la taille (max 5MB)
      if (file.size > 5 * 1024 * 1024) {
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'L\'image ne doit pas dépasser 5MB'
        });
        return;
      }
      
      // Convertir en base64 pour preview
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.photoPreview = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  /**
   * Supprime la photo
   */
  removePhoto(): void {
    this.photoPreview = null;
  }

  /**
   * Soumission du formulaire
   */
  onSubmit(): void {
    if (this.adherentForm.invalid) {
      this.markFormGroupTouched(this.adherentForm);
      Swal.fire({
        icon: 'warning',
        title: 'Formulaire invalide',
        text: 'Veuillez remplir tous les champs obligatoires correctement'
      });
      return;
    }

    this.submitting = true;
    const formValue = this.adherentForm.value;
    
    // Construire l'objet adhérent
    const adherent: Adherent = {
      ...formValue,
      photo: this.photoPreview || undefined,
      naissance: new Date(formValue.naissance),
      effetPolice: formValue.effetPolice ? new Date(formValue.effetPolice) : undefined,
      echeancePolice: formValue.echeancePolice ? new Date(formValue.echeancePolice) : undefined
    };

    if (this.isEditMode) {
      this.updateAdherent(adherent);
    } else {
      this.createAdherent(adherent);
    }
  }

  /**
   * Créer un nouvel adhérent
   */
  createAdherent(adherent: Adherent): void {
    this.adherentService.createAdherent(adherent).subscribe({
      next: (created) => {
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: 'Adhérent créé avec succès',
          timer: 3000,
          showConfirmButton: false
        });
        this.submitting = false;
        this.router.navigate(['/adherents', created.codeAdherent]);
      },
      error: (error) => {
        console.error('Erreur lors de la création', error);
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: error.error?.message || 'Erreur lors de la création de l\'adhérent'
        });
        this.submitting = false;
      }
    });
  }

  /**
   * Mettre à jour un adhérent
   */
  updateAdherent(adherent: Adherent): void {
    this.adherentService.updateAdherent(this.codeAdherent, adherent).subscribe({
      next: (updated) => {
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: 'Adhérent modifié avec succès',
          timer: 3000,
          showConfirmButton: false
        });
        this.submitting = false;
        this.router.navigate(['/adherents', updated.codeAdherent]);
      },
      error: (error) => {
        console.error('Erreur lors de la modification', error);
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: error.error?.message || 'Erreur lors de la modification de l\'adhérent'
        });
        this.submitting = false;
      }
    });
  }

  /**
   * Annuler et retourner
   */
  onCancel(): void {
    if (this.adherentForm.dirty) {
      Swal.fire({
        title: 'Modifications non enregistrées',
        text: 'Voulez-vous vraiment quitter sans enregistrer ?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Oui, quitter',
        cancelButtonText: 'Annuler'
      }).then((result) => {
        if (result.isConfirmed) {
          this.goBack();
        }
      });
    } else {
      this.goBack();
    }
  }

  /**
   * Retour à la page précédente
   */
  goBack(): void {
    if (this.isEditMode) {
      this.router.navigate(['/adherents', this.codeAdherent]);
    } else {
      this.router.navigate(['/adherents']);
    }
  }

  /**
   * Marquer tous les champs comme touched pour afficher les erreurs
   */
  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
    });
  }

  /**
   * Vérifie si un champ a une erreur
   */
  hasError(fieldName: string, errorType?: string): boolean {
    const field = this.adherentForm.get(fieldName);
    if (!field) return false;
    
    if (errorType) {
      return field.hasError(errorType) && (field.dirty || field.touched);
    }
    return field.invalid && (field.dirty || field.touched);
  }
}
