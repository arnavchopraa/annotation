package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.models.User;
import org.example.models.SubmissionDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

class SubmissionDBTest {

    @Test
    void idGetterAndSetter() {
        SubmissionDB submissionDB = new SubmissionDB();
        submissionDB.setId(1L);
        assertEquals(1L, submissionDB.getId());
    }

    @Test
    void userGetterAndSetter() {
        SubmissionDB submissionDB = new SubmissionDB();
        User user = new User();
        user.setId(1L);
        submissionDB.setUser(user);
        assertEquals(user, submissionDB.getUser());
    }

    @Test
    void filesGetterAndSetter() throws IOException {
        List<File> files = new ArrayList<>();
        File file = File.createTempFile("temp", ".txt");
        files.add(file);
        User user = new User();
        SubmissionDB submissionDB = new SubmissionDB(user.getId(), user);
        submissionDB.setFiles(files);
        assertEquals(files.size(), submissionDB.getFiles().size());
    }

    @Test
    void FileCorrectConversion() throws IOException {
        List<File> files = new ArrayList<>();
        File file = File.createTempFile("temp", ".txt");
        Files.writeString(file.toPath(), "Hello World\n");
        files.add(file);
        User user = new User();
        SubmissionDB submissionDB = new SubmissionDB(user.getId(), user, files);
        assertEquals(Files.readString(submissionDB.getFiles().get(0).toPath()), "Hello World\n");
    }

    @Test
    void unreadableFiles() throws FileNotFoundException {
        User user = new User();
        user.setId(1L);
        File file = new File("nonexistent.txt");
        List<File> files = new ArrayList<>();
        files.add(file);
        assertThrows(FileNotFoundException.class, () -> new SubmissionDB(1L, user, files));
    }

    @Test
    void unreadableFile() throws IOException {
        User user = new User();
        user.setId(1L);
        File file = new File("nonexistent.txt");
        assertThrows(IOException.class, () -> new SubmissionDB(1L, user, file));
    }

    @Test
    void shouldCreateSubmissionWithUserAndId() throws IOException {
        User user = new User();
        user.setId(1L);
        SubmissionDB submissionDB = new SubmissionDB(1L, user);
        assertEquals(1L, submissionDB.getId());
        assertEquals(user, submissionDB.getUser());
        assertTrue(submissionDB.getFiles().isEmpty());
    }

    @Test
    void shouldCreateSubmissionWithUserAndIdAndFiles() throws IOException {
        User user = new User();
        user.setId(1L);
        File file = File.createTempFile("temp", ".txt");
        List<File> files = new ArrayList<>();
        files.add(file);
        SubmissionDB submissionDB = new SubmissionDB(1L, user, files);
        assertEquals(1L, submissionDB.getId());
        assertEquals(user, submissionDB.getUser());
        assertEquals(1, submissionDB.getFiles().size());
    }

    @Test
    void shouldCreateSubmissionWithUserAndIdAndSingleFile() throws IOException {
        User user = new User();
        user.setId(1L);
        File file = File.createTempFile("temp", ".txt");
        SubmissionDB submissionDB = new SubmissionDB(1L, user, file);
        assertEquals(1L, submissionDB.getId());
        assertEquals(user, submissionDB.getUser());
        assertEquals(1, submissionDB.getFiles().size());
    }
}