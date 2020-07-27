import { Pipe, PipeTransform } from '@angular/core';
import { IVisit } from '../../shared/model/visit.model';

@Pipe({
  name: 'userIdFilter',
})
export class UserIdPipe implements PipeTransform {
  transform(items: IVisit[], userId: bigint): IVisit[] {
    if (!items) {
      return [];
    }
    return items.filter(item => item.user!.id === userId);
  }
}
