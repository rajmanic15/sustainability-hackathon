import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EducationSharedModule } from 'app/shared/shared.module';
import { EnrolmentCourseComponent } from './enrolment-course.component';
import { EnrolmentCourseDetailComponent } from './enrolment-course-detail.component';
import { EnrolmentCourseUpdateComponent } from './enrolment-course-update.component';
import { EnrolmentCourseDeleteDialogComponent } from './enrolment-course-delete-dialog.component';
import { enrolmentCourseRoute } from './enrolment-course.route';

@NgModule({
  imports: [EducationSharedModule, RouterModule.forChild(enrolmentCourseRoute)],
  declarations: [
    EnrolmentCourseComponent,
    EnrolmentCourseDetailComponent,
    EnrolmentCourseUpdateComponent,
    EnrolmentCourseDeleteDialogComponent,
  ],
  entryComponents: [EnrolmentCourseDeleteDialogComponent],
})
export class EducationEnrolmentCourseModule {}
