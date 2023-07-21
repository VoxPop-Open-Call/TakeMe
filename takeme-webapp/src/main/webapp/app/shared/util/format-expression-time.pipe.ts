import { Pipe, PipeTransform } from "@angular/core";
import moment from "moment/moment";

@Pipe({
    name: 'formatExpressionTime',
})
export class FormatExpressionTimePipe implements PipeTransform {
    transform(timestamp: any): string {
        if (timestamp === undefined) {
            return '-';
        }
        const createdMoment = moment(timestamp);
        return createdMoment && createdMoment.isValid() ? createdMoment.format('HH:mm') : '-';
    }
}
