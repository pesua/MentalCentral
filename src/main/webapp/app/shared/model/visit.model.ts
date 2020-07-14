import { Moment } from 'moment';

export interface IVisit {
  id?: number;
  type?: string;
  time?: Moment;
  teraphy?: string;
  doctorFullname?: string;
  doctorId?: number;
  patientFullname?: string;
  patientId?: number;
}

export class Visit implements IVisit {
  constructor(
    public id?: number,
    public type?: string,
    public time?: Moment,
    public teraphy?: string,
    public doctorFullname?: string,
    public doctorId?: number,
    public patientFullname?: string,
    public patientId?: number
  ) {}
}
