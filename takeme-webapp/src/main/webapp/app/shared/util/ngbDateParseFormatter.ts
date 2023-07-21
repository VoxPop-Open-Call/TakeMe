import { Injectable } from '@angular/core';
import { NgbDateParserFormatter, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import moment from 'moment';

function padNumber(value: number) {
    if (isNumber(value)) {
        return `0${value}`.slice(-2);
    } else {
        return '';
    }
}

function isNumber(value: any): boolean {
    return !isNaN(toInteger(value));
}

function toInteger(value: any): number {
    return parseInt(`${value}`, 10);
}

@Injectable()
export class CustomNgbDateParserFormatter extends NgbDateParserFormatter {
    parse(value: string): NgbDateStruct {
        if (value) {
            const myDate = moment(value, 'DD/MM/YYYY').toDate();
            return { year: myDate.getFullYear(), month: myDate.getMonth(), day: myDate.getDay() };
        }
        return null;
    }

    format(date: NgbDateStruct): string {
        let stringDate: string;
        if (date) {
            const d = new Date(date.year, date.month - 1, date.day);
            stringDate = moment(d).format('DD/MM/YYYY');
        } else {
            stringDate = '';
        }
        return stringDate;
    }
}
