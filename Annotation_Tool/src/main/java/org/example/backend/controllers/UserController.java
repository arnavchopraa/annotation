package org.example.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.backend.importmodels.PageDetails;
import org.example.backend.models.User;
import org.example.backend.requestModels.PasswordsRequest;
import org.example.backend.services.JwtService;
import org.example.backend.services.PasswordHashingService;
import org.example.backend.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserController {
    private UserService service;

    private JwtService jwtService;

    /**
     * Constructor for the UserController
     *
     * @param service the service for the user
     * @param jwtService the token service for the jwt
     */
    @Autowired
    public UserController(UserService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    /**
     * This method returns all the users in the database
     *
     * @return a list of all the users in the database
     */
    @Operation(summary = "Get all users from the database",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Successfully retrieved all users",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = User.class))
                        )
                )
            }
    )
    @GetMapping("/")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(service.getUsers());
    }

    /**
     * This method returns the user with the given id
     *
     * @param id the id of the user
     * @return the user with the given id
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a user from the database",
            parameters = {
                @Parameter(name = "id", description = "ID of the user to be retrieved", required = true, in = ParameterIn.PATH)
            },
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Successfully retrieved user",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = User.class)
                        )
                ),
                @ApiResponse(responseCode = "404", description = "Could not find the specified user")
            }
    )
    public ResponseEntity<User> getUser(@PathVariable("id") String id) {
        User user = service.getUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    /**
     * This method returns a page details entity containing the username and the role
     * of the user that has been authenticated
     *
     * @param authorizationHeader the authorization header, containing the token of the user
     * @return a page details entity containing the username and the role of the user
     *
     * !! - Please, better error handling :)
     */
    @Operation(summary = "Retrieve page details of authenticated user",
        parameters = {
            @Parameter(name = "Authorization", description = "JWT Token of the user", required = true, in = ParameterIn.HEADER)
        },
        responses = {
            @ApiResponse(responseCode = "200",
                    description = "Get the page details of the user",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageDetails.class)
                    )
            )
        }
    )
    @GetMapping("/me")
    public ResponseEntity<PageDetails> getPageDetails(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);
        return ResponseEntity.ok(new PageDetails(username, role));
    }

    /**
     * This method adds a user to the database
     *
     * @param user the user to be added
     * @return the user that was added
     */
    @Operation(summary = "Add a user to the database",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User to be added to the database",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Successfully added a new user",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = User.class)
                        )
                ),
                @ApiResponse(responseCode = "400", description = "User could not be saved"),
                @ApiResponse(responseCode = "500", description = "Password hashing failed")
            }
    )
    @PostMapping("/")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            user.setPassword(PasswordHashingService.hashPassword(user.getPassword()));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    @Operation(summary = "Update a user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New User details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully updated the user",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "User could not be saved")
            }
    )
    @PutMapping("/{id}")
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
    @Operation(summary = "Delete a user from the database",
            parameters = {
                @Parameter(name = "id", description = "ID of the user to be deleted", required = true, in = ParameterIn.PATH)
            },
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Successfully deleted the user",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = User.class)
                        )
                ),
                @ApiResponse(responseCode = "404", description = "User was not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable String id) {
        User deleted = service.deleteUser(id);
        if (deleted == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deleted);
    }

    /**
     * This method updates the password of a user
     * @param id the id of the user to be changed
     * @param pass An object containing the old and new password
     * @return the new updated user
     */
    @Operation(summary = "Update a user's password",
            parameters = {
                @Parameter(name = "id", description = "ID of the user whose password should be updated", required = true, in = ParameterIn.PATH)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Password request object",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PasswordsRequest.class)
                    )
            ),
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Password updated successfully",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = User.class)
                        )
                ),
                @ApiResponse(responseCode = "400", description = "Some password field is empty"),
                @ApiResponse(responseCode = "403", description = "Old password does not match"),
                @ApiResponse(responseCode = "404", description = "User was not found in the database"),
                @ApiResponse(responseCode = "500", description = "Password hashing failed")
            }
    )
    @PutMapping("/updatePassword/{id}")
    public ResponseEntity<User> updatePassword(@PathVariable String id, @RequestBody PasswordsRequest pass) {

        if(id == null || pass == null || pass.getOldPassword() == null || pass.getNewPassword() == null
            || pass.getOldPassword().equals("") || pass.getNewPassword().equals(""))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String oldPassword = pass.getOldPassword();
        String newPassword = pass.getNewPassword();

        User user = service.getUser(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            if(!service.checkPassword(user, PasswordHashingService.hashPassword(oldPassword)))
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            user.setPassword(PasswordHashingService.hashPassword(newPassword));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        User updated = service.updateUser(user);
        return ResponseEntity.ok(updated);
    }

}