import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEnrolmentExam, EnrolmentExam } from 'app/shared/model/enrolment-exam.model';
import { EnrolmentExamService } from './enrolment-exam.service';
import { EnrolmentExamComponent } from './enrolment-exam.component';
import { EnrolmentExamDetailComponent } from './enrolment-exam-detail.component';
import { EnrolmentExamUpdateComponent } from './enrolment-exam-update.component';

@Injectable({ providedIn: 'root' })
export class EnrolmentExamResolve implements Resolve<IEnrolmentExam> {
  constructor(private service: EnrolmentExamService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEnrolmentExam> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((enrolmentExam: HttpResponse<EnrolmentExam>) => {
          if (enrolmentExam.body) {
            return of(enrolmentExam.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EnrolmentExam());
  }
}

export const enrolmentExamRoute: Routes = [
  {
    path: '',
    component: EnrolmentExamComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'educationApp.enrolmentExam.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EnrolmentExamDetailComponent,
    resolve: {
      enrolmentExam: EnrolmentExamResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'educationApp.enrolmentExam.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EnrolmentExamUpdateComponent,
    resolve: {
      enrolmentExam: EnrolmentExamResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'educationApp.enrolmentExam.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EnrolmentExamUpdateComponent,
    resolve: {
      enrolmentExam: EnrolmentExamResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'educationApp.enrolmentExam.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
