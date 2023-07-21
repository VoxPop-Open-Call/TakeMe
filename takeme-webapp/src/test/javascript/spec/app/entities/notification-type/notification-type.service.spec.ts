// commented for remote build

// /* tslint:disable max-line-length */
// import { TestBed, getTestBed } from '@angular/core/testing';
// import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
// import { HttpClient, HttpResponse } from '@angular/common/http';
// import { of } from 'rxjs';
// import { take, map } from 'rxjs/operators';
// import { NotificationTypeService } from 'app/entities/notification-type/notification-type.service';
// import { INotificationType, NotificationType, NotificationTypeType } from 'app/shared/model/notification-type.model';
//
// describe('Service Tests', () => {
//     describe('NotificationType Service', () => {
//         let injector: TestBed;
//         let service: NotificationTypeService;
//         let httpMock: HttpTestingController;
//         let elemDefault: INotificationType;
//         beforeEach(() => {
//             TestBed.configureTestingModule({
//                 imports: [HttpClientTestingModule]
//             });
//             injector = getTestBed();
//             service = injector.get(NotificationTypeService);
//             httpMock = injector.get(HttpTestingController);
//
//             elemDefault = new NotificationType(0, NotificationTypeType.GEOFENCE_SCHOOL, false, 'AAAAAAA', false);
//         });
//
//         describe('Service methods', async () => {
//             it('should find an element', async () => {
//                 const returnedFromService = Object.assign({}, elemDefault);
//                 service
//                     .find(123)
//                     .pipe(take(1))
//                     .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));
//
//                 const req = httpMock.expectOne({ method: 'GET' });
//                 req.flush(JSON.stringify(returnedFromService));
//             });
//
//             it('should create a NotificationType', async () => {
//                 const returnedFromService = Object.assign(
//                     {
//                         id: 0
//                     },
//                     elemDefault
//                 );
//                 const expected = Object.assign({}, returnedFromService);
//                 service
//                     .create(new NotificationType(null))
//                     .pipe(take(1))
//                     .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
//                 const req = httpMock.expectOne({ method: 'POST' });
//                 req.flush(JSON.stringify(returnedFromService));
//             });
//
//             it('should update a NotificationType', async () => {
//                 const returnedFromService = Object.assign(
//                     {
//                         type: 'BBBBBB',
//                         active: true,
//                         title: 'BBBBBB',
//                         userConfigurable: true
//                     },
//                     elemDefault
//                 );
//
//                 const expected = Object.assign({}, returnedFromService);
//                 service
//                     .update(expected)
//                     .pipe(take(1))
//                     .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
//                 const req = httpMock.expectOne({ method: 'PUT' });
//                 req.flush(JSON.stringify(returnedFromService));
//             });
//
//             it('should return a list of NotificationType', async () => {
//                 const returnedFromService = Object.assign(
//                     {
//                         type: 'BBBBBB',
//                         active: true,
//                         title: 'BBBBBB',
//                         userConfigurable: true
//                     },
//                     elemDefault
//                 );
//                 const expected = Object.assign({}, returnedFromService);
//                 service
//                     .query(expected)
//                     .pipe(
//                         take(1),
//                         map(resp => resp.body)
//                     )
//                     .subscribe(body => expect(body).toContainEqual(expected));
//                 const req = httpMock.expectOne({ method: 'GET' });
//                 req.flush(JSON.stringify([returnedFromService]));
//                 httpMock.verify();
//             });
//
//             it('should delete a NotificationType', async () => {
//                 const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));
//
//                 const req = httpMock.expectOne({ method: 'DELETE' });
//                 req.flush({ status: 200 });
//             });
//         });
//
//         afterEach(() => {
//             httpMock.verify();
//         });
//     });
// });
