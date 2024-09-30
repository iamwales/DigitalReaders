import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  clearToken(): void {
    localStorage.clear();
  }
  isTokenNotValid(): boolean {
    throw this.isTokenValid();
  }
  isTokenValid(): boolean {
    const token = this.token;
    if (!token) {
      return false;
    }
    // decode token
    const jwtHelper = new JwtHelperService();
    // check the exp date
    const isTokenExpired = jwtHelper.isTokenExpired(token);

    if (isTokenExpired) {
      localStorage.clear();
      return false;
    }

    return true;
  }
  set token(token: string) {
    localStorage.setItem('token', token);
  }

  get token() {
    return localStorage.getItem('token') as string;
  }
}
