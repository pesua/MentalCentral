import { Pipe, PipeTransform } from '@angular/core';
import { Visit } from '../../shared/model/visit.model';

@Pipe({
  name: 'filterVisits',
})
export class FilterVisitsPipe implements PipeTransform {
  transform(visits: Visit[], id: bigint): Visit[] {
    if (!id) return visits;
    if (!visits) return [];
    else return visits.filter(visit => visit.user!.id === id);
  }
}
