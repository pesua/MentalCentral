import { Pipe, PipeTransform } from '@angular/core';
import { Visit } from '../../shared/model/visit.model';
import * as moment from 'moment';
import { Moment } from 'moment';

@Pipe({
  name: 'filterVisits',
})
export class FilterVisitsPipe implements PipeTransform {
  private date!: Moment;
  transform(visits: Visit[], id: bigint): Visit[] {
    this.date = moment();
    if (!id) return visits;
    if (!visits) return [];
    else return visits.filter(visit => visit.user!.id === id && this.date.diff(visit.time, 'hour') <= 2);
  }
}
