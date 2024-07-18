import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UsersNotification } from '../models';

@Injectable({
  providedIn: 'root',
})
export class NotifierService {
  constructor(private httpClient: HttpClient) {}

  getUsers() {
    return this.httpClient.get<string[]>('/api/notifier/users');
  }

  notifyAll(usersNotification: UsersNotification) {
    return this.httpClient.post(
      '/api/notifier/all-notification',
      usersNotification
    );
  }

  notifyUser(usersNotification: UsersNotification) {
    return this.httpClient.post(
      '/api/notifier/user-notification',
      usersNotification
    );
  }
}
