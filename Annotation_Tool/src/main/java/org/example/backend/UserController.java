package org.example.backend;

import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.database.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

	private UserRepository repo;
	private UserService service;

	@Autowired
	public UserController(UserRepository repo, UserService service) {
		this.repo = repo;
		this.service = service;
	}

	@GetMapping("/")
	@ResponseBody
	public List<User> getUsers() {
		return repo.findAll();
	}

    @GetMapping("/{id}")
 	@ResponseBody
	public ResponseEntity<User> getUser( @PathVariable("id") long id) {
		User user = service.getUser(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
	}

	@PostMapping("/{id}")
	@ResponseBody
	public ResponseEntity<User> addUser(@RequestBody User user) {
		User user1 = service.addUser(user);
		if (user1 == null) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(user1);
	}

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