import { Component, OnDestroy, OnInit } from '@angular/core';
import { WebsocketService } from '../service/websocket.service';
import { StompSubscription } from '@stomp/stompjs';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css'],
})
export class NotificationsComponent implements OnInit, OnDestroy {
  generalSubscription: StompSubscription | null = null;
  specificSubscription: StompSubscription | null = null;

  notifications: string[] = [];
  userNotifications: string[] = [];

  isConnected: boolean | null = null;

  constructor(private websocketService: WebsocketService) {}

  ngOnDestroy(): void {
    this.generalSubscription?.unsubscribe();
    this.specificSubscription?.unsubscribe();
  }

  ngOnInit(): void {
    this.websocketService.isConnected$.subscribe(() => {
      this.subscribeToTopics();
      this.isConnected = true;
    });
  }

  handleConnection() {
    if (this.isConnected) {
      this.websocketService.disconnectFromServer();
    } else {
      this.websocketService.connectToServer();
    }
    this.isConnected = !this.isConnected;
  }

  private subscribeToTopics() {
    this.generalSubscription = this.websocketService.subscribeToStompServer(
      '/all/hello-all',
      (message) => {
        this.notifications = [...this.notifications, message.body];
      }
    );
    this.specificSubscription = this.websocketService.subscribeToStompServer(
      `/user/specific-user/${this.websocketService.sessionId}/hello`,
      (message) => {
        this.userNotifications = [...this.userNotifications, message.body];
      }
    );
  }
}
