import { Moment } from 'moment';
import { IStudent } from 'app/shared/model/student.model';
import { ICourse } from 'app/shared/model/course.model';

export interface IEnrolmentExam {
  id?: number;
  status?: string;
  enrolmentDate?: Moment;
  student?: IStudent;
  course?: ICourse;
}

export class EnrolmentExam implements IEnrolmentExam {
  constructor(
    public id?: number,
    public status?: string,
    public enrolmentDate?: Moment,
    public student?: IStudent,
    public course?: ICourse
  ) {}
}
