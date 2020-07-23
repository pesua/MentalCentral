import { Pipe, PipeTransform } from '@angular/core';
import { IVisit } from '../../shared/model/visit.model';

@Pipe({
  name: 'filterByPatientId',
})
export class FIlterPipe implements PipeTransform {
  transform(visits: IVisit[], id: number): IVisit[] {
    if (!visits) {
      return [];
    }
    return visits.filter(visit => visit.patient.id === id);
  }
}
