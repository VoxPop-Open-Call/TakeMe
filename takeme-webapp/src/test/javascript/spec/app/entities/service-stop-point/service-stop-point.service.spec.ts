/* tslint:disable max-line-length */
import { getTestBed, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { map, take } from 'rxjs/operators';
import moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ServiceStopPointService } from 'app/entities/service-stop-point/service-stop-point.service';
import { IServiceStopPoint, ServiceStopPoint, StatusType, StopPointType } from 'app/shared/model/service-stop-point.model';

import { Location } from 'app/shared/model/location.model';
import { ServiceStopPointFrequency } from 'app/shared/model/service-stop-point-frequency.model';
import { ServiceStopPointDaysOfWeek } from 'app/shared/model/service-stop-point-days-of-week.model';

describe('Service Tests', () => {
    describe('ServiceStopPoint Service', () => {
        let injector: TestBed;
        let service: ServiceStopPointService;
        let httpMock: HttpTestingController;
        let elemDefault: IServiceStopPoint;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(ServiceStopPointService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new ServiceStopPoint(
                0,
                StopPointType.COLLECTION,
                '10',
                '10',
                currentDate,
                StatusType.ACTIVE,
                new Location(),
                new ServiceStopPointFrequency(),
                [new ServiceStopPointDaysOfWeek()]
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        startFrequencyDate: currentDate.format(DATE_TIME_FORMAT)
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

            it('should create a ServiceStopPoint', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        startFrequencyDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        startFrequencyDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new ServiceStopPoint(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a ServiceStopPoint', async () => {
                const returnedFromService = Object.assign(
                    {
                        stopPointType: 'BBBBBB',
                        startHour: 'BBBBBB',
                        combinedTime: 'BBBBBB',
                        startFrequencyDate: currentDate.format(DATE_TIME_FORMAT),
                        statusType: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        startFrequencyDate: currentDate
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

            it('should return a list of ServiceStopPoint', async () => {
                const returnedFromService = Object.assign(
                    {
                        stopPointType: 'BBBBBB',
                        startHour: 'BBBBBB',
                        combinedTime: 'BBBBBB',
                        startFrequencyDate: currentDate.format(DATE_TIME_FORMAT),
                        statusType: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        startFrequencyDate: currentDate
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

            it('should delete a ServiceStopPoint', async () => {
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
