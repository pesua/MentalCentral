import { Moment } from 'moment';

export interface IHistory {
  id?: number;
  dateRecord?: Moment;
  type?: string;
  info?: string;
  patientFullname?: string;
  patientId?: number;
}

export class History implements IHistory {
  constructor(
    public id?: number,
    public dateRecord?: Moment,
    public type?: string,
    public info?: string,
    public patientFullname?: string,
    public patientId?: number
  ) {}
}
