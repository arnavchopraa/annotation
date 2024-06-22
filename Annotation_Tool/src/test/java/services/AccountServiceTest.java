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
import org.mockito.*;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.example.backend.exceptions.EmailException;
import org.example.backend.importmodels.Coordinator;
import org.example.backend.importmodels.Student;
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

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;

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
        accountService = new AccountService(userService, submissionService, emailService);
    }

    @Test
    void testCreateCoordinatorsAccounts() throws SQLException, EmailException {
        Coordinator coordinator = Mockito.mock(Coordinator.class);
        Mockito.when(userService.getUser(anyString())).thenReturn(null);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());
        Mockito.when(emailService.generateRandomCode()).thenReturn("randomhash");
        accountService.createCoordinatorsAccounts(Arrays.asList(coordinator));

        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    void testCreateStudentAccounts() throws EmailException {
        Student student = new Student("", "", "", "", "", "");
        Mockito.when(userService.getUser(anyString())).thenReturn(null);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());
        Mockito.when(emailService.generateRandomCode()).thenReturn("randomhash");
        accountService.createStudentAccounts(Arrays.asList(student));

        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    void testCreateSubmissions() throws SQLException {
        Coordinator coordinator = Mockito.mock(Coordinator.class);
        Student student = Mockito.mock(Student.class);
        User user = new User();
        user.setCorrespondingSubmissions(new HashSet<>());
        Association mockAssociation = mock(Association.class);
        when(coordinator.getAssociations()).thenReturn(Arrays.asList(mockAssociation));
        when(mockAssociation.getStudent()).thenReturn(student);
        when(mockAssociation.getSubmission()).thenReturn(new Submission("test", "test",
        "test", "test", "test", "test", new byte[0]));
        when(userService.getUser(anyString())).thenReturn(user);

        accountService.createSubmissions(coordinator, user);

        verify(submissionService, times(1)).addSubmission(any(SubmissionDB.class));
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
