import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEnrolmentCourse } from 'app/shared/model/enrolment-course.model';
import { EnrolmentCourseService } from './enrolment-course.service';

@Component({
  templateUrl: './enrolment-course-delete-dialog.component.html',
})
export class EnrolmentCourseDeleteDialogComponent {
  enrolmentCourse?: IEnrolmentCourse;

  constructor(
    protected enrolmentCourseService: EnrolmentCourseService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.enrolmentCourseService.delete(id).subscribe(() => {
      this.eventManager.broadcast('enrolmentCourseListModification');
      this.activeModal.close();
    });
  }
}
