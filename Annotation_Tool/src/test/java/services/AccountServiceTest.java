package services;

import org.example.backend.exceptions.EmailException;
import org.example.backend.importmodels.Association;
import org.example.backend.importmodels.Coordinator;
import org.example.backend.importmodels.Student;
import org.example.backend.importmodels.Submission;
import org.example.backend.models.SubmissionDB;
import org.example.backend.models.User;
import org.example.backend.services.AccountService;
import org.example.backend.services.EmailService;
import org.example.backend.services.SubmissionService;
import org.example.backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    @Mock
    private SubmissionService submissionService;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGenerateContent() {
        String username = "johndoe@example.com";
        String password = "randomPassword123";

        String expectedContent = "An administrator has uploaded theses on our annotation tool! We have" +
                " created an account for you. Here are the credentials that you can use to access your" +
                "account: <br> Username: <b>johndoe@example.com</b> <br> Password: <b>randomPassword123</b> <br> You can reset your password later on the platform!";

        String generatedContent = accountService.generateContent(username, password);

        assertEquals(expectedContent, generatedContent);
    }
    // Additional tests for edge cases, exception handling, etc. can be added as needed.
}
