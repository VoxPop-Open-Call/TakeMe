import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export enum OrganizationsFilterType {
    ACTIVE = 'ACTIVE',
    INACTIVE = 'INACTIVE'
}

@Injectable()
export class OrganizationsFilterService {
    private filter = new BehaviorSubject(OrganizationsFilterType.ACTIVE.valueOf());
    currentFilter = this.filter.asObservable();

    constructor() {}

    changeFilter(newFilter: string) {
        this.filter.next(newFilter);
    }
}
