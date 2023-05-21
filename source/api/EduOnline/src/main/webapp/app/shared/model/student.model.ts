import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IStudent {
  id?: number;
  age?: number;
  grade?: number;
  dateOfBirth?: Moment;
  parentEmail?: string;
  internalUser?: IUser;
}

export class Student implements IStudent {
  constructor(
    public id?: number,
    public age?: number,
    public grade?: number,
    public dateOfBirth?: Moment,
    public parentEmail?: string,
    public internalUser?: IUser
  ) {}
}
