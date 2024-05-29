package org.example.utils;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import org.example.models.SubmissionEntity;
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
     * Method for getting the SubmissionEntitys
     *
     * @return the SubmissionEntitys
     */
    public List<SubmissionEntity> getSubmissionEntitys() {
        return getWebTarget()
                .path("SubmissionEntitys/")
                .request()
                .get(new GenericType<List<SubmissionEntity>>() {});
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
     * Method for getting the SubmissionEntity with the given id
     *
     * @param id the id of the SubmissionEntity
     * @return the SubmissionEntity with the given id
     */
    public SubmissionEntity getSubmissionEntity(long id) {
        return getWebTarget()
                .path("SubmissionEntitys/")
                .path(String.valueOf(id))
                .request()
                .get(SubmissionEntity.class);
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
     * Method for adding a SubmissionEntity
     *
     * @param SubmissionEntity the SubmissionEntity to be added
     * @return the SubmissionEntity that was added
     */
    public SubmissionEntity addSubmissionEntity(SubmissionEntity SubmissionEntity) {
        return getWebTarget()
                .path("SubmissionEntitys/")
                .request()
                .post(Entity.json(SubmissionEntity), SubmissionEntity.class);
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
     * Method for updating a SubmissionEntity
     *
     * @param SubmissionEntity the SubmissionEntity to be updated
     * @return the SubmissionEntity that was updated
     */
    public SubmissionEntity updateSubmissionEntity(SubmissionEntity SubmissionEntity) {
        return getWebTarget()
                .path("SubmissionEntitys/")
                .path(String.valueOf(SubmissionEntity.getId()))
                .request()
                .put(Entity.json(SubmissionEntity), SubmissionEntity.class);
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
     * Method for deleting a SubmissionEntity
     *
     * @param id the id of the SubmissionEntity to be deleted
     */
    public void deleteSubmissionEntity(long id) {
        getWebTarget()
                .path("SubmissionEntitys/")
                .path(String.valueOf(id))
                .request()
                .delete();
    }

    /**
     * Method for getting the SubmissionEntitys of a user
     *
     * @param id the id of the user
     * @return the SubmissionEntitys of the user
     */
    public List<SubmissionEntity> getSubmissionEntitysOfUser(long id) {
        return getWebTarget()
                .path("users/")
                .path(String.valueOf(id))
                .path("SubmissionEntitys/")
                .request()
                .get(new GenericType<List<SubmissionEntity>>() {
                });
    }
}
