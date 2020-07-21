import { Pipe, PipeTransform } from '@angular/core';
import { IPatient } from 'app/shared/model/patient.model';

@Pipe({
  name: 'filterPatient',
})
export class FilterPipe implements PipeTransform {
  transform(items: IPatient[], searchText: string): IPatient[] {
    if (!items) {
      return [];
    }
    if (!searchText) {
      return items;
    }
    searchText = searchText.toLocaleLowerCase();

    return items.filter(it => {
      return it.fullName?.toLocaleLowerCase().includes(searchText);
    });
  }
}
