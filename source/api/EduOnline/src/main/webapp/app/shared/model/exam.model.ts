export interface IExam {
  id?: number;
  name?: string;
  description?: string;
}

export class Exam implements IExam {
  constructor(public id?: number, public name?: string, public description?: string) {}
}
