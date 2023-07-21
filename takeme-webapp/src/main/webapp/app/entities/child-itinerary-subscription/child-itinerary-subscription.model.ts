import dayjs from 'dayjs/esm';
import {IChild, StatusType} from "../../shared/model/child.model";
import {IPromoterItinerary} from "../../backoffice/famility/promoter-itinerary/promoter-itinerary.model";
import {IUser} from "../../core";

export interface IChildItinerarySubscription {
    id: number;
    statusType?: StatusType | null;
    subscriptionDate?: dayjs.Dayjs | null;
    activationDate?: dayjs.Dayjs | null;
    deactivationDate?: dayjs.Dayjs | null;
    comments?: string | null;
    additionalInformation?: string | null;
    cardNumber?: string | null;
    child?: IChild;
    promoterItinerary?: IPromoterItinerary;
    user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewChildItinerarySubscription = Omit<IChildItinerarySubscription, 'id'> & { id: null };
