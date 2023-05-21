import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IEnrolmentCourse, EnrolmentCourse } from 'app/shared/model/enrolment-course.model';
import { EnrolmentCourseService } from './enrolment-course.service';
import { IStudent } from 'app/shared/model/student.model';
import { StudentService } from 'app/entities/student/student.service';
import { ICourse } from 'app/shared/model/course.model';
import { CourseService } from 'app/entities/course/course.service';

type SelectableEntity = IStudent | ICourse;

@Component({
  selector: 'jhi-enrolment-course-update',
  templateUrl: './enrolment-course-update.component.html',
})
export class EnrolmentCourseUpdateComponent implements OnInit {
  isSaving = false;
  students: IStudent[] = [];
  courses: ICourse[] = [];
  enrolmentDateDp: any;

  editForm = this.fb.group({
    id: [],
    status: [],
    enrolmentDate: [null, [Validators.required]],
    student: [],
    course: [],
  });

  constructor(
    protected enrolmentCourseService: EnrolmentCourseService,
    protected studentService: StudentService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enrolmentCourse }) => {
      this.updateForm(enrolmentCourse);

      this.studentService
        .query({ filter: 'enrolmentcourse-is-null' })
        .pipe(
          map((res: HttpResponse<IStudent[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IStudent[]) => {
          if (!enrolmentCourse.student || !enrolmentCourse.student.id) {
            this.students = resBody;
          } else {
            this.studentService
              .find(enrolmentCourse.student.id)
              .pipe(
                map((subRes: HttpResponse<IStudent>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IStudent[]) => (this.students = concatRes));
          }
        });

      this.courseService
        .query({ filter: 'enrolmentcourse-is-null' })
        .pipe(
          map((res: HttpResponse<ICourse[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ICourse[]) => {
          if (!enrolmentCourse.course || !enrolmentCourse.course.id) {
            this.courses = resBody;
          } else {
            this.courseService
              .find(enrolmentCourse.course.id)
              .pipe(
                map((subRes: HttpResponse<ICourse>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ICourse[]) => (this.courses = concatRes));
          }
        });
    });
  }

  updateForm(enrolmentCourse: IEnrolmentCourse): void {
    this.editForm.patchValue({
      id: enrolmentCourse.id,
      status: enrolmentCourse.status,
      enrolmentDate: enrolmentCourse.enrolmentDate,
      student: enrolmentCourse.student,
      course: enrolmentCourse.course,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enrolmentCourse = this.createFromForm();
    if (enrolmentCourse.id !== undefined) {
      this.subscribeToSaveResponse(this.enrolmentCourseService.update(enrolmentCourse));
    } else {
      this.subscribeToSaveResponse(this.enrolmentCourseService.create(enrolmentCourse));
    }
  }

  private createFromForm(): IEnrolmentCourse {
    return {
      ...new EnrolmentCourse(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      enrolmentDate: this.editForm.get(['enrolmentDate'])!.value,
      student: this.editForm.get(['student'])!.value,
      course: this.editForm.get(['course'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnrolmentCourse>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
