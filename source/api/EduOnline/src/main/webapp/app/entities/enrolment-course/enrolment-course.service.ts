import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IEnrolmentCourse } from 'app/shared/model/enrolment-course.model';

type EntityResponseType = HttpResponse<IEnrolmentCourse>;
type EntityArrayResponseType = HttpResponse<IEnrolmentCourse[]>;

@Injectable({ providedIn: 'root' })
export class EnrolmentCourseService {
  public resourceUrl = SERVER_API_URL + 'api/enrolment-courses';

  constructor(protected http: HttpClient) {}

  create(enrolmentCourse: IEnrolmentCourse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(enrolmentCourse);
    return this.http
      .post<IEnrolmentCourse>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(enrolmentCourse: IEnrolmentCourse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(enrolmentCourse);
    return this.http
      .put<IEnrolmentCourse>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEnrolmentCourse>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEnrolmentCourse[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(enrolmentCourse: IEnrolmentCourse): IEnrolmentCourse {
    const copy: IEnrolmentCourse = Object.assign({}, enrolmentCourse, {
      enrolmentDate:
        enrolmentCourse.enrolmentDate && enrolmentCourse.enrolmentDate.isValid()
          ? enrolmentCourse.enrolmentDate.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.enrolmentDate = res.body.enrolmentDate ? moment(res.body.enrolmentDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((enrolmentCourse: IEnrolmentCourse) => {
        enrolmentCourse.enrolmentDate = enrolmentCourse.enrolmentDate ? moment(enrolmentCourse.enrolmentDate) : undefined;
      });
    }
    return res;
  }
}
