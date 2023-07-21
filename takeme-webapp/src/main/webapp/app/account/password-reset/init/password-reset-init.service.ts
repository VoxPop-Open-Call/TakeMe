import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class PasswordResetInitService {
    constructor(private http: HttpClient) {}

    reset(email: string): Observable<HttpResponse<any>> {
        const data = { email };
        return this.http.post('api/account/reset-password', data, { observe: 'response' });
    }
}
