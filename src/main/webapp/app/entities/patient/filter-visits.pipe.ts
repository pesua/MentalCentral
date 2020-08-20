import { Pipe, PipeTransform } from '@angular/core';
import { Visit } from '../../shared/model/visit.model';

@Pipe({
  name: 'filterVisits',
})
export class FilterVisitsPipe implements PipeTransform {
  private date!: Date;
  transform(visits: Visit[], id: bigint): Visit[] {
    this.date = new Date();
    if (!id) return visits;
    if (!visits) return [];
    else return visits.filter(visit => visit.user!.id === id && visit.time!.date() >= this.date.getDate());
  }
}
