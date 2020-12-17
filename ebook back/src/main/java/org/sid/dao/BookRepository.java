package org.sid.dao;

import org.sid.entities.BookApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.ArrayList;

@RepositoryRestResource
public interface BookRepository extends JpaRepository<BookApp,Integer> {
    public BookApp findById(int id);
    @Query("select b from BookApp b")
    public ArrayList<BookApp> findAll();

}
