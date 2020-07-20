import { IVisit } from 'app/shared/model/visit.model';

export interface IDoctor {
  id?: number;
  fullName?: string;
  phone?: string;
  visits?: IVisit[];
}

export class Doctor implements IDoctor {
  constructor(public id?: number, public fullName?: string, public phone?: string, public visits?: IVisit[]) {}
}
