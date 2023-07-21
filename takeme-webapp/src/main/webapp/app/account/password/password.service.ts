import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class PasswordService {
    constructor(private http: HttpClient) {}

    save(newPassword: string, currentPassword: string): Observable<HttpResponse<any>> {
        const data = {
            currentPassword,
            newPassword
        };
        return this.http.post('api/account/change-password', data, { observe: 'response' });
    }
}
