import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IPatient } from 'app/shared/model/patient.model';

export interface IVisit {
  id?: number;
  type?: string;
  time?: Moment;
  therapy?: string;
  user?: IUser;
  patient?: IPatient;
}

export class Visit implements IVisit {
  constructor(
    public id?: number,
    public type?: string,
    public time?: Moment,
    public therapy?: string,
    public user?: IUser,
    public patient?: IPatient
  ) {}
}
