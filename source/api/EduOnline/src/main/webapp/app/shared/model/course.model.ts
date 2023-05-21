import { Moment } from 'moment';

export interface ICourse {
  id?: number;
  name?: string;
  description?: string;
  startDate?: Moment;
  endDate?: Moment;
  isActive?: boolean;
}

export class Course implements ICourse {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public startDate?: Moment,
    public endDate?: Moment,
    public isActive?: boolean
  ) {
    this.isActive = this.isActive || false;
  }
}
