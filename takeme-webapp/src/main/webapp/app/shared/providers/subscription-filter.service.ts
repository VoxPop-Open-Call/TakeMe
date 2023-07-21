import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export enum SubscriptionFilterType {
    ACCEPTED = 'ACTIVE',
    PENDING = 'PENDING',
    REJECTED = 'INACTIVE'
}

@Injectable()
export class SubscriptionFilterService {
    private filter = new BehaviorSubject(SubscriptionFilterType.PENDING.valueOf());
    currentFilter = this.filter.asObservable();

    constructor() {}

    changeFilter(newFilter: string) {
        this.filter.next(newFilter);
    }
}
