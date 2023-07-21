/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ChildSubscriptionService } from 'app/entities/child-subscription/child-subscription.service';
import { IChildSubscription, ChildSubscription, StatusType } from 'app/shared/model/child-subscription.model';

describe('Service Tests', () => {
    describe('ChildSubscription Service', () => {
        let injector: TestBed;
        let service: ChildSubscriptionService;
        let httpMock: HttpTestingController;
        let elemDefault: IChildSubscription;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(ChildSubscriptionService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new ChildSubscription(0, StatusType.NEW, false, currentDate, currentDate, 'AAAAAAA');
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        subscriptionDate: currentDate.format(DATE_TIME_FORMAT),
                        activationDate: currentDate.format(DATE_TIME_FORMAT)
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

            it('should create a ChildSubscription', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        subscriptionDate: currentDate.format(DATE_TIME_FORMAT),
                        activationDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        subscriptionDate: currentDate,
                        activationDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new ChildSubscription(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a ChildSubscription', async () => {
                const returnedFromService = Object.assign(
                    {
                        statusType: 'BBBBBB',
                        famility: true,
                        subscriptionDate: currentDate.format(DATE_TIME_FORMAT),
                        activationDate: currentDate.format(DATE_TIME_FORMAT),
                        comments: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        subscriptionDate: currentDate,
                        activationDate: currentDate
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

            it('should return a list of ChildSubscription', async () => {
                const returnedFromService = Object.assign(
                    {
                        statusType: 'BBBBBB',
                        famility: true,
                        subscriptionDate: currentDate.format(DATE_TIME_FORMAT),
                        activationDate: currentDate.format(DATE_TIME_FORMAT),
                        comments: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        subscriptionDate: currentDate,
                        activationDate: currentDate
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

            it('should delete a ChildSubscription', async () => {
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
