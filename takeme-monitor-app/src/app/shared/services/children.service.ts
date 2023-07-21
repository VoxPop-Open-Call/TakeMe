import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpResponse, HttpClient } from '@angular/common/http';
import { BACKEND_BASE_URL } from 'src/environments/environment';
import { PhotoDTO } from '../models/photo-dto';
import { TutorDTO } from '../models/tutor-dto';

@Injectable({
    providedIn: 'root'
})
export class ChildrenService {

    private BACKEND_URL = BACKEND_BASE_URL + '/api/children';

    constructor(private http: HttpClient) {
    }

    public getChildPhoto(childId: string, photoId: String): Observable<HttpResponse<PhotoDTO>> {
        return this.http.get<PhotoDTO>(this.BACKEND_URL + '/' + childId + '/photo/' + photoId, { observe: 'response' });
    }

    public getTutorsByChildId(childId: string): Observable<HttpResponse<TutorDTO[]>> {
        return this.http.get<TutorDTO[]>(this.BACKEND_URL + '/' + childId + '/tutors', { observe: 'response' });
    }
}
