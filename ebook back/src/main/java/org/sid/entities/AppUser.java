package org.sid.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(unique = true)
    private  String userName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//comme json ignore
    private  String password;
    private boolean actived;
    @ManyToMany(fetch = FetchType.EAGER)//charger tt les roles de l'utilisateur
    private Collection<AppRole> roles=new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<BookApp> bookApps=new ArrayList<>();
	public AppUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AppUser(String userName, String password, boolean actived, Collection<AppRole> roles,
			Collection<BookApp> bookApps) {
		super();
		this.userName = userName;
		this.password = password;
		this.actived = actived;
		this.roles = roles;
		this.bookApps = bookApps;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isActived() {
		return actived;
	}
	public void setActived(boolean actived) {
		this.actived = actived;
	}
	public Collection<AppRole> getRoles() {
		return roles;
	}
	public void setRoles(Collection<AppRole> roles) {
		this.roles = roles;
	}
	public Collection<BookApp> getBookApps() {
		return bookApps;
	}
	public void setBookApps(Collection<BookApp> bookApps) {
		this.bookApps = bookApps;
	}
    
}
