import { Component, OnInit } from '@angular/core';
import {BookServiceService} from '../book-service.service';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.css']
})
export class BookComponent implements OnInit {
  fileName;
  selectedFile:File=null;
  formData:FormData=null;
  file;
  resp;
  image;
  constructor(private bookService:BookServiceService) { }

  ngOnInit() {
    this.getBooks()
  }
  onSelectImage(event){
    this.image=event.target.files[0].name;
  }
  onSelectFile(event) {
    if (event.target.files[0] != undefined) {
      this.fileName=event.target.files[0].name
        this.formData = new FormData();
        this.selectedFile = event.target.files;
        this.file = this.selectedFile[0];

        this.formData.append('file', this.file);

        this.addBook();
    }
  }

  addBook(){
    this.bookService.postRessource(this.bookService.host+"/books/upload",this.formData)
      .subscribe(data=>{
        this.resp=data;
      },err=>{

      })
  }
  books;
  getBooks(){
    this.bookService.getBooks(this.bookService.host+"/bookApps")
      .subscribe(data=>{
        this.books=data;
      },err=>{
        console.log(err);
      })
  }
  down(id){
    this.bookService.getBooks(this.bookService.host+"/download/"+id)
      .subscribe(data=>{
        console.log("okkkk")
      },err=>{
        console.log("nooooo")
      })
  }
}
