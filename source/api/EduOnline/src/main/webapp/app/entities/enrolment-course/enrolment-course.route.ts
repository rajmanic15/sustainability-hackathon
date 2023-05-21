import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEnrolmentCourse, EnrolmentCourse } from 'app/shared/model/enrolment-course.model';
import { EnrolmentCourseService } from './enrolment-course.service';
import { EnrolmentCourseComponent } from './enrolment-course.component';
import { EnrolmentCourseDetailComponent } from './enrolment-course-detail.component';
import { EnrolmentCourseUpdateComponent } from './enrolment-course-update.component';

@Injectable({ providedIn: 'root' })
export class EnrolmentCourseResolve implements Resolve<IEnrolmentCourse> {
  constructor(private service: EnrolmentCourseService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEnrolmentCourse> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((enrolmentCourse: HttpResponse<EnrolmentCourse>) => {
          if (enrolmentCourse.body) {
            return of(enrolmentCourse.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EnrolmentCourse());
  }
}

export const enrolmentCourseRoute: Routes = [
  {
    path: '',
    component: EnrolmentCourseComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'educationApp.enrolmentCourse.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EnrolmentCourseDetailComponent,
    resolve: {
      enrolmentCourse: EnrolmentCourseResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'educationApp.enrolmentCourse.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EnrolmentCourseUpdateComponent,
    resolve: {
      enrolmentCourse: EnrolmentCourseResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'educationApp.enrolmentCourse.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EnrolmentCourseUpdateComponent,
    resolve: {
      enrolmentCourse: EnrolmentCourseResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'educationApp.enrolmentCourse.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
