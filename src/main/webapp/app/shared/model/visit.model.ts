import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IPatient } from 'app/shared/model/patient.model';

export interface IVisit {
  id?: number;
  type?: string;
  time?: Moment;
  therapy?: string;
  user?: IUser;
  fileContentType?: string;
  file?: any;
  fileName?: string;
  patient?: IPatient;
  note?: string;
}

export class Visit implements IVisit {
  constructor(
    public id?: number,
    public type?: string,
    public time?: Moment,
    public therapy?: string,
    public user?: IUser,
    public fileContentType?: string,
    public file?: any,
    public fileName?: string,
    public patient?: IPatient,
    public note?: string
  ) {}
}
