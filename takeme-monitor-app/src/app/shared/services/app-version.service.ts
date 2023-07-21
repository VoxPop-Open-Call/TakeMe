import { Injectable } from '@angular/core';
import { AppVersion } from '@ionic-native/app-version/ngx';

@Injectable({
  providedIn: 'root'
})
export class AppVersionService {

  constructor(private appVersion: AppVersion) { }

  getAppName(): Promise<string> {
    return this.appVersion.getAppName();
  }

  getPackageName(): Promise<string> {
    return this.appVersion.getPackageName();
  }

  getVersionCode()  {
    return this.appVersion.getVersionCode();
  }

  getVersionNumber(): Promise<string> {
    if (this.appVersion) {
      return this.appVersion.getVersionNumber();
    } else {
      return new Promise((resolve, reject) => {
        resolve('N/A');
      });
    }
  }
}
