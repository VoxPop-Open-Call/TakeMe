/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ItineraryService } from 'app/entities/itinerary/itinerary.service';
import { IItinerary, Itinerary, ItineraryStatusType } from 'app/shared/model/itinerary.model';
import { Location } from 'app/shared/model/location.model';

describe('Service Tests', () => {
    describe('Itinerary Service', () => {
        let injector: TestBed;
        let service: ItineraryService;
        let httpMock: HttpTestingController;
        let elemDefault: IItinerary;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(ItineraryService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Itinerary(
                0,
                'AAAAAAA',
                currentDate,
                currentDate,
                currentDate,
                new Location(),
                ItineraryStatusType.READY_TO_START,
                0
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        effectiveStartTime: currentDate.format(DATE_TIME_FORMAT),
                        effectiveEndTime: currentDate.format(DATE_TIME_FORMAT)
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

            it('should create a Itinerary', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        effectiveStartTime: currentDate.format(DATE_TIME_FORMAT),
                        effectiveEndTime: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        effectiveStartTime: currentDate,
                        effectiveEndTime: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Itinerary(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Itinerary', async () => {
                const returnedFromService = Object.assign(
                    {
                        name: 'BBBBBB',
                        estimatedStartTime: 'BBBBBB',
                        effectiveStartTime: currentDate.format(DATE_TIME_FORMAT),
                        effectiveEndTime: currentDate.format(DATE_TIME_FORMAT),
                        itineraryStatusType: 'BBBBBB',
                        estimatedKM: 1
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        effectiveStartTime: currentDate,
                        effectiveEndTime: currentDate
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

            it('should return a list of Itinerary', async () => {
                const returnedFromService = Object.assign(
                    {
                        name: 'BBBBBB',
                        estimatedStartTime: 'BBBBBB',
                        effectiveStartTime: currentDate.format(DATE_TIME_FORMAT),
                        effectiveEndTime: currentDate.format(DATE_TIME_FORMAT),
                        itineraryStatusType: 'BBBBBB',
                        estimatedKM: 1
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        effectiveStartTime: currentDate,
                        effectiveEndTime: currentDate
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

            it('should delete a Itinerary', async () => {
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
