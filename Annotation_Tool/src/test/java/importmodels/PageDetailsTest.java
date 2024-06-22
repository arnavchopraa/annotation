package importmodels;

import org.example.backend.importmodels.PageDetails;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PageDetailsTest {

    @Test
    public void testConstructorAndGetters() {
        // Create an instance using the constructor
        String email = "test@example.com";
        String role = "admin";
        PageDetails pageDetails = new PageDetails(email, role);

        // Verify the values set by the constructor
        assertEquals(email, pageDetails.getEmail());
        assertEquals(role, pageDetails.getRole());
    }

    @Test
    public void testSettersAndGetters() {
        // Create an instance using the default constructor
        PageDetails pageDetails = new PageDetails();

        // Set values using setters
        String email = "test@example.com";
        String role = "admin";
        pageDetails.setEmail(email);
        pageDetails.setRole(role);

        // Verify the values using getters
        assertEquals(email, pageDetails.getEmail());
        assertEquals(role, pageDetails.getRole());
    }

    @Test
    public void testToString() {
        // Create an instance using the constructor
        String email = "test@example.com";
        String role = "admin";
        PageDetails pageDetails = new PageDetails(email, role);

        // Verify the toString() method
        String expectedToString = "PageDetails(email=test@example.com, role=admin)";
        assertEquals(expectedToString, pageDetails.toString());
    }
}
