import { IVisit } from 'app/shared/model/visit.model';

export interface IDoctor {
  id?: number;
  fullname?: string;
  phone?: string;
  visits?: IVisit[];
}

export class Doctor implements IDoctor {
  constructor(public id?: number, public fullname?: string, public phone?: string, public visits?: IVisit[]) {}
}
