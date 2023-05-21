import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EducationSharedModule } from 'app/shared/shared.module';
import { EnrolmentExamComponent } from './enrolment-exam.component';
import { EnrolmentExamDetailComponent } from './enrolment-exam-detail.component';
import { EnrolmentExamUpdateComponent } from './enrolment-exam-update.component';
import { EnrolmentExamDeleteDialogComponent } from './enrolment-exam-delete-dialog.component';
import { enrolmentExamRoute } from './enrolment-exam.route';

@NgModule({
  imports: [EducationSharedModule, RouterModule.forChild(enrolmentExamRoute)],
  declarations: [EnrolmentExamComponent, EnrolmentExamDetailComponent, EnrolmentExamUpdateComponent, EnrolmentExamDeleteDialogComponent],
  entryComponents: [EnrolmentExamDeleteDialogComponent],
})
export class EducationEnrolmentExamModule {}
