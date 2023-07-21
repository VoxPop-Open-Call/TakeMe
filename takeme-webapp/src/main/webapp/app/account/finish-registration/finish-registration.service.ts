import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Tutor} from "../../shared/model/tutor.model";

@Injectable({providedIn: 'root'})
export class FinishRegistrationService {

  constructor(private http: HttpClient) {
  }

  createTutor(tutor: any): Observable<Tutor> {
    return this.http.post<Tutor>('api/tutors', tutor);
  }
}
