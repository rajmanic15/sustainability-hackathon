import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreparequestionsComponent } from './preparequestions.component';

describe('PreparequestionsComponent', () => {
  let component: PreparequestionsComponent;
  let fixture: ComponentFixture<PreparequestionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PreparequestionsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PreparequestionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
