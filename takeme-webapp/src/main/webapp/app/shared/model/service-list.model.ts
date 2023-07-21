import { Moment } from 'moment';
import { ServiceStopPoint } from 'app/shared/model/service-stop-point.model';

export class ServiceListModel {
    constructor(
        public id?: number,
        public recurrent?: boolean,
        public startDate?: Moment,
        public endDate?: Moment,
        public serviceStopPoints?: ServiceStopPoint[]
    ) {
        this.recurrent = this.recurrent || false;
    }
}
