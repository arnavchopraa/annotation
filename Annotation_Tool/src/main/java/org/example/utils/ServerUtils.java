package org.example.utils;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import org.example.models.Submission;
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
    private WebTarget getWebTarget() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server);
    }

    /**
     * Method for getting the users
     *
     * @return the users
     */
    public List<User> getUsers() {
        return getWebTarget()
                .path("users/")
                .request()
                .get(new GenericType<List<User>>() {});
    }
    /**
     * Method for getting the submissions
     *
     * @return the submissions
     */
    public List<Submission> getSubmissions() {
        return getWebTarget()
                .path("submissions/")
                .request()
                .get(new GenericType<List<Submission>>() {});
    }

    /**
     * Method for getting the user with the given id
     *
     * @param id the id of the user
     * @return the user with the given id
     */
    public User getUser(long id) {
        return getWebTarget()
                .path("users/")
                .path(String.valueOf(id))
                .request()
                .get(User.class);
    }
    /**
     * Method for getting the submission with the given id
     *
     * @param id the id of the submission
     * @return the submission with the given id
     */
    public Submission getSubmission(long id) {
        return getWebTarget()
                .path("submissions/")
                .path(String.valueOf(id))
                .request()
                .get(Submission.class);
    }

    /**
     * Method for adding a user
     *
     * @param user the user to be added
     * @return the user that was added
     */
    public User addUser(User user) {
        return getWebTarget()
                .path("users/")
                .request()
                .post(Entity.json(user), User.class);
    }

    /**
     * Method for adding a submission
     *
     * @param submission the submission to be added
     * @return the submission that was added
     */
    public Submission addSubmission(Submission submission) {
        return getWebTarget()
                .path("submissions/")
                .request()
                .post(Entity.json(submission), Submission.class);
    }

    /**
     * Method for updating a user
     *
     * @param user the user to be updated
     * @return the user that was updated
     */
    public User updateUser(User user) {
        return getWebTarget()
                .path("users/")
                .path(String.valueOf(user.getId()))
                .request()
                .put(Entity.json(user), User.class);
    }

    /**
     * Method for updating a submission
     *
     * @param submission the submission to be updated
     * @return the submission that was updated
     */
    public Submission updateSubmission(Submission submission) {
        return getWebTarget()
                .path("submissions/")
                .path(String.valueOf(submission.getId()))
                .request()
                .put(Entity.json(submission), Submission.class);
    }

    /**
     * Method for deleting a user
     *
     * @param id the id of the user to be deleted
     */
    public void deleteUser(long id) {
        getWebTarget()
                .path("users/")
                .path(String.valueOf(id))
                .request()
                .delete();
    }

    /**
     * Method for deleting a submission
     *
     * @param id the id of the submission to be deleted
     */
    public void deleteSubmission(long id) {
        getWebTarget()
                .path("submissions/")
                .path(String.valueOf(id))
                .request()
                .delete();
    }

    /**
     * Method for getting the submissions of a user
     *
     * @param id the id of the user
     * @return the submissions of the user
     */
    public List<Submission> getSubmissionsOfUser(long id) {
        return getWebTarget()
                .path("users/")
                .path(String.valueOf(id))
                .path("submissions/")
                .request()
                .get(new GenericType<List<Submission>>() {
                });
    }
}
