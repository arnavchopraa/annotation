package org.example.utils;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import org.example.models.SubmissionDB;
import org.example.models.User;

import java.util.List;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

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
    public List<SubmissionDB> getSubmissions() {
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
    public SubmissionDB getSubmission(long id) {
        return getWebTarget()
                .path("submissions/")
                .path(String.valueOf(id))
                .request()
                .get(SubmissionDB.class);
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
     * @param submissionDB the submission to be added
     * @return the submission that was added
     */
    public SubmissionDB addSubmission(SubmissionDB submissionDB) {
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
     * @param submissionDB the submission to be updated
     * @return the submission that was updated
     */
    public SubmissionDB updateSubmission(SubmissionDB submissionDB) {
        return getWebTarget()
                .path("submissions/")
                .path(String.valueOf(submissionDB.getId()))
                .request()
                .put(Entity.json(submissionDB), SubmissionDB.class);
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
    public List<SubmissionDB> getSubmissionsOfUser(long id) {
        return getWebTarget()
                .path("users/")
                .path(String.valueOf(id))
                .path("submissions/")
                .request()
                .get(new GenericType<List<SubmissionDB>>() {
                });
    }
}
