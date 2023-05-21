import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEnrolmentExam } from 'app/shared/model/enrolment-exam.model';

@Component({
  selector: 'jhi-enrolment-exam-detail',
  templateUrl: './enrolment-exam-detail.component.html',
})
export class EnrolmentExamDetailComponent implements OnInit {
  enrolmentExam: IEnrolmentExam | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enrolmentExam }) => (this.enrolmentExam = enrolmentExam));
  }

  previousState(): void {
    window.history.back();
  }
}
