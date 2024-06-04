package controllers;

import org.example.controllers.AnnotationCodeController;
import org.example.models.AnnotationCode;
import org.example.services.AnnotationCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AnnotationCodeControllerTest {

    @Mock
    private AnnotationCodeService service;

    @InjectMocks
    private AnnotationCodeController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAnnotationCodes() {
        List<AnnotationCode> codes = Arrays.asList(new AnnotationCode("1", "Code1"), new AnnotationCode("2", "Code2"));

        when(service.getAnnotationCodes()).thenReturn(codes);

        ResponseEntity<List<AnnotationCode>> response = controller.getAnnotationCodes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(codes, response.getBody());
    }

    @Test
    public void testGetAnnotationCode_Success() {
        AnnotationCode code = new AnnotationCode("1", "Code1");

        when(service.getAnnotationCode(anyString())).thenReturn(code);

        ResponseEntity<AnnotationCode> response = controller.getAnnotationCode("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(code, response.getBody());
    }

    @Test
    public void testGetAnnotationCode_NotFound() {
        when(service.getAnnotationCode(anyString())).thenReturn(null);

        ResponseEntity<AnnotationCode> response = controller.getAnnotationCode("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testAddAnnotationCode_Success() {
        AnnotationCode code = new AnnotationCode("1", "Code1");

        when(service.addAnnotationCode(any(AnnotationCode.class))).thenReturn(code);

        ResponseEntity<AnnotationCode> response = controller.addAnnotationCode(code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(code, response.getBody());
    }

    @Test
    public void testAddAnnotationCode_BadRequest() {
        when(service.addAnnotationCode(any(AnnotationCode.class))).thenReturn(null);

        AnnotationCode code = new AnnotationCode();
        ResponseEntity<AnnotationCode> response = controller.addAnnotationCode(code);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdateAnnotationCode_Success() {
        AnnotationCode code = new AnnotationCode("1", "Code1");

        when(service.updateAnnotationCode(any(AnnotationCode.class))).thenReturn(code);

        ResponseEntity<AnnotationCode> response = controller.updateAnnotationCode("1", code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(code, response.getBody());
    }

    @Test
    public void testUpdateAnnotationCode_BadRequest() {
        when(service.updateAnnotationCode(any(AnnotationCode.class))).thenReturn(null);

        AnnotationCode code = new AnnotationCode();
        ResponseEntity<AnnotationCode> response = controller.updateAnnotationCode("1", code);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteAnnotationCode_Success() {
        AnnotationCode code = new AnnotationCode("1", "Code1");

        when(service.deleteAnnotationCode(anyString())).thenReturn(code);

        ResponseEntity<AnnotationCode> response = controller.deleteAnnotationCode("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(code, response.getBody());
    }

    @Test
    public void testDeleteAnnotationCode_NotFound() {
        when(service.deleteAnnotationCode(anyString())).thenReturn(null);

        ResponseEntity<AnnotationCode> response = controller.deleteAnnotationCode("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
