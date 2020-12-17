import { Injectable } from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {AuthenticationService} from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardServiceService implements CanActivate{


  constructor(private router:Router,private authService:AuthenticationService) { }

  canActivate(): boolean {

    if(this.authService.isAutheticated()){
      return true;
    }
    else {
      this.router.navigateByUrl('/login');
      return false;
    }
  }
}
