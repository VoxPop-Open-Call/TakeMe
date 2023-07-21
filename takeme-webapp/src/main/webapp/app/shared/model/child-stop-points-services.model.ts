import { Child } from 'app/shared/model/child.model';
import { ServiceStopPoint } from 'app/shared/model/service-stop-point.model';

export class ChildStopPointsServices {
    constructor(public child?: Child, public serviceStopPoints?: ServiceStopPoint[]) {}
}
