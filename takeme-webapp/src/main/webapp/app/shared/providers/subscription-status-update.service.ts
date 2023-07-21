import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class SubscriptionStatusUpdateService {
    subscriptionsChanged = new Subject();

    constructor() {}

    newChanges(status: string) {
        this.subscriptionsChanged.next(status);
    }
}
