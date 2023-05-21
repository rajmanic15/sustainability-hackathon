import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEnrolmentCourse } from 'app/shared/model/enrolment-course.model';
import { EnrolmentCourseService } from './enrolment-course.service';
import { EnrolmentCourseDeleteDialogComponent } from './enrolment-course-delete-dialog.component';

@Component({
  selector: 'jhi-enrolment-course',
  templateUrl: './enrolment-course.component.html',
})
export class EnrolmentCourseComponent implements OnInit, OnDestroy {
  enrolmentCourses?: IEnrolmentCourse[];
  eventSubscriber?: Subscription;

  constructor(
    protected enrolmentCourseService: EnrolmentCourseService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.enrolmentCourseService.query().subscribe((res: HttpResponse<IEnrolmentCourse[]>) => (this.enrolmentCourses = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEnrolmentCourses();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEnrolmentCourse): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEnrolmentCourses(): void {
    this.eventSubscriber = this.eventManager.subscribe('enrolmentCourseListModification', () => this.loadAll());
  }

  delete(enrolmentCourse: IEnrolmentCourse): void {
    const modalRef = this.modalService.open(EnrolmentCourseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.enrolmentCourse = enrolmentCourse;
  }
}
