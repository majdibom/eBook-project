package org.sid.service;

import org.sid.entities.AppRole;
import org.sid.entities.AppUser;
import org.sid.entities.BookApp;
import org.springframework.data.domain.Page;

import java.util.ArrayList;

public interface AccountService {
    public AppUser saveUser(String username, String password, String confirmedPassword);
    public AppRole saveRole(AppRole appRole);
    public AppUser loadUserByUsername(String username);
    public void addRoleToUser(String username, String rolename);
    public Page<AppUser> getAllUsers(int page, int size);
    public void deleteRoleFromUser(String username, String rolename);
    public void changerPassword(String username, String actuel, String nouvelle, String confirmed);
    public void deleteRole(String rolename);
    public String addBookToUser(String username,int id);
    public ArrayList<BookApp> findBooksByUserName(String userName);
}
