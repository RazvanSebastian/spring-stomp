import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { NotificationsComponent } from './notifications/notifications.component';
import { CookieService } from 'ngx-cookie-service';
import { NotifierComponent } from './notifier/notifier.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    NotificationsComponent,
    NotifierComponent,
  ],
  imports: [BrowserModule, ReactiveFormsModule, HttpClientModule, FormsModule],
  providers: [CookieService],
  bootstrap: [AppComponent],
})
export class AppModule {}
