import { Component, OnInit } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AdminLteService } from '../../core/services/admin-lte-service';
import { ContentHeaderComponent } from '../../shared/content-header/content-header.component';
import { UserService } from '../../core/services/user.service';
import { RoleService } from '../../core/services/role.service';
import { AuthService } from '../../core/auth/auth.service';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, ContentHeaderComponent, TranslatePipe],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {

  pageTitle = 'Dashboard';
  breadcrumbItems = [
    { label: 'Home', route: '/dashboard' },
    { label: 'Dashboard', active: true }
  ];

  isLoading = true;

  stats = {
    totalUsers:      0,
    activeUsers:     0,
    totalRoles:      0,
    connectedUser:   '',
  };

  constructor(
    private adminLteService: AdminLteService,
    private userService:     UserService,
    private roleService:     RoleService,
    private authService:     AuthService
  ) {}

  ngOnInit(): void {
    setTimeout(() => this.adminLteService.iniAdminLTE, 100);

    const session = this.authService.currentUserValue;
    this.stats.connectedUser = session?.userDTO?.nomcomplet ?? session?.userDTO?.username ?? '';

    forkJoin({
      users: this.userService.getUsers(1, 1).pipe(catchError(() => of({ data: [], total: 0 }))),
      roles: this.roleService.getAllRoles().pipe(catchError(() => of([]))),
    }).subscribe({
      next: ({ users, roles }) => {
        this.stats.totalUsers  = users.total ?? 0;
        this.stats.activeUsers = users.data?.filter((u: any) => u.isActive).length ?? 0;
        this.stats.totalRoles  = roles.length ?? 0;
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }
}
