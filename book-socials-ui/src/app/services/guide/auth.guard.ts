import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TokenService } from '../token/token.service';

export const authGuard: CanActivateFn = () => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  const isTokenValid = tokenService.isTokenValid();

  if (isTokenValid) {
    return true;
  } else {
    router.navigate(['login']);
    return false;
  }
};
