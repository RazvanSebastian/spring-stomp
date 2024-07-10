import { Component, OnInit } from '@angular/core';
import { AuthService } from './service/auth.service';
import { WebsocketService } from './service/websocket.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  isLoggedIn: boolean | null = null;

  notifications: string[] = [];
  sessionId: string | undefined;

  constructor(
    private websocketService: WebsocketService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.authService.isLoggedIn$.subscribe((isLoggedIn) => {
      this.isLoggedIn = isLoggedIn;
    });
    this.authService.isAuthenticated().subscribe({
      next: () => {
        this.authService.isLoggedIn$.next(true);
        this.websocketService.connectToServer();
      },
      error: (err) => {
        console.error(err);
        this.authService.isLoggedIn$.next(false);
      },
    });
  }
}
