import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export enum TutorsFilterType {
    ACTIVE = 'ACTIVE',
    INACTIVE = 'INACTIVE'
}

@Injectable()
export class TutorsFilterService {
    private filter = new BehaviorSubject(TutorsFilterType.ACTIVE.valueOf());
    currentFilter = this.filter.asObservable();

    constructor() {}

    changeFilter(newFilter: string) {
        this.filter.next(newFilter);
    }
}
