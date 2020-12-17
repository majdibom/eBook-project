package org.sid.web;

import org.sid.entities.BookApp;
import org.sid.metier.BookMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@CrossOrigin("*")
public class BookRestService {
    @Autowired
    private BookMetier bookMetier;

    @PostMapping("books/upload")
    public void uploadFile(@RequestParam String authName,@RequestParam String desc,@RequestParam("file") MultipartFile file) {

        bookMetier.uploadFile(authName, desc,file);
    }

    @GetMapping("/download/{id}")
    public BookApp downloadFile(@PathVariable int id) {
        return bookMetier.downloadFile(id);
     // BookApp bookApp=  bookMetier.downloadFile(id);

        /*return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(bookApp.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + bookApp.getNom() + "\"")
                .body(new ByteArrayResource(bookApp.getData()));*/
    }

    @DeleteMapping("books/delete")
    public void deleteBook(@RequestParam int id){
        bookMetier.deleteBook(id);
    }

    @GetMapping("user/findAll")
    public ArrayList<BookApp> findAll(){
        return bookMetier.findAll();
    }

    @GetMapping("user/favoriteInMain")
    public ArrayList<BookApp> favoriteColor(@RequestParam String username) {
        return bookMetier.favoriteColor(username);
    }
}
