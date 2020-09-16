import { IUser } from 'app/core/user/user.model';

export interface IUserExtra {
  id?: number;
  middleName?: string;
  phoneNumber?: string;
  degree?: string;
  user?: IUser;
}

export class UserExtra implements IUserExtra {
  constructor(public id?: number, public middleName?: string, public phoneNumber?: string, public degree?: string, public user?: IUser) {}
}
