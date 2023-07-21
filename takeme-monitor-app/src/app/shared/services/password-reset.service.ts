import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {BACKEND_BASE_URL} from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class PasswordResetService {

    private BACKEND_RESET_PASSWORD = BACKEND_BASE_URL + '/api/account/reset-password';

    constructor(private http: HttpClient) {
    }

    reset(email: string): Observable<HttpResponse<any>> {
        const data = {email};

        return this.http.post<any>(this.BACKEND_RESET_PASSWORD, data, {observe: 'response'});
    }
}
