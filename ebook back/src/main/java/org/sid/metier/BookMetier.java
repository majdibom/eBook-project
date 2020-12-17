package org.sid.metier;

import org.sid.entities.BookApp;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

public interface BookMetier {
    public void uploadFile(String authName, String desc,MultipartFile file);
    public BookApp downloadFile(int id);
    public void deleteBook(int id);
    public ArrayList<BookApp> findAll();
    public ArrayList<BookApp> favoriteColor(String username);
}
