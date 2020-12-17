import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from '../authentication.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {


  constructor(private authService:AuthenticationService,private router:Router) { }

  ngOnInit() {
  }
  error="";
  onLogin(data){
    this.authService.login(data)
      .subscribe(resp=>{
        let jwt=resp.headers.get('Authorization');//on aura un jwt, on doit le stocker dans jwt
        this.authService.saveToken(jwt);
        this.router.navigateByUrl("/main");
      },err=>{
        this.error="Username ou mot de passe incorrecte!"
        console.log(err);
      })

  }

}
