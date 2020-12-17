package org.sid.entities;



//cette classe pour qu'on puisse envoyer les donner d'un utilisateur qui vient de s'enregister sous forme Json avec @RequestBody
//dans la méthode register de UserController
public class UserForm {
	
    private String username;
    private String password;
    private String confirmedPassword;

	public UserForm() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserForm(String username, String password, String confirmedPassword) {
		super();
		this.username = username;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmedPassword() {
		return confirmedPassword;
	}
	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

}
