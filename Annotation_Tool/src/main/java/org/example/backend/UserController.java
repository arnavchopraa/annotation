package org.example.backend;

import org.example.models.User;
import org.example.database.UserRepository;
import org.example.database.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

	private UserRepository repo;
	private UserService service;

	/**
	 * Constructor for the UserController
	 *
	 * @param repo the repository for the user
	 * @param service the service for the user
	 */
	@Autowired
	public UserController(UserRepository repo, UserService service) {
		this.repo = repo;
		this.service = service;
	}

	/**
	 * This method returns all the users in the database
	 *
	 * @return a list of all the users in the database
	 */
	@GetMapping("/")
	@ResponseBody
	public List<User> getUsers() {
		return repo.findAll();
	}

	/**
	 * This method returns the user with the given id
	 *
	 * @param id the id of the user
	 * @return the user with the given id
	 */
    @GetMapping("/{id}")
 	@ResponseBody
	public ResponseEntity<User> getUser( @PathVariable("id") long id) {
		User user = service.getUser(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
	}

	/**
	 * This method adds a user to the database
	 *
	 * @param user the user to be added
	 * @return the user that was added
	 */
	@PostMapping("/{id}")
	@ResponseBody
	public ResponseEntity<User> addUser(@RequestBody User user) {
		User user1 = service.addUser(user);
		if (user1 == null) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(user1);
	}

	/**
	 * This method updates a user in the database
	 *
	 * @param user the user to be updated
	 * @return the user that was updated
	 */
	@PutMapping("/{id}")
	@ResponseBody
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		User user1 = service.updateUser(user);
		if (user1 == null) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(user1);
	}

	/**
	 * This method deletes a user from the database
	 *
	 * @param id the id of the user to be deleted
	 * @return the user that was deleted
	 */
	@DeleteMapping("/{id}")
	@ResponseBody
	public void deleteUser(@PathVariable Long id) {
		User deleted = service.deleteUser(id);
		if (deleted == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(deleted);
	}
}