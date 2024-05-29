package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.models.User;
import org.example.models.SubmissionEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

class SubmissionEntityTest {

    @Test
    void idGetterAndSetter() {
        SubmissionEntity SubmissionEntity = new SubmissionEntity();
        SubmissionEntity.setId(1L);
        assertEquals(1L, SubmissionEntity.getId());
    }

    @Test
    void userGetterAndSetter() {
        SubmissionEntity SubmissionEntity = new SubmissionEntity();
        User user = new User();
        user.setId(1L);
        SubmissionEntity.setUser(user);
        assertEquals(user, SubmissionEntity.getUser());
    }

    @Test
    void filesGetterAndSetter() throws IOException {
        List<File> files = new ArrayList<>();
        File file = File.createTempFile("temp", ".txt");
        files.add(file);
        User user = new User();
        SubmissionEntity SubmissionEntity = new SubmissionEntity(user.getId(), user);
        SubmissionEntity.setFiles(files);
        assertEquals(files.size(), SubmissionEntity.getFiles().size());
    }

    @Test
    void FileCorrectConversion() throws IOException {
        List<File> files = new ArrayList<>();
        File file = File.createTempFile("temp", ".txt");
        Files.writeString(file.toPath(), "Hello World\n");
        files.add(file);
        User user = new User();
        SubmissionEntity SubmissionEntity = new SubmissionEntity(user.getId(), user, files);
        assertEquals(Files.readString(SubmissionEntity.getFiles().get(0).toPath()), "Hello World\n");
    }

    @Test
    void unreadableFiles() throws FileNotFoundException {
        User user = new User();
        user.setId(1L);
        File file = new File("nonexistent.txt");
        List<File> files = new ArrayList<>();
        files.add(file);
        assertThrows(FileNotFoundException.class, () -> new SubmissionEntity(1L, user, files));
    }

    @Test
    void unreadableFile() throws IOException {
        User user = new User();
        user.setId(1L);
        File file = new File("nonexistent.txt");
        assertThrows(IOException.class, () -> new SubmissionEntity(1L, user, file));
    }

    @Test
    void shouldCreateSubmissionEntityWithUserAndId() throws IOException {
        User user = new User();
        user.setId(1L);
        SubmissionEntity SubmissionEntity = new SubmissionEntity(1L, user);
        assertEquals(1L, SubmissionEntity.getId());
        assertEquals(user, SubmissionEntity.getUser());
        assertTrue(SubmissionEntity.getFiles().isEmpty());
    }

    @Test
    void shouldCreateSubmissionEntityWithUserAndIdAndFiles() throws IOException {
        User user = new User();
        user.setId(1L);
        File file = File.createTempFile("temp", ".txt");
        List<File> files = new ArrayList<>();
        files.add(file);
        SubmissionEntity SubmissionEntity = new SubmissionEntity(1L, user, files);
        assertEquals(1L, SubmissionEntity.getId());
        assertEquals(user, SubmissionEntity.getUser());
        assertEquals(1, SubmissionEntity.getFiles().size());
    }

    @Test
    void shouldCreateSubmissionEntityWithUserAndIdAndSingleFile() throws IOException {
        User user = new User();
        user.setId(1L);
        File file = File.createTempFile("temp", ".txt");
        SubmissionEntity SubmissionEntity = new SubmissionEntity(1L, user, file);
        assertEquals(1L, SubmissionEntity.getId());
        assertEquals(user, SubmissionEntity.getUser());
        assertEquals(1, SubmissionEntity.getFiles().size());
    }
}