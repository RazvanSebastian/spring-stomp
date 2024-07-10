import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../service/auth.service';
import { WebsocketService } from '../service/websocket.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  form = new FormGroup({
    username: new FormControl('', { validators: [Validators.required] }),
    password: new FormControl('', { validators: [Validators.required] }),
  });

  constructor(
    private authService: AuthService,
    private websocketService: WebsocketService
  ) {}

  login() {
    this.authService
      .login(
        this.form.value.username as string,
        this.form.value.password as string
      )
      .subscribe((response) => {
        setTimeout(() => {
          this.websocketService.connectToServer();
        }, 1000);
        this.authService.isLoggedIn$.next(true);
      });
  }
}
