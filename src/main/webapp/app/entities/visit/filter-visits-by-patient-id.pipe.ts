import { Pipe, PipeTransform } from '@angular/core';
import { IVisit } from '../../shared/model/visit.model';
import * as moment from 'moment';
import { Moment } from 'moment';

@Pipe({
  name: 'filterVisitsByPatientId',
})
export class FilterVisitsByPatientIdPipe implements PipeTransform {
  private date!: Moment;
  transform(items: IVisit[], id: number): IVisit[] {
    this.date = moment();
    if (!items) {
      return [];
    }
    if (!id) {
      return items;
    }
    return items
      .filter(item => item.patient!.id === id && this.date.diff(item.time, 'hour') >= 2)
      .sort((a, b) => a.time!.valueOf() - b.time!.valueOf());
  }
}
