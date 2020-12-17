import { Component, OnInit } from '@angular/core';
import {BookServiceService} from '../book-service.service';
import {AuthenticationService} from '../authentication.service';

@Component({
  selector: 'app-favorite',
  templateUrl: './favorite.component.html',
  styleUrls: ['./favorite.component.css']
})
export class FavoriteComponent implements OnInit {
books;
username;
  constructor(private bookService:BookServiceService,private authService:AuthenticationService) { }

  ngOnInit() {
    this.username=this.authService.username;
    this.getBooks();
  }
  getBooks(){
    this.bookService.getRessource(this.bookService.host+"/user/getUsersBook?userName="+this.username)
      .subscribe(data=>{
        this.books=data;
      },err=>{
        console.log(err);
      })
  }

  down(id){
    this.bookService.getRessource(this.bookService.host+"/download/"+id)
      .subscribe(data=>{
        window.alert("Book has been downloaded succefully")
      },err=>{
        console.log("nooooo")
      })
  }


  favorite(id){
    this.bookService.getRessource(this.bookService.host + "/user/addBookToUser?username=" + this.username + "&id=" + id)
      .subscribe(data => {

        this.getBooks();
      }, err => {
        console.log(err)
      })
  }
}
