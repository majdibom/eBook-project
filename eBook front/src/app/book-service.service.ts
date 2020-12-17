import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {AuthenticationService} from './authentication.service';


@Injectable({
  providedIn: 'root'
})
export class BookServiceService {

  public host:string="http://localhost:8080";
  constructor(private http: HttpClient,private authService:AuthenticationService) { }


  postRessource(url,data){
    let headers=new HttpHeaders({'Authorization':'Bearer '+this.authService.jwt});
    return this.http.post(url,data,{headers:headers});//il faut ajouter le token dans l'option headers
  }

  getBooks(url){
    return this.http.get(url);
  }
}
