import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { EducationTestModule } from '../../../test.module';
import { EnrolmentExamUpdateComponent } from 'app/entities/enrolment-exam/enrolment-exam-update.component';
import { EnrolmentExamService } from 'app/entities/enrolment-exam/enrolment-exam.service';
import { EnrolmentExam } from 'app/shared/model/enrolment-exam.model';

describe('Component Tests', () => {
  describe('EnrolmentExam Management Update Component', () => {
    let comp: EnrolmentExamUpdateComponent;
    let fixture: ComponentFixture<EnrolmentExamUpdateComponent>;
    let service: EnrolmentExamService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EducationTestModule],
        declarations: [EnrolmentExamUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(EnrolmentExamUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EnrolmentExamUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EnrolmentExamService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new EnrolmentExam(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new EnrolmentExam();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
