import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})
export class LangueService {
  private keyLanguage = 'userLanguage';
  _userLanguage = '';
  private supportedLanguage = ['en', 'fr'];

  constructor(private translate: TranslateService) {
    this.initLanguage();
    this.translate.use(this._userLanguage);
  }


  initLanguage() {
    const value = localStorage.getItem(this.keyLanguage);
    if (value != null) {
      this._userLanguage = value;
    } else {
      const browserLanguage = navigator.language.split('-')[0];//en-EN,fr-FR
      this._userLanguage = 'fr';
      if (this.supportedLanguage.includes(browserLanguage)) {
        this._userLanguage = browserLanguage;
        localStorage.setItem(this.keyLanguage, browserLanguage);

      }

    }
  }
  setLanguage(language:string){
    this._userLanguage=language;
    localStorage.setItem(this.keyLanguage,this._userLanguage);
    this.translate.use(this._userLanguage);
  }
  getUseuLanguage(){
    return this._userLanguage;
  }

  getAvailableLanguage(){
    return this.supportedLanguage;
  }

}
