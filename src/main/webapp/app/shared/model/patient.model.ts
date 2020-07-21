import { Moment } from 'moment';
import { IVisit } from 'app/shared/model/visit.model';

export interface IPatient {
  id?: number;
  fullName?: string;
  birthDate?: Moment;
  address?: string;
  phoneNumber?: string;
  diagnosis?: number;
  visits?: IVisit[];
}

export class Patient implements IPatient {
  constructor(
    public id?: number,
    public fullName?: string,
    public birthDate?: Moment,
    public address?: string,
    public phoneNumber?: string,
    public diagnosis?: number,
    public visits?: IVisit[]
  ) {}
}
