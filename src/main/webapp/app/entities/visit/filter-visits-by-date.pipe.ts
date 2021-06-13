import { Pipe, PipeTransform } from '@angular/core';
import { Moment } from 'moment';
import { IVisit } from '../../shared/model/visit.model';
import * as moment from 'moment';

@Pipe({
  name: 'filterVisitsByDate',
})
export class FilterVisitsByDatePipe implements PipeTransform {
  private date!: Moment;
  transform(items: IVisit[]): IVisit[] {
    this.date = moment();
    if (!items) {
      return [];
    }

    return items.filter(item => this.date.diff(item.time, 'hour') <= 12).sort((a, b) => a.time!.valueOf() - b.time!.valueOf());
  }
}
