import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'student',
        loadChildren: () => import('./student/student.module').then(m => m.EducationStudentModule),
      },
      {
        path: 'course',
        loadChildren: () => import('./course/course.module').then(m => m.EducationCourseModule),
      },
      {
        path: 'exam',
        loadChildren: () => import('./exam/exam.module').then(m => m.EducationExamModule),
      },
      {
        path: 'enrolment-course',
        loadChildren: () => import('./enrolment-course/enrolment-course.module').then(m => m.EducationEnrolmentCourseModule),
      },
      {
        path: 'enrolment-exam',
        loadChildren: () => import('./enrolment-exam/enrolment-exam.module').then(m => m.EducationEnrolmentExamModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EducationEntityModule {}
