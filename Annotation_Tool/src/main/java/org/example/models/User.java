package org.example.models;

import javax.persistance.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name="Users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name="name")
	private String name;

	@Column(name="password")
	private String password;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	/**
	 * Basic constructor for User
	 *
	 * @param name the name of the user
	 * @param password the password of the user
	 */

	public User(String name, String password) {
		this.name = name;
		this.encoder = new BcryptPasswordEncoder();
		this.password = encoder.encode(password);
	}

	/**
	 * Getter for the id of the user
	 *
	 * @return the id of the user
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter for the id of the user
	 *
	 * @param id the id of the user
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter for the name of the user
	 *
	 * @return the name of the user
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for the name of the user
	 *
	 * @param name the name of the user
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for the password of the user
	 *
	 * @return the password of the user
	 */

	public String getPassword() {
		return password;
	}

	/**
	 * Setter for the password of the user
	 *
	 * @param password the password of the user
	 */

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Method for checking if the password is correct
	 *
	 * @param password the password to check
	 * @return true if the password is correct, false otherwise
	 */

	public boolean checkPassword(String password) {
		return encoder.matches(password, this.password);
	}
}