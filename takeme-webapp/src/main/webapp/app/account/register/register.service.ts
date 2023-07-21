import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ITermsAndConditions} from "../../shared/model/terms-and-conditions";

@Injectable({providedIn: 'root'})
export class Register {

  constructor(private http: HttpClient) {
  }

  save(account: any): Observable<any> {
    return this.http.post(SERVER_API_URL + 'api/register', account);
  }

    getTermsAndConditionsURL(): Observable<ITermsAndConditions> {
        return this.http.get<ITermsAndConditions>(SERVER_API_URL + 'api/terms-and-conditions');
    }
}
