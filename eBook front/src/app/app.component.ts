import { Component } from '@angular/core';
import {AuthenticationService} from './authentication.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'eBookApp';
  constructor(private authService:AuthenticationService,private router:Router) {}

  ngOnInit(): void {  //il faut ajouter implements onInit à la classe au début

     this.authService.loadToken();

     // on va l'appeler psk qu'on on s'authetifie et après on load la page on doit s'authentifier de nouveau
  }

  isAdmin(){
    return this.authService.isAdmin();
  }
  isUser(){
    return this.authService.isUser();
  }
  isAuthenticated(){
    return  this.authService.isAutheticated();
  }

  logout(){
    this.authService.logout();
    this.router.navigateByUrl('/login')
  }
}
