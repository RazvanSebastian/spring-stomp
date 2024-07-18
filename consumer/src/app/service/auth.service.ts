import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Subject, switchMap, tap } from 'rxjs';
import { UserDetails } from '../models';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private _csrfToken: string | null = null;

  isLoggedIn$ = new Subject<boolean>();
  userDetails$ = new Subject<UserDetails>();

  constructor(private httpClient: HttpClient) {}

  get csrfToken() {
    return this._csrfToken;
  }

  login(username: string, password: string) {
    return (
      this.httpClient
        .post<UserDetails>(
          '/api/login',
          JSON.stringify({ username, password }),
          {
            observe: 'response',
            responseType: 'json',
          }
        )
        // After login we need to get csrf token since we don't have before login
        .pipe(
          tap((response: HttpResponse<UserDetails>) => {
            this.userDetails$.next(response.body as UserDetails);
          }),
          switchMap((response: HttpResponse<Object>) => {
            if (response.status >= 200 && response.status < 300) {
              return this.httpClient.get('/api/csrf-token', {
                observe: 'response',
                responseType: 'text',
              });
            } else {
              throw new Error('Not authenticated');
            }
          })
        )
    );
  }

  isAuthenticated() {
    return this.httpClient.get<UserDetails>('/api/is-authenticated', {
      observe: 'response',
      responseType: 'json',
    });
  }
}
