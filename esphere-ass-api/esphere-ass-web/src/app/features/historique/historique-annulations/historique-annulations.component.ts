import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
import { CertificateService } from '../../../core/services/certificate.service';

@Component({
  selector: 'app-historique-annulations',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslatePipe],
  templateUrl: './historique-annulations.component.html',
  styleUrl: './historique-annulations.component.css'
})
export class HistoriqueAnnulationsComponent implements OnInit {
  historique: any[] = [];
  filtered:   any[] = [];
  search = '';
  isLoading = true;
  errorMsg  = '';

  constructor(private certService: CertificateService) {}

  ngOnInit(): void {
    this.certService.getHistoriqueByOfficeAndType('ANNULATION').subscribe({
      next: h => { this.historique = h; this.filtered = h; this.isLoading = false; },
      error: () => { this.errorMsg = 'HISTORIQUE.ERROR_LOAD'; this.isLoading = false; }
    });
  }

  applyFilter(): void {
    const q = this.search.toLowerCase();
    this.filtered = this.historique.filter(h =>
      h.certificateReference?.toLowerCase().includes(q) ||
      h.motifLibelle?.toLowerCase().includes(q) ||
      h.usernameExecutant?.toLowerCase().includes(q)
    );
  }
}
