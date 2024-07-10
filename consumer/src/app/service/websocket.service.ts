import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import { CookieService } from 'ngx-cookie-service';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class WebsocketService {
  private _client: Client | null = null;
  private _sessionId: string | null = null;

  isConnected$ = new Subject<boolean>();

  get sessionId() {
    return this._sessionId;
  }

  constructor(private cookieService: CookieService) {}

  connectToServer() {
    this._client = new Client({
      brokerURL: 'ws://localhost:8080/api/ws',
      connectHeaders: {
        'X-XSRF-TOKEN': this.cookieService.get('XSRF-TOKEN'),
      },
      onConnect: (frame) => {
        console.log('Connected to websocket');
        this._sessionId = frame.headers['user-name']; // Get the session ID
        this.isConnected$.next(true);
      },
    });
    this._client.debug = (message) => {
      console.log(message);
    };
    this._client.activate();
  }

  disconnectFromServer() {
    this._client?.deactivate();
  }

  subscribeToStompServer(
    destination: string,
    consumer: (message: IMessage) => void
  ) {
    return this._client?.subscribe(destination, consumer) || null;
  }
}
