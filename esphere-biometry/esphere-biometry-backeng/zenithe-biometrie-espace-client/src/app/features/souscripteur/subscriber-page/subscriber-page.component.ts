import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscriber } from '../../../core/models/Subscriber';
import { SubscriberFormComponent } from '../subscriber-form/subscriber-form.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-subscriber-page',
  standalone: true,
  imports: [CommonModule, SubscriberFormComponent],
  templateUrl: './subscriber-page.component.html',
  styleUrl: './subscriber-page.component.css'
})
export class SubscriberPageComponent implements OnInit {
  subscriberId?: number;
  successMessage = '';

  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.subscriberId = id ? Number(id) : undefined;
  }

  onSaved(subscriber: Subscriber): void {
    this.successMessage = `Souscripteur "${subscriber.fullName}" ${this.subscriberId ? 'modifié' : 'créé'} avec succès !`;
    setTimeout(() => {
      this.router.navigate(['/subscribers']);
    }, 1500);
  }

  onCancelled(): void {
    this.router.navigate(['/subscribers']);
  }

}
