package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.models.User;
import org.example.models.FileEntity;
import org.example.models.Submission;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        Submission submission = new Submission(user.getId(), user, files);
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
}