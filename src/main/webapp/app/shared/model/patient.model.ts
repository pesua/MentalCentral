import { Moment } from 'moment';

export interface IPatient {
  id?: number;
  fullname?: string;
  dateBirthday?: Moment;
  address?: string;
  phoneNumber?: string;
  diagnosis?: number;
}

export class Patient implements IPatient {
  constructor(
    public id?: number,
    public fullname?: string,
    public dateBirthday?: Moment,
    public address?: string,
    public phoneNumber?: string,
    public diagnosis?: number
  ) {}
}
