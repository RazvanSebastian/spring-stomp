import { Component, OnInit } from '@angular/core';
import { NotifierService } from '../service/notifier.service';

@Component({
  selector: 'app-notifier',
  templateUrl: './notifier.component.html',
  styleUrls: ['./notifier.component.css'],
})
export class NotifierComponent implements OnInit {
  notifiableUsers: string[] = [];
  selectedUser!: string;
  notificationMessage!: string;

  constructor(private notifierService: NotifierService) {}

  ngOnInit(): void {
    this.initializeNotifiableUsers();
  }

  notifyUser() {
    this.notifierService
      .notifyUser({
        message: this.notificationMessage,
        userId: this.selectedUser,
      })
      .subscribe();
  }

  notifyAll() {
    this.notifierService
      .notifyAll({
        message: this.notificationMessage,
      })
      .subscribe();
  }

  private initializeNotifiableUsers() {
    this.notifierService.getUsers().subscribe((users) => {
      this.notifiableUsers = users;
    });
  }
}
