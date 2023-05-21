import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IStudent, Student } from 'app/shared/model/student.model';
import { StudentService } from './student.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-student-update',
  templateUrl: './student-update.component.html',
})
export class StudentUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    age: [null, [Validators.min(10), Validators.max(100)]],
    grade: [null, [Validators.required, Validators.min(1), Validators.max(12)]],
    dateOfBirth: [null, [Validators.required]],
    parentEmail: [null, [Validators.required, Validators.minLength(5), Validators.maxLength(254)]],
    internalUser: [],
  });

  constructor(
    protected studentService: StudentService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ student }) => {
      if (!student.id) {
        const today = moment().startOf('day');
        student.dateOfBirth = today;
      }

      this.updateForm(student);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(student: IStudent): void {
    this.editForm.patchValue({
      id: student.id,
      age: student.age,
      grade: student.grade,
      dateOfBirth: student.dateOfBirth ? student.dateOfBirth.format(DATE_TIME_FORMAT) : null,
      parentEmail: student.parentEmail,
      internalUser: student.internalUser,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const student = this.createFromForm();
    if (student.id !== undefined) {
      this.subscribeToSaveResponse(this.studentService.update(student));
    } else {
      this.subscribeToSaveResponse(this.studentService.create(student));
    }
  }

  private createFromForm(): IStudent {
    return {
      ...new Student(),
      id: this.editForm.get(['id'])!.value,
      age: this.editForm.get(['age'])!.value,
      grade: this.editForm.get(['grade'])!.value,
      dateOfBirth: this.editForm.get(['dateOfBirth'])!.value
        ? moment(this.editForm.get(['dateOfBirth'])!.value, DATE_TIME_FORMAT)
        : undefined,
      parentEmail: this.editForm.get(['parentEmail'])!.value,
      internalUser: this.editForm.get(['internalUser'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudent>>): void {
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

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
