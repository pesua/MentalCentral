import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { HistoryService } from 'app/entities/history/history.service';
import { IHistory, History } from 'app/shared/model/history.model';

describe('Service Tests', () => {
  describe('History Service', () => {
    let injector: TestBed;
    let service: HistoryService;
    let httpMock: HttpTestingController;
    let elemDefault: IHistory;
    let expectedResult: IHistory | IHistory[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(HistoryService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new History(0, currentDate, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateRecord: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a History', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateRecord: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateRecord: currentDate,
          },
          returnedFromService
        );

        service.create(new History()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a History', () => {
        const returnedFromService = Object.assign(
          {
            dateRecord: currentDate.format(DATE_FORMAT),
            type: 'BBBBBB',
            info: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateRecord: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of History', () => {
        const returnedFromService = Object.assign(
          {
            dateRecord: currentDate.format(DATE_FORMAT),
            type: 'BBBBBB',
            info: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateRecord: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a History', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
