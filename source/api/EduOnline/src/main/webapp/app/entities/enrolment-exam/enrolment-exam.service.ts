import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IEnrolmentExam } from 'app/shared/model/enrolment-exam.model';

type EntityResponseType = HttpResponse<IEnrolmentExam>;
type EntityArrayResponseType = HttpResponse<IEnrolmentExam[]>;

@Injectable({ providedIn: 'root' })
export class EnrolmentExamService {
  public resourceUrl = SERVER_API_URL + 'api/enrolment-exams';

  constructor(protected http: HttpClient) {}

  create(enrolmentExam: IEnrolmentExam): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(enrolmentExam);
    return this.http
      .post<IEnrolmentExam>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(enrolmentExam: IEnrolmentExam): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(enrolmentExam);
    return this.http
      .put<IEnrolmentExam>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEnrolmentExam>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEnrolmentExam[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(enrolmentExam: IEnrolmentExam): IEnrolmentExam {
    const copy: IEnrolmentExam = Object.assign({}, enrolmentExam, {
      enrolmentDate:
        enrolmentExam.enrolmentDate && enrolmentExam.enrolmentDate.isValid() ? enrolmentExam.enrolmentDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((enrolmentExam: IEnrolmentExam) => {
        enrolmentExam.enrolmentDate = enrolmentExam.enrolmentDate ? moment(enrolmentExam.enrolmentDate) : undefined;
      });
    }
    return res;
  }
}
