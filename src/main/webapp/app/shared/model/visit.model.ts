import { Moment } from 'moment';

export interface IVisit {
  id?: number;
  type?: string;
  time?: Moment;
  therapy?: string;
  doctorFullName?: string;
  doctorId?: number;
  patientFullName?: string;
  patientId?: number;
}

export class Visit implements IVisit {
  constructor(
    public id?: number,
    public type?: string,
    public time?: Moment,
    public therapy?: string,
    public doctorFullName?: string,
    public doctorId?: number,
    public patientFullName?: string,
    public patientId?: number
  ) {}
}
