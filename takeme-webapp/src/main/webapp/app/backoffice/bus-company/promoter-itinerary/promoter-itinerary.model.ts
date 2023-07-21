import dayjs from 'dayjs/esm';
import { IPromoterService } from 'app/backoffice/famility/promoter-service/promoter-service.model';
import { IPromoterStopPoint } from "../../famility/promoter-stop-point/promoter-stop-point.model";
import { Organization } from "../../../shared/model/organization.model";

export interface IPromoterItinerary {
  id: number;
  name?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  service?: Pick<IPromoterService, 'id' | 'name'> | null;
  organization?: Pick<Organization, 'id' | 'name'> | null;
  promoterItineraryStopPoints?: IPromoterStopPoint[];
}

export type NewPromoterItinerary = Omit<IPromoterItinerary, 'id'> & { id: null };
