import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { EducationTestModule } from '../../../test.module';
import { EnrolmentCourseUpdateComponent } from 'app/entities/enrolment-course/enrolment-course-update.component';
import { EnrolmentCourseService } from 'app/entities/enrolment-course/enrolment-course.service';
import { EnrolmentCourse } from 'app/shared/model/enrolment-course.model';

describe('Component Tests', () => {
  describe('EnrolmentCourse Management Update Component', () => {
    let comp: EnrolmentCourseUpdateComponent;
    let fixture: ComponentFixture<EnrolmentCourseUpdateComponent>;
    let service: EnrolmentCourseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EducationTestModule],
        declarations: [EnrolmentCourseUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(EnrolmentCourseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EnrolmentCourseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EnrolmentCourseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new EnrolmentCourse(123);
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
        const entity = new EnrolmentCourse();
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
