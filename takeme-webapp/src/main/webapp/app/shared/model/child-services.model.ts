import { Child } from 'app/shared/model/child.model';
import { Service } from 'app/shared/model/service.model';

export class ChildServices {
    constructor(public child?: Child, public services?: Service[]) {}
}
