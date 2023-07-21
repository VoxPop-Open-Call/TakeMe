import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { IPhotoTutor } from 'app/shared/model/photo-tutor.model';
import { IPhotoChild } from 'app/shared/model/photo-child.model';
import { Observable } from 'rxjs';
import { IPhotoDriver } from 'app/shared/model/photo-driver';
import { IPhoto } from "../model/photo.model";

@Injectable({
    providedIn: 'root'
})
export class PhotoService {
    constructor(
        private httpClient: HttpClient
    ) {
    }

    patchPhotoTutor(idTutor, photo): Observable<HttpResponse<any>> {
        const data = {photo};
        return this.httpClient.patch<any>(`api/tutors/${idTutor}/photo`, data, {observe: 'response'});
    }

    patchPhotoChild(idChild, photo) {
        const data = {photo};
        return this.httpClient.patch<any>(`api/children/${idChild}/photo`, data, {observe: 'response'});
    }

    getPhotoChild(idChild, photoId): Observable<HttpResponse<IPhotoChild>> {
        return this.httpClient.get<IPhotoChild>(`api/children/${idChild}/photo/${photoId}`, {observe: 'response'});
    }

    public getPhotoTutor(tutorId, photoId): Observable<HttpResponse<IPhoto>> {
        return this.httpClient.get<IPhoto>(`api/tutors/${tutorId}/photo/${photoId}`, {observe: 'response'});
    }

    getPhotoDriver(idDriver, idPhoto): Observable<HttpResponse<IPhotoDriver>> {
        return this.httpClient.get<IPhotoTutor>(`api/drivers/${idDriver}/photo/${idPhoto}`, {observe: 'response'});
    }
}
