import { Pipe, PipeTransform } from '@angular/core';
import { IPatient } from '../../shared/model/patient.model';

@Pipe({
  name: 'filterFields',
})
export class FilterFieldsPipe implements PipeTransform {
  transform(items: IPatient[], search: any): IPatient[] {
    if (!items) return [];
    if (!search) return items;

    search = search.toLocaleLowerCase();

    return items.filter(
      item =>
        item.fullName!.toLocaleLowerCase().includes(search) ||
        item.phoneNumber!.toLocaleLowerCase().includes(search) ||
        item.address!.toLocaleLowerCase().includes(search)
    );
  }
}
