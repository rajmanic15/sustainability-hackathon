import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EducationTestModule } from '../../../test.module';
import { EnrolmentExamDetailComponent } from 'app/entities/enrolment-exam/enrolment-exam-detail.component';
import { EnrolmentExam } from 'app/shared/model/enrolment-exam.model';

describe('Component Tests', () => {
  describe('EnrolmentExam Management Detail Component', () => {
    let comp: EnrolmentExamDetailComponent;
    let fixture: ComponentFixture<EnrolmentExamDetailComponent>;
    const route = ({ data: of({ enrolmentExam: new EnrolmentExam(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EducationTestModule],
        declarations: [EnrolmentExamDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(EnrolmentExamDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EnrolmentExamDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load enrolmentExam on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.enrolmentExam).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
