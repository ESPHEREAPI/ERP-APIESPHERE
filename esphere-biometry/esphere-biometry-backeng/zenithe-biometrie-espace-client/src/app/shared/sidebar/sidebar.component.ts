import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
//import { MenuItem } from '../model/menu-item';

import { MenuItem } from 'primeng/api';
import { AuthService } from '../../core/auth/auth.service';
import { User } from '../../core/models/user';
//import { MenuItem } from '../model/menu-item';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit {
  @Input() user: User | null = null;
  imagePath = './img/user2-160x160.jpg';
    userSession: any;

  //menuItems: MenuItem[] = [];

 profilType:string | null= "";
  constructor(private authService: AuthService) {
    


  }
  ngOnInit(): void {
     this.userSession = this.authService.currentUserValue;
    if (this.userSession && this.userSession.usersDTO) {
      console.log(this.user)
      this.user = this.userSession.usersDTO;
     
    }
    if (this.user) {
      this.imagePath = this.user.profileImageUrl ?? './img/user2-160x160.jpg';
      this.profilType=this.user.profilType?.toString() ?? null;
    } else {
      this.imagePath = './img/user2-160x160.jpg';
    }

  }
  hasPermission(item: MenuItem): boolean {
   
   //if (!item.permissions || item.permissions.length === 0) {
   //   return true;
   // }

   // return item.permissions.some(permission => this.authService.hasPermission(permission)); 
   return true;

  }

  hasChildPermissions(item: MenuItem): boolean {
    //if (!item.children || item.children.length === 0) {
     // return false;
   // }

    //return item.children.some(child => this.hasPermission(child));
    return true;
  }

  toggleExpanded(item: MenuItem): void {
    item.expanded = !item.expanded;
  }


menuItems = [
    {
      title: 'Dashboard',
      icon: 'fas fa-tachometer-alt',
      route: '/dashboard',
      children: [],
      profilType:'SOUSCRIPTEUR'
    },
     {
      title: 'Dashboard PayLoad',
      icon: 'fas fa-tachometer-alt',
      route: '/dashboard-payload',
      children: [],
      profilType:'PAYLOAD'
    },
     {
      title: 'Certificate',
      icon: 'fas fa-tachometer-alt',
      route: '/certificate-payload',
      children: [],
      profilType:'PAYLOAD'
    },
    {
      title: 'Adherents',
      icon: 'fas fa-users',
      route: '',
       children: [
        
        {
          title: 'List Adherent',
            icon: 'far fa-circle',
          route: '/adherents/adherents-list',
          //permission: 'adherents.list'
        },
        {
          title: 'Detail Adherent',
            icon: 'far fa-circle',
          route: '/historique',
         // permission: 'adherents.profile'
        }
      ],
         profilType:'SOUSCRIPTEUR'
    },
     {
      title: 'Ayant Droit',
      icon: 'fas fa-user-friends',
      route: '/ayants-droit',
       children: [],
      profilType:'SOUSCRIPTEUR'
    //  permission: 'ayants-droit.view'
    },
    {
      title: 'Reporting',
      icon: 'fas fa-chart-bar',
      route: '/reporting',
       children: [],
      profilType:'DII'
     // permission: 'reporting.view'
    },
    {
      title: 'Notifications',
      icon: 'fas fa-bell',
      route: '/notifications',
       children: [],
          profilType:'DII'
      //permission: 'notifications.view'
    },
    {
      title: 'Paramètres',
      icon: 'fas fa-cogs',
      route: '',
      children: [
        {
          title: 'Souscripteur',
          icon: 'far fa-circle',
          route: '/subscribers'
        },
        {
          title: 'Sécurité',
          icon: 'far fa-circle',
          route: '/security'
        }
      ],
         profilType:'DII'
    }
  ];



}
