/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { UserNotificationService } from 'app/entities/user-notification/user-notification.service';
import { IUserNotification, UserNotification } from 'app/shared/model/user-notification.model';

describe('Service Tests', () => {
    describe('UserNotification Service', () => {
        let injector: TestBed;
        let service: UserNotificationService;
        let httpMock: HttpTestingController;
        let elemDefault: IUserNotification;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(UserNotificationService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new UserNotification(0, currentDate, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        sentDate: currentDate.format(DATE_TIME_FORMAT)
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

            it('should create a UserNotification', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        sentDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        sentDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new UserNotification(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a UserNotification', async () => {
                const returnedFromService = Object.assign(
                    {
                        sentDate: currentDate.format(DATE_TIME_FORMAT),
                        title: 'BBBBBB',
                        body: 'BBBBBB',
                        userId: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        sentDate: currentDate
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

            it('should return a list of UserNotification', async () => {
                const returnedFromService = Object.assign(
                    {
                        sentDate: currentDate.format(DATE_TIME_FORMAT),
                        title: 'BBBBBB',
                        body: 'BBBBBB',
                        userId: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        sentDate: currentDate
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

            it('should delete a UserNotification', async () => {
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
