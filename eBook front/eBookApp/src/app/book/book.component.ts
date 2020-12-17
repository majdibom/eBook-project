import { Component, OnInit } from '@angular/core';
import {BookServiceService} from '../book-service.service';
import {AuthenticationService} from '../authentication.service';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.css']
})
export class BookComponent implements OnInit {
  fileName;
  selectedFile:File=null;
  formData:FormData=null;
  add;
  file;
  resp;
  image;
  username:any;
  constructor(private bookService:BookServiceService,private authService:AuthenticationService) { }

  ngOnInit() {
    this.username=this.authService.username;
    this.getBooks()

  }

  onSelectFile(event) {
    if (event.target.files[0] != undefined) {
        this.formData = new FormData();
        this.file = event.target.files[0];

        this.formData.append('file', this.file);

        this.addBook();
    }
  }
authName;description;fileChosen;
  addBook(){
    let confirmer=confirm("Are you sure to add this book!");
    if (confirmer) {
      this.bookService.postRessource(this.bookService.host + "/books/upload?authName=" + this.authName + "&desc=" + this.description, this.formData)
        .subscribe(data => {

          this.getBooks();
          this.authName = null;
          this.description = null;
          this.fileChosen = null;
        }, err => {

        })
    }
    else {
      this.fileChosen = null;
    }
  }
  books;
  getBooks(){
    this.bookService.getRessource(this.bookService.host+"/user/favoriteInMain?username="+this.username)
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

  isAdmin(){
    return this.authService.isAdmin();
  }

  msg:any;
  favorite(id){
    this.bookService.getRessource(this.bookService.host + "/user/addBookToUser?username=" + this.username + "&id=" + id)
      .subscribe(data => {
        this.getBooks();
        //this.msg=data;
        //window.alert(this.msg);
      }, err => {
        console.log(err)
      })
  }

  delete(id) {
    let confirmer = confirm("Are you sure to delete this book?");
    if (confirmer) {
      this.bookService.deleteRessource(this.bookService.host+"/books/delete?id="+id)
        .subscribe(data => {
          this.getBooks();
        }, err => {
          console.log(err)
        })
    }
  }
}
