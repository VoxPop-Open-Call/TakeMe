/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import { OrganizationService } from 'app/entities/organization/organization.service';
import { IOrganization, Organization, OrganizationType, StatusType } from 'app/shared/model/organization.model';

describe('Service Tests', () => {
    describe('Organization Service', () => {
        let injector: TestBed;
        let service: OrganizationService;
        let httpMock: HttpTestingController;
        let elemDefault: IOrganization;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(OrganizationService);
            httpMock = injector.get(HttpTestingController);

            elemDefault = new Organization(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', OrganizationType.BUS_COMPANY, StatusType.NEW);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign({}, elemDefault);
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Organization', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .create(new Organization(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Organization', async () => {
                const returnedFromService = Object.assign(
                    {
                        name: 'BBBBBB',
                        nifCountry: 'BBBBBB',
                        nifNumber: 'BBBBBB',
                        organizationType: 'BBBBBB',
                        statusType: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign({}, returnedFromService);
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Organization', async () => {
                const returnedFromService = Object.assign(
                    {
                        name: 'BBBBBB',
                        nifCountry: 'BBBBBB',
                        nifNumber: 'BBBBBB',
                        organizationType: 'BBBBBB',
                        statusType: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
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

            it('should delete a Organization', async () => {
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
