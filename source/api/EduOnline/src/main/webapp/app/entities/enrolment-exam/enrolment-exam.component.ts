import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEnrolmentExam } from 'app/shared/model/enrolment-exam.model';
import { EnrolmentExamService } from './enrolment-exam.service';
import { EnrolmentExamDeleteDialogComponent } from './enrolment-exam-delete-dialog.component';

@Component({
  selector: 'jhi-enrolment-exam',
  templateUrl: './enrolment-exam.component.html',
})
export class EnrolmentExamComponent implements OnInit, OnDestroy {
  enrolmentExams?: IEnrolmentExam[];
  eventSubscriber?: Subscription;

  constructor(
    protected enrolmentExamService: EnrolmentExamService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.enrolmentExamService.query().subscribe((res: HttpResponse<IEnrolmentExam[]>) => (this.enrolmentExams = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEnrolmentExams();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEnrolmentExam): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEnrolmentExams(): void {
    this.eventSubscriber = this.eventManager.subscribe('enrolmentExamListModification', () => this.loadAll());
  }

  delete(enrolmentExam: IEnrolmentExam): void {
    const modalRef = this.modalService.open(EnrolmentExamDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.enrolmentExam = enrolmentExam;
  }
}
