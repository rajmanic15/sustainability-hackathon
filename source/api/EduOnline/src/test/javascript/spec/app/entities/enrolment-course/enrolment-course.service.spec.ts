import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { EnrolmentCourseService } from 'app/entities/enrolment-course/enrolment-course.service';
import { IEnrolmentCourse, EnrolmentCourse } from 'app/shared/model/enrolment-course.model';

describe('Service Tests', () => {
  describe('EnrolmentCourse Service', () => {
    let injector: TestBed;
    let service: EnrolmentCourseService;
    let httpMock: HttpTestingController;
    let elemDefault: IEnrolmentCourse;
    let expectedResult: IEnrolmentCourse | IEnrolmentCourse[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(EnrolmentCourseService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new EnrolmentCourse(0, 'AAAAAAA', currentDate);
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

      it('should create a EnrolmentCourse', () => {
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

        service.create(new EnrolmentCourse()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a EnrolmentCourse', () => {
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

      it('should return a list of EnrolmentCourse', () => {
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

      it('should delete a EnrolmentCourse', () => {
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
