package org.example.utils;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.example.models.SubmissionDB;
import org.example.models.User;

import java.util.List;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerUtils {
    @Value("${backend.server.url}")
    private static String server;

    /**
     * Method for getting the server
     *
     * @return the server
     */
    public static String getServer() {
        return server;
    }

    /**
     * Method for setting the server
     *
     * @param server the server
     */
    public static void setServer(String server) {
        ServerUtils.server = server;
    }

    /**
     * Method for getting the web target
     *
     * @return the web target
     */
    private static WebTarget getWebTarget() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server);
    }

    /**
     * Method for getting the users
     *
     * @return the users
     */
    public static List<User> getUsers() {
        return getWebTarget()
                .path("users/")
                .request()
                .get(new GenericType<List<User>>() {});
    }
    /**
     * Method for getting the SubmissionDBs
     *
     * @return the SubmissionDBs
     */
    public static List<SubmissionDB> getSubmissionDBs() {
        return getWebTarget()
                .path("submissions/")
                .request()
                .get(new GenericType<List<SubmissionDB>>() {});
    }

    /**
     * Method for getting the user with the given id
     *
     * @param id the id of the user
     * @return the user with the given id
     */
    public static Response getUser(String id) {
        return getWebTarget()
                .path("users/")
                .path(id)
                .request()
                .get();
    }
    /**
     * Method for getting the SubmissionDB with the given id
     *
     * @param id the id of the SubmissionDB
     * @return the SubmissionDB with the given id
     */
    public static SubmissionDB getSubmissionDB(String id) {
        return getWebTarget()
                .path("submissions/")
                .path(id)
                .request()
                .get(SubmissionDB.class);
    }

    /**
     * Method for adding a user
     *
     * @param user the user to be added
     * @return the user that was added
     */
    public static User addUser(User user) {
        return getWebTarget()
                .path("users/")
                .request()
                .post(Entity.json(user), User.class);
    }

    /**
     * Method for adding a SubmissionDB
     *
     * @param submissionDB the SubmissionDB to be added
     * @return the SubmissionDB that was added
     */
    public static SubmissionDB addSubmissionDB(SubmissionDB submissionDB) {
        return getWebTarget()
                .path("submissions/")
                .request()
                .post(Entity.json(submissionDB), SubmissionDB.class);
    }

    /**
     * Method for updating a user
     *
     * @param user the user to be updated
     * @return the user that was updated
     */
    public static User updateUser(User user) {
        return getWebTarget()
                .path("users/")
                .path(user.getId())
                .request()
                .put(Entity.json(user), User.class);
    }

    /**
     * Method for updating a SubmissionDB
     *
     * @param submissionDB the SubmissionDB to be updated
     * @return the SubmissionDB that was updated
     */
    public static SubmissionDB updateSubmissionDB(SubmissionDB submissionDB) {
        return getWebTarget()
                .path("submissions/")
                .path(submissionDB.getId())
                .request()
                .put(Entity.json(submissionDB), SubmissionDB.class);
    }

    /**
     * Method for deleting a user
     *
     * @param id the id of the user to be deleted
     */
    public static void deleteUser(String id) {
        getWebTarget()
                .path("users/")
                .path(id)
                .request()
                .delete();
    }

    /**
     * Method for deleting a SubmissionDB
     *
     * @param id the id of the SubmissionDB to be deleted
     */
    public static void deleteSubmissionDB(String id) {
        getWebTarget()
                .path("submissions/")
                .path(id)
                .request()
                .delete();
    }

    /**
     * Method for getting the SubmissionDBs of a user
     *
     * @param id the id of the user
     * @return the SubmissionDBs of the user
     */
    public static List<SubmissionDB> getSubmissionDBsOfUser(String id) {
        return getWebTarget()
                .path("users/")
                .path(id)
                .path("submissions/")
                .request()
                .get(new GenericType<List<SubmissionDB>>() {
                });
    }
}
