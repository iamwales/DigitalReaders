import { Component } from '@angular/core';
import { AuthenticationRequest } from '../../services/models/authentication-request';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/services';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  constructor(
    private router: Router,
    private authService: AuthenticationService // another one
  ) {}
  register() {
    this.router.navigate(['register']);
  }
  login() {
    this.errorMsg = [];
    this.authService
      .authenticate({
        body: this.authRequest,
      })
      .subscribe({
        next: (res) => {
          // todo - save the token
          this.router.navigate(['books']);
        },
        error: (err) => {
          console.log(err);
        },
      });
  }
  authRequest: AuthenticationRequest = { email: '', password: '' };
  errorMsg: Array<String> = [];
}
