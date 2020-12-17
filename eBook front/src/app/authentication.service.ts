import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  host2:string="http://localhost:8080";
  jwt:string;
  username:string;
  roles:Array<string>;
  constructor(private http:HttpClient) { }

  login(data){
    //return this.http.post(this.host2+"/login",data);
    //si on fait seulement ça:cette fonction va recupere la reponse http et la converti sous format Json, or on s'intersesse par l'entete de la reponse http, donc on ajoute option
    return this.http.post(this.host2+"/login",data,{observe:'response'});
    //donc avec observe on le dit c pas la peine de me convertir la reponse au format json mais donne moi tt la reponse http, et apres je vais recuperer tout ce que je veux à partir de la réponse
  }

  saveToken(jwt: string) {
    localStorage.setItem('token',jwt);
    //on le met dans le contexte de l'application
    this.jwt=jwt;
    this.parseJWT();//methode pour recupérer username et role, il faut installer librairie pour parser jwt npm install @auth0/angular-jwt --save
  }

  parseJWT(){
    let jwtHelper=new JwtHelperService();
    let objJWT=jwtHelper.decodeToken(this.jwt);
    this.username=objJWT.sub;//obj c le claim dans jwt qui est object c username
    this.roles=objJWT.roles;//claims roles dans jwt

  }

  isAdmin(){
    return this.roles.indexOf('ADMIN')>=0;//role contient admin ou non
  }

  isUser(){
    return this.roles.indexOf('USER')>=0;
  }

  isAutheticated(){
    return this.roles;//s'il y a roles donc c authentifé déjà
  }

  loadToken(){
    this.jwt=localStorage.getItem('token');// une fois que j'ai le token je dois le parser
    this.parseJWT();
  }

  logout() {
    localStorage.removeItem('token');
    //initialiser les parametre maintenant
    this.initParamas();
  }

  initParamas(){
    this.jwt=undefined;
    this.username=undefined;
    this.roles=undefined;
  }
}
