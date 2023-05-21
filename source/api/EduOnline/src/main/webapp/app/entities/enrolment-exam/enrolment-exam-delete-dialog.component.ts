import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEnrolmentExam } from 'app/shared/model/enrolment-exam.model';
import { EnrolmentExamService } from './enrolment-exam.service';

@Component({
  templateUrl: './enrolment-exam-delete-dialog.component.html',
})
export class EnrolmentExamDeleteDialogComponent {
  enrolmentExam?: IEnrolmentExam;

  constructor(
    protected enrolmentExamService: EnrolmentExamService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.enrolmentExamService.delete(id).subscribe(() => {
      this.eventManager.broadcast('enrolmentExamListModification');
      this.activeModal.close();
    });
  }
}
