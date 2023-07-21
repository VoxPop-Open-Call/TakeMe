import {Moment} from 'moment';
import {IOrganization} from 'app/shared/model//organization.model';
import {IServiceStopPoint} from 'app/shared/model/service-stop-point.model';
import {IChildItinerarySubscription} from "../../entities/child-itinerary-subscription/child-itinerary-subscription.model";

export interface IService {
    id?: number;
    recurrent?: boolean;
    startDate?: Moment;
    endDate?: Moment;
    organization?: IOrganization;
    childItinerarySubscription?: IChildItinerarySubscription;
    serviceStopPoints?: IServiceStopPoint[];
    selected?: boolean;
    selectable?: boolean;
}

export class Service implements IService {
    constructor(
        public id?: number,
        public recurrent?: boolean,
        public startDate?: Moment,
        public endDate?: Moment,
        public organization?: IOrganization,
        public childItinerarySubscription?: IChildItinerarySubscription,
        public serviceStopPoints?: IServiceStopPoint[],
        public selected?: boolean,
        public selectable?: boolean
    ) {
        this.recurrent = this.recurrent || false;
    }
}
