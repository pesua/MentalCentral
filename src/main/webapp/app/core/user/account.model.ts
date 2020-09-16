export class Account {
  constructor(
    public activated: boolean,
    public authorities: string[],
    public email: string,
    public firstName: string,
    public langKey: string,
    public lastName: string,
    public middleName: string,
    public phoneNumber: string,
    public degree: string,
    public login: string,
    public imageUrl: string
  ) {}
}
