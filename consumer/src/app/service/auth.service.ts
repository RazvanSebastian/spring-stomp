import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Subject, switchMap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private _csrfToken: string | null = null;
  private getCsrfTokenFunction = (response: HttpResponse<Object>) => {
    if (response.status >= 200 && response.status < 300) {
      return this.httpClient.get('/api/csrf-token', {
        observe: 'response',
        responseType: 'text',
      });
    } else {
      throw new Error('Not authenticated');
    }
  };

  isLoggedIn$ = new Subject<boolean>();

  constructor(private httpClient: HttpClient) {}

  get csrfToken() {
    return this._csrfToken;
  }

  login(username: string, password: string) {
    return (
      this.httpClient
        .post('/api/login', JSON.stringify({ username, password }), {
          observe: 'response',
        })
        // After login we need to get csrf token since we don't have before login
        .pipe(switchMap(this.getCsrfTokenFunction))
    );
  }

  isAuthenticated() {
    return this.httpClient.get('/api/is-authenticated', {
      observe: 'response',
    });
  }
}
