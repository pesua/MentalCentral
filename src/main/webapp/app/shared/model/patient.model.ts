import { Moment } from 'moment';
import { IVisit } from 'app/shared/model/visit.model';
import { IHistory } from 'app/shared/model/history.model';

export interface IPatient {
  id?: number;
  fullname?: string;
  dateBirthday?: Moment;
  address?: string;
  phone?: string;
  diagnosis?: number;
  visits?: IVisit[];
  histories?: IHistory[];
}

export class Patient implements IPatient {
  constructor(
    public id?: number,
    public fullname?: string,
    public dateBirthday?: Moment,
    public address?: string,
    public phone?: string,
    public diagnosis?: number,
    public visits?: IVisit[],
    public histories?: IHistory[]
  ) {}
}
