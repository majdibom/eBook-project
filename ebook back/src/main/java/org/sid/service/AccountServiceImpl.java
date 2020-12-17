package org.sid.service;

import java.util.ArrayList;

import org.sid.dao.AppRoleRepository;
import org.sid.dao.AppUserRepository;
import org.sid.dao.BookRepository;
import org.sid.entities.AppRole;
import org.sid.entities.AppUser;
import org.sid.entities.BookApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppRoleRepository appRoleRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; //il faut ajouter dans SecServiceApplication @bean de BcryptPasswordEncoder


    @Override
    public AppUser saveUser(String username, String password, String confirmedPassword) {
        AppUser user=appUserRepository.findByUserName(username);
        if(user!=null) throw new  RuntimeException("Utilisateur existe déjà");
        if(!password.equals(confirmedPassword)) throw new RuntimeException("Mot de passe n'est pas confirmé");

        AppUser appUser=new AppUser();
        appUser.setUserName(username);
        appUser.setPassword(bCryptPasswordEncoder.encode(password));//on va l'enregistrer dans la BD de manière crypté
        appUser.setActived(true);
        appUserRepository.save(appUser);
        addRoleToUser(username,"USER");//attribuer à un utilisateur qui vient de s'enregistrer un role user

        return appUser;
    }

    @Override
    public AppRole saveRole(AppRole appRole) {
        AppRole role=appRoleRepository.findByRoleName(appRole.getRoleName());
        if (role!=null) throw new  RuntimeException("Role existe déjà");
        return appRoleRepository.save(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUserName(username);

    }

    @Override
    public void addRoleToUser(String username, String rolename) {
        AppUser appUser=appUserRepository.findByUserName(username);
        AppRole appRole=appRoleRepository.findByRoleName(rolename);
        if (appUser.getRoles().contains(appRole)) throw new RuntimeException("Ce role est déjà associé à cet utilisateur");
        appUser.getRoles().add(appRole);
    }

    @Override
    public Page<AppUser> getAllUsers(int page, int size) {
        return appUserRepository.findAll(new PageRequest(page,size));
    }

	@Override
	public void deleteRoleFromUser(String username, String rolename) {
		AppUser user=appUserRepository.findByUserName(username);
		AppRole role=appRoleRepository.findByRoleName(rolename);
		user.getRoles().remove(role);		
	}

	@Override
	public void changerPassword(String username,String actuel, String nouvelle, String confirmed) {
		AppUser user=appUserRepository.findByUserName(username);
		if(nouvelle!=confirmed) throw new RuntimeException("Mot de passe n'est pas confirmé");
		user.setPassword(bCryptPasswordEncoder.encode(nouvelle));
		appUserRepository.save(user);
	}

	@Override
	public void deleteRole(String rolename) {
		AppRole role=appRoleRepository.findByRoleName(rolename);
		ArrayList<AppUser> appUsers=appUserRepository.findByRoles(role);
		appUsers.forEach(x->x.getRoles().remove(role));
		appRoleRepository.delete(role);
	}

    @Override
    public String addBookToUser(String username, int id) {
        String message="";
        AppUser appUser=appUserRepository.findByUserName(username);
        BookApp bookApp=bookRepository.findById(id);
        if(!appUser.getBookApps().contains(bookApp)){
            appUser.getBookApps().add(bookApp);
            message="Book has been added to favorite";
        }
        else {
            appUser.getBookApps().remove(bookApp);
            message="Book has been removed from favorite";
        }
        return message;
    }

    @Override
    public ArrayList<BookApp> findBooksByUserName(String userName) {
        return appUserRepository.findBooksByUserName(userName);
    }
}
