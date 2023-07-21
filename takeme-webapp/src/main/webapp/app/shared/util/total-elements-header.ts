import { Injectable } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';

@Injectable()
export class TotalElementsHeader {
    getTotalElements(header: HttpHeaders): number {
        if (header.has('X-Total-Count')) {
            return parseInt(header.get('X-Total-Count'), 10);
        } else {
            return 0;
        }
    }
}
