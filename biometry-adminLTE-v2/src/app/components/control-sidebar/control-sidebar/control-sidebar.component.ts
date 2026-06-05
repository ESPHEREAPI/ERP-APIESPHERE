import { Component, Input, Renderer2 } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { User } from '../../../models/user';

@Component({
  selector: 'app-control-sidebar',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule],
  templateUrl: './control-sidebar.component.html',
  styleUrls: ['./control-sidebar.component.css']
})
export class ControlSidebarComponent {
  activeTab = 'home';
   @Input() user: User | null = null
  isOpen = false;
  
  settings = {
    fixedLayout: false,
    boxedLayout: false,
    sidebar: {
      collapsed: false,
      expandOnHover: false,
      fixed: true
    },
    skin: 'blue'
  };

  availableSkins = [
    { name: 'blue', label: 'Bleu', class: 'skin-blue' },
    { name: 'black', label: 'Noir', class: 'skin-black' },
    { name: 'purple', label: 'Violet', class: 'skin-purple' },
    { name: 'green', label: 'Vert', class: 'skin-green' },
    { name: 'red', label: 'Rouge', class: 'skin-red' },
    { name: 'yellow', label: 'Jaune', class: 'skin-yellow' }
  ];

  constructor(private renderer: Renderer2) {}

  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }

  toggle(): void {
    this.isOpen = !this.isOpen;
    
    if (this.isOpen) {
      this.renderer.addClass(document.body, 'control-sidebar-open');
    } else {
      this.renderer.removeClass(document.body, 'control-sidebar-open');
    }
  }

  changeSkin(skin: string): void {
    this.availableSkins.forEach(s => {
      this.renderer.removeClass(document.body, s.class);
    });
    
    const selectedSkin = this.availableSkins.find(s => s.name === skin);
    if (selectedSkin) {
      this.renderer.addClass(document.body, selectedSkin.class);
      this.settings.skin = skin;
    }
  }

  toggleFixedLayout(): void {
    if (this.settings.fixedLayout) {
      this.renderer.addClass(document.body, 'fixed');
    } else {
      this.renderer.removeClass(document.body, 'fixed');
    }
  }

  toggleBoxedLayout(): void {
    if (this.settings.boxedLayout) {
      this.renderer.addClass(document.body, 'layout-boxed');
    } else {
      this.renderer.removeClass(document.body, 'layout-boxed');
    }
  }

  toggleSidebarFixed(): void {
    if (this.settings.sidebar.fixed) {
      this.renderer.addClass(document.body, 'sidebar-fixed');
    } else {
      this.renderer.removeClass(document.body, 'sidebar-fixed');
    }
  }
}