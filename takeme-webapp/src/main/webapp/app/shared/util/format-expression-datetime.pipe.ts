import { Pipe, PipeTransform } from "@angular/core";
import moment from "moment/moment";

@Pipe({
  name: 'formatExpressionDateTime',
})
export class FormatExpressionDateTimePipe implements PipeTransform {
  transform(timestamp: any): string {
    if (timestamp === undefined) {
      return '-';
    }
    const createdMoment = moment(timestamp);
    return createdMoment && createdMoment.isValid() ? createdMoment.format('yyyy-MM-DD HH:mm') : '-';
  }
}
