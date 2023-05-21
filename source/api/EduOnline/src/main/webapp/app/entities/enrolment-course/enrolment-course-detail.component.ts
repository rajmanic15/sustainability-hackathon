import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEnrolmentCourse } from 'app/shared/model/enrolment-course.model';

@Component({
  selector: 'jhi-enrolment-course-detail',
  templateUrl: './enrolment-course-detail.component.html',
})
export class EnrolmentCourseDetailComponent implements OnInit {
  enrolmentCourse: IEnrolmentCourse | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enrolmentCourse }) => (this.enrolmentCourse = enrolmentCourse));
  }

  previousState(): void {
    window.history.back();
  }
}
