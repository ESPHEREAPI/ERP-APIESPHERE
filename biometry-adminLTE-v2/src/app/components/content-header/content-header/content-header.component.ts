import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { filter } from 'rxjs/operators';

interface Breadcrumb {
  label: string;
  url: string;
}

@Component({
  selector: 'app-content-header',
  standalone: true,
  imports: [CommonModule, RouterModule, TranslateModule],
  templateUrl: './content-header.component.html',
  styleUrls: ['./content-header.component.css']
})
export class ContentHeaderComponent implements OnInit {
  pageTitle = '';
  pageSubtitle = '';
  breadcrumbs: Breadcrumb[] = [];

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.updateBreadcrumbs();

    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.updateBreadcrumbs();
    });

    // Recalculer quand la langue change
    this.translate.onLangChange.subscribe(() => {
      this.updateBreadcrumbs();
    });
  }

  private updateBreadcrumbs(): void {
    const root = this.activatedRoute.root;
    this.breadcrumbs = this.getBreadcrumbs(root);

    if (this.breadcrumbs.length > 0) {
      this.pageTitle = this.breadcrumbs[this.breadcrumbs.length - 1].label;
    }

    // Récupérer le subtitle de la route active
    let route = this.activatedRoute.root;
    while (route.firstChild) {
      route = route.firstChild;
    }
    const subtitleKey = route.snapshot.data['pageSubtitle'];
    this.pageSubtitle = subtitleKey ? this.translate.instant(subtitleKey) : '';
  }

  private getBreadcrumbs(
    route: ActivatedRoute,
    url: string = '',
    breadcrumbs: Breadcrumb[] = []
  ): Breadcrumb[] {
    const children = route.children;

    if (children.length === 0) return breadcrumbs;

    for (const child of children) {
      const routeURL = child.snapshot.url.map(s => s.path).join('/');
      if (routeURL !== '') url += `/${routeURL}`;

      const breadcrumbKey = child.snapshot.data['breadcrumb'];
      if (breadcrumbKey) {
        breadcrumbs.push({
          label: this.translate.instant(breadcrumbKey), // 🔑 traduction ici
          url
        });
      }

      return this.getBreadcrumbs(child, url, breadcrumbs);
    }

    return breadcrumbs;
  }
}