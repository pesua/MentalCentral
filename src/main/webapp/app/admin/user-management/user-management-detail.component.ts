import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { User } from 'app/core/user/user.model';
import { UserExtraService } from '../../entities/user-extra/user-extra.service';

@Component({
  selector: 'jhi-user-mgmt-detail',
  templateUrl: './user-management-detail.component.html',
})
export class UserManagementDetailComponent implements OnInit {
  user: User | null = null;

  constructor(private route: ActivatedRoute, private userExtraService: UserExtraService) {}

  ngOnInit(): void {
    this.route.data.subscribe(({ user }) => {
      this.user = user;

      if (user.id !== undefined) {
        this.userExtraService.find(user.id).subscribe(userExtra => {
          user.middleName = userExtra.body!.middleName;
          user.phoneNumber = userExtra.body!.phoneNumber;
        });
      }
    });
  }
}
