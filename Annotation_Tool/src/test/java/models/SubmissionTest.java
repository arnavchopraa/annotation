package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.models.User;
import org.example.models.Submission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

class SubmissionTest {

    @Test
    void idGetterAndSetter() {
        Submission submission = new Submission();
        submission.setId(1L);
        assertEquals(1L, submission.getId());
    }

    @Test
    void userGetterAndSetter() {
        Submission submission = new Submission();
        User user = new User();
        user.setId(1L);
        submission.setUser(user);
        assertEquals(user, submission.getUser());
    }

    @Test
    void filesGetterAndSetter() throws IOException {
        List<File> files = new ArrayList<>();
        File file = File.createTempFile("temp", ".txt");
        files.add(file);
        User user = new User();
        Submission submission = new Submission(user.getId(), user);
        submission.setFiles(files);
        assertEquals(files.size(), submission.getFiles().size());
    }

    @Test
    void FileCorrectConversion() throws IOException {
        List<File> files = new ArrayList<>();
        File file = File.createTempFile("temp", ".txt");
        Files.writeString(file.toPath(), "Hello World\n");
        files.add(file);
        User user = new User();
        Submission submission = new Submission(user.getId(), user, files);
        assertEquals(Files.readString(submission.getFiles().get(0).toPath()), "Hello World\n");
    }

    @Test
    void unreadableFiles() throws FileNotFoundException {
        User user = new User();
        user.setId(1L);
        File file = new File("nonexistent.txt");
        List<File> files = new ArrayList<>();
        files.add(file);
        assertThrows(FileNotFoundException.class, () -> new Submission(1L, user, files));
    }

    @Test
    void unreadableFile() throws IOException {
        User user = new User();
        user.setId(1L);
        File file = new File("nonexistent.txt");
        assertThrows(IOException.class, () -> new Submission(1L, user, file));
    }

    @Test
    void shouldCreateSubmissionWithUserAndId() throws IOException {
        User user = new User();
        user.setId(1L);
        Submission submission = new Submission(1L, user);
        assertEquals(1L, submission.getId());
        assertEquals(user, submission.getUser());
        assertTrue(submission.getFiles().isEmpty());
    }

    @Test
    void shouldCreateSubmissionWithUserAndIdAndFiles() throws IOException {
        User user = new User();
        user.setId(1L);
        File file = File.createTempFile("temp", ".txt");
        List<File> files = new ArrayList<>();
        files.add(file);
        Submission submission = new Submission(1L, user, files);
        assertEquals(1L, submission.getId());
        assertEquals(user, submission.getUser());
        assertEquals(1, submission.getFiles().size());
    }

    @Test
    void shouldCreateSubmissionWithUserAndIdAndSingleFile() throws IOException {
        User user = new User();
        user.setId(1L);
        File file = File.createTempFile("temp", ".txt");
        Submission submission = new Submission(1L, user, file);
        assertEquals(1L, submission.getId());
        assertEquals(user, submission.getUser());
        assertEquals(1, submission.getFiles().size());
    }
}