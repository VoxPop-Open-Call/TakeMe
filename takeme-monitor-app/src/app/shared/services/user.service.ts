import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {UserDTO} from '../models/user-dto';
import {BACKEND_BASE_URL} from '../../../environments/environment';
import {Observable} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    private BACKEND_USER_INFO = BACKEND_BASE_URL + '/api/users/info';

    constructor(private httpClient: HttpClient) {
    }

    getUserInformation(): Observable<HttpResponse<UserDTO>> {
        return this.httpClient.get<UserDTO>(this.BACKEND_USER_INFO, {observe: 'response'});
    }
}
