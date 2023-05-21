import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IEnrolmentExam, EnrolmentExam } from 'app/shared/model/enrolment-exam.model';
import { EnrolmentExamService } from './enrolment-exam.service';
import { IStudent } from 'app/shared/model/student.model';
import { StudentService } from 'app/entities/student/student.service';
import { ICourse } from 'app/shared/model/course.model';
import { CourseService } from 'app/entities/course/course.service';

type SelectableEntity = IStudent | ICourse;

@Component({
  selector: 'jhi-enrolment-exam-update',
  templateUrl: './enrolment-exam-update.component.html',
})
export class EnrolmentExamUpdateComponent implements OnInit {
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
    protected enrolmentExamService: EnrolmentExamService,
    protected studentService: StudentService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enrolmentExam }) => {
      this.updateForm(enrolmentExam);

      this.studentService
        .query({ filter: 'enrolmentexam-is-null' })
        .pipe(
          map((res: HttpResponse<IStudent[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IStudent[]) => {
          if (!enrolmentExam.student || !enrolmentExam.student.id) {
            this.students = resBody;
          } else {
            this.studentService
              .find(enrolmentExam.student.id)
              .pipe(
                map((subRes: HttpResponse<IStudent>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IStudent[]) => (this.students = concatRes));
          }
        });

      this.courseService
        .query({ filter: 'enrolmentexam-is-null' })
        .pipe(
          map((res: HttpResponse<ICourse[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ICourse[]) => {
          if (!enrolmentExam.course || !enrolmentExam.course.id) {
            this.courses = resBody;
          } else {
            this.courseService
              .find(enrolmentExam.course.id)
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

  updateForm(enrolmentExam: IEnrolmentExam): void {
    this.editForm.patchValue({
      id: enrolmentExam.id,
      status: enrolmentExam.status,
      enrolmentDate: enrolmentExam.enrolmentDate,
      student: enrolmentExam.student,
      course: enrolmentExam.course,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enrolmentExam = this.createFromForm();
    if (enrolmentExam.id !== undefined) {
      this.subscribeToSaveResponse(this.enrolmentExamService.update(enrolmentExam));
    } else {
      this.subscribeToSaveResponse(this.enrolmentExamService.create(enrolmentExam));
    }
  }

  private createFromForm(): IEnrolmentExam {
    return {
      ...new EnrolmentExam(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      enrolmentDate: this.editForm.get(['enrolmentDate'])!.value,
      student: this.editForm.get(['student'])!.value,
      course: this.editForm.get(['course'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnrolmentExam>>): void {
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
