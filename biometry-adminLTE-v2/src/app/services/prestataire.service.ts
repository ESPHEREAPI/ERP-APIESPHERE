import { Injectable }     from '@angular/core';
import { HttpClient,
         HttpParams }     from '@angular/common/http';
import { Observable }     from 'rxjs';

export interface VisiteInfo {
  codeVisite:       string;
  annee:            string;
  naturePrestation: string;
  prestataireId:    string;
  nomAssure:        string | null;
  nomAyantDroit:    string | null;
  souscripteur:     string | null;
  groupe:           number | null;
}

export interface TypeConsultation {
  code:   string;
  libelle: string;
}

export interface Medicament {
  id:          number;
  nom:         string;
  codification: string | null;
  prix:        number | null;
  nouveau?:     boolean;  // ← ajouter ceci
}

export interface ExamenActe {
  id:           number;
  code?:        string;
  nom:          string;
  cotation?:    number;
  valeur?:      number;
  typeExamen?:  string;
  codification?: string;
  statut?:      string;
  supprime?:    string;
  nouveau?:     boolean; // true = vient d'être créé en BDD
}
export interface TypePrestation {
  id: string;
  nom: string;
  affiche: number;
  categorie: string;
}
@Injectable({ providedIn: 'root' })
export class PrestataireService {

  constructor(private http: HttpClient) {}

  // ── Infos visite depuis sessionStorage ────────────────
  getVisiteInfo(): VisiteInfo | null {
    const code   = sessionStorage.getItem('visite_code');
    const annee  = sessionStorage.getItem('visite_annee');
    const nature = sessionStorage.getItem('visite_nature');
    const prest  = sessionStorage.getItem('visite_prestataire');

    if (!code) return null;

    return {
      codeVisite:       code,
      annee:            annee   || '',
      naturePrestation: nature  || '',
      prestataireId:    prest   || '',
      nomAssure:        null,
      nomAyantDroit:    null,
      souscripteur:     null,
      groupe:           null
    };
  }

  // ── Infos visite depuis le serveur ────────────────────
  getInfosVisite(codeVisite: string): Observable<any> {
    return this.http.get<any>(
      `/validations/visite/${codeVisite}`);
  }

  // ── Types consultation ────────────────────────────────
  getTypesConsultation(): Observable<TypeConsultation[]> {
    return this.http.get<TypeConsultation[]>(
      '/referentiel/types-consultation');
  }

  // ── Liste médicaments ─────────────────────────────────
  getMedicaments(
    search?: string
  ): Observable<Medicament[]> {
    let params = new HttpParams();
    if (search) params = params.set('search', search);
    return this.http.get<Medicament[]>(
      '/validations/referentiel/medicaments', { params });
  }

  // ── Liste examens/actes ───────────────────────────────
  getExamensActes(
    search?: string
  ): Observable<ExamenActe[]> {
    let params = new HttpParams();
    if (search) params = params.set('search', search);
    return this.http.get<ExamenActe[]>(
      '/validations/referentiel/examens-actes', { params });
  }

  getTypesExamen(): Observable<TypePrestation[]> {
  const categorie = 'examens';
  const url = `/validations/referentiel/types-examen/${categorie}`;
  
  console.log('URL appelée :', url); // 👈 pour déboguer
  
  return this.http.get<TypePrestation[]>(url);
}
  // ── Soumettre consultation ────────────────────────────
  soumettreConsultation(data: any): Observable<any> {
    return this.http.post<any>(
      '/validations/consultations', data);
  }

  // ── Soumettre ordonnance ──────────────────────────────
  soumettreOrdonnance(data: any): Observable<any> {
    return this.http.post<any>(
      '/validations/prestations', data);
  }

  // ── Soumettre examen ──────────────────────────────────
  soumettreExamen(data: any): Observable<any> {
    console.log("data prestation",data);
    return this.http.post<any>(
      '/validations/prestations', data);
  }

  // ── Consommation adhérent ─────────────────────────────
  getConsommation(visiteId: string): Observable<any> {
    return this.http.get<any>(
      `/validations/consommation/visite/${visiteId}`);
  }
  // Dans prestataire.service.ts — ajouter cette méthode

/** Recherche un médicament par nom ou le crée s'il est introuvable */
rechercherOuCreerMedicament(nom: string): Observable<Medicament> {
  const url = `/validations/medicaments/rechercher-ou-creer`;
  return this.http.post<Medicament>(url, { nom });
}

/**
 * POST /api/examens/rechercher-ou-creer
 * Cherche l'examen par nom en BDD, le crée s'il est introuvable.
 * Retourne { ...examen, nouveau: true } si création.
 */
rechercherOuCreerExamen(nom: string): Observable<ExamenActe> {
  return this.http.post<ExamenActe>(
    `/validations/examens/rechercher-ou-creer`,
    { nom }
  );
}

getTaux(
    police:         string,
    groupe:         number,
    typePrestation: string,
    codeAdherent:   string
): Observable<any> {
  console.log(police,groupe,typePrestation,codeAdherent);
  
    return this.http.get<any>(
        `/validations/taux`, {
            params: {
                police,
                groupe:         groupe.toString(),
                typePrestation,
                codeAdherent
            }
        });
}
}