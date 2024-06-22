package requestModels;

import org.example.backend.requestModels.PasswordsRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordsRequestTest {

    @Test
    void oldPasswordGetterTest() {
        PasswordsRequest pr = new PasswordsRequest("old", "new");
        assertEquals("old", pr.getOldPassword());
    }

    @Test
    void newPasswordGetterTest() {
        PasswordsRequest pr = new PasswordsRequest("old", "new");
        assertEquals("new", pr.getNewPassword());
    }
}

