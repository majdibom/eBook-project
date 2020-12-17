package org.sid.web;

import org.sid.entities.AppUser;
import org.sid.entities.BookApp;
import org.sid.entities.UserForm;
import org.sid.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class UserRestApi {
    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public AppUser register(@RequestBody UserForm userForm){
        return accountService.saveUser(userForm.getUsername(), userForm.getPassword(),userForm.getConfirmedPassword());
    }

    @PostMapping("appUsers/addRoleToUser")
    public void addRoleToUser(@RequestParam String username,@RequestParam String rolename){
        accountService.addRoleToUser(username, rolename);
    }

    @GetMapping("appUsers/getAllUsers")
    public Page<AppUser> getAllUsers(int page,int size){
        return accountService.getAllUsers(page, size);
    }
    
    @DeleteMapping("/appUsers/deleteRoleFromUser")
    public void deleteRoleFromUser(@RequestParam String username,@RequestParam String rolename) {
    	accountService.deleteRoleFromUser(username, rolename);
    }
    
    @DeleteMapping("appRoles/deleteRole")
    public void deleteRole(@RequestParam String rolename) {
    	accountService.deleteRole(rolename);
    }

    @GetMapping("user/addBookToUser")
    public String addBookToUser(@RequestParam String username,@RequestParam int id){
        return accountService.addBookToUser(username, id);
    }

    @GetMapping("user/getUsersBook")
    public ArrayList<BookApp> findBooksByUserName(@RequestParam String userName){
        return accountService.findBooksByUserName(userName);
    }
}
