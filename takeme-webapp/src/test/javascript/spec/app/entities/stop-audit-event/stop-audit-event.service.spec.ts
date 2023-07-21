/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { StopAuditEventService } from 'app/entities/stop-audit-event/stop-audit-event.service';
import { IStopAuditEvent, StopAuditEvent, StopEventType } from 'app/shared/model/stop-audit-event.model';

describe('Service Tests', () => {
    describe('StopAuditEvent Service', () => {
        let injector: TestBed;
        let service: StopAuditEventService;
        let httpMock: HttpTestingController;
        let elemDefault: IStopAuditEvent;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(StopAuditEventService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new StopAuditEvent(0, currentDate, StopEventType.CHECK_IN);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        eventTime: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a StopAuditEvent', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        eventTime: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        eventTime: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new StopAuditEvent(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a StopAuditEvent', async () => {
                const returnedFromService = Object.assign(
                    {
                        eventTime: currentDate.format(DATE_TIME_FORMAT),
                        eventType: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        eventTime: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of StopAuditEvent', async () => {
                const returnedFromService = Object.assign(
                    {
                        eventTime: currentDate.format(DATE_TIME_FORMAT),
                        eventType: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        eventTime: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a StopAuditEvent', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
