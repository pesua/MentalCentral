import { Moment } from 'moment';

export interface IHistory {
  id?: number;
  recordDate?: Moment;
  type?: string;
  info?: string;
  patientFullName?: string;
  patientId?: number;
}

export class History implements IHistory {
  constructor(
    public id?: number,
    public recordDate?: Moment,
    public type?: string,
    public info?: string,
    public patientFullName?: string,
    public patientId?: number
  ) {}
}
