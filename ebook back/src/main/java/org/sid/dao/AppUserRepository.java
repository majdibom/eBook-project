package org.sid.dao;

import java.util.ArrayList;

import org.sid.entities.AppRole;
import org.sid.entities.AppUser;
import org.sid.entities.BookApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    public AppUser findByUserName(String userName);
    public ArrayList<AppUser> findByRoles(AppRole appRole);
    @Query("select a.bookApps from AppUser a where a.userName=:x")
    public ArrayList<BookApp> findBooksByUserName(@Param("x") String userName);
}
