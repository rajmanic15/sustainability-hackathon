import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { EnrolmentExamService } from 'app/entities/enrolment-exam/enrolment-exam.service';
import { IEnrolmentExam, EnrolmentExam } from 'app/shared/model/enrolment-exam.model';

describe('Service Tests', () => {
  describe('EnrolmentExam Service', () => {
    let injector: TestBed;
    let service: EnrolmentExamService;
    let httpMock: HttpTestingController;
    let elemDefault: IEnrolmentExam;
    let expectedResult: IEnrolmentExam | IEnrolmentExam[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(EnrolmentExamService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new EnrolmentExam(0, 'AAAAAAA', currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            enrolmentDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a EnrolmentExam', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            enrolmentDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            enrolmentDate: currentDate,
          },
          returnedFromService
        );

        service.create(new EnrolmentExam()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a EnrolmentExam', () => {
        const returnedFromService = Object.assign(
          {
            status: 'BBBBBB',
            enrolmentDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            enrolmentDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of EnrolmentExam', () => {
        const returnedFromService = Object.assign(
          {
            status: 'BBBBBB',
            enrolmentDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            enrolmentDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a EnrolmentExam', () => {
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
