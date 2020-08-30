import { Pipe, PipeTransform } from '@angular/core';
import { IVisit } from '../../shared/model/visit.model';

@Pipe({
  name: 'filterVisitsByPatientId',
})
export class FilterVisitsByPatientIdPipe implements PipeTransform {
  private date!: Date;
  transform(items: IVisit[], id: number): IVisit[] {
    this.date = new Date();
    if (!items) {
      return [];
    }
    if (!id) {
      return items;
    }
    return items.filter(item => item.patient!.id === id && item.time!.date() <= this.date.getDate());
  }
}
