import {Moment} from 'moment';
import {ITutor} from 'app/shared/model//tutor.model';
import {IOrganization} from 'app/shared/model//organization.model';
import {IChildSubscriptionChild} from 'app/shared/model/child-subscription-child.model';
import {IChildItinerarySubscription} from "../../entities/child-itinerary-subscription/child-itinerary-subscription.model";

export const enum StatusType {
    NEW = 'NEW',            // TODO: REMOVE
    ACTIVE = 'ACTIVE',
    INACTIVE = 'INACTIVE',
    PENDING = 'PENDING'
}

export interface IChild {
    id?: number;
    name?: string;
    nifNumber?: string;
    dateOfBirth?: Moment;
    famility?: boolean;
    statusType?: StatusType;
    nifCountry?: string;
    photo?: string;
    photoId?: string;
    tutors?: ITutor[];
    itinerarySubscriptions?: IChildItinerarySubscription[];
    organizations?: IOrganization[];
    childSubscriptionChild?: IChildSubscriptionChild[];
}

export class Child implements IChild {
    constructor(
        public id?: number,
        public name?: string,
        public nifNumber?: string,
        public dateOfBirth?: Moment,
        public famility?: boolean,
        public statusType?: StatusType,
        public nifCountry?: string,
        public photo?: string,
        public photoId?: string,
        public tutors?: ITutor[],
        public itinerarySubscriptions?: IChildItinerarySubscription[],
        public organizations?: IOrganization[],
        public childSubscriptionChild?: IChildSubscriptionChild[]
    ) {
        this.famility = this.famility || false;
    }
}
