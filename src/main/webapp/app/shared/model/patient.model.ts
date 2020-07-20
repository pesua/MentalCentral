import { Moment } from 'moment';
import { IHistory } from 'app/shared/model/history.model';
import { IVisit } from 'app/shared/model/visit.model';

export interface IPatient {
  id?: number;
  fullName?: string;
  birthdayDate?: Moment;
  address?: string;
  phone?: string;
  diagnosis?: number;
  histories?: IHistory[];
  visits?: IVisit[];
}

export class Patient implements IPatient {
  constructor(
    public id?: number,
    public fullName?: string,
    public birthdayDate?: Moment,
    public address?: string,
    public phone?: string,
    public diagnosis?: number,
    public histories?: IHistory[],
    public visits?: IVisit[]
  ) {}
}
