import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LearnerContentComponent } from './learner-content.component';

describe('LearnerContentComponent', () => {
  let component: LearnerContentComponent;
  let fixture: ComponentFixture<LearnerContentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LearnerContentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LearnerContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
