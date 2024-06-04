package services;

import org.example.database.AnnotationCodeRepository;
import org.example.models.AnnotationCode;
import org.example.services.AnnotationCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnnotationCodeServiceTest {

    @Mock
    private AnnotationCodeRepository repo;

    @InjectMocks
    private AnnotationCodeService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAnnotationCode() {
        AnnotationCode annotationCode = new AnnotationCode();
        annotationCode.setId("1");
        when(repo.findById("1")).thenReturn(Optional.of(annotationCode));

        AnnotationCode result = service.getAnnotationCode("1");
        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    public void testGetAnnotationCode_NotFound() {
        when(repo.findById("1")).thenReturn(Optional.empty());

        AnnotationCode result = service.getAnnotationCode("1");
        assertNull(result);
    }

    @Test
    public void testGetAnnotationCodes() {
        AnnotationCode annotationCode1 = new AnnotationCode();
        AnnotationCode annotationCode2 = new AnnotationCode();
        when(repo.findAll()).thenReturn(Arrays.asList(annotationCode1, annotationCode2));

        assertEquals(2, service.getAnnotationCodes().size());
    }

    @Test
    public void testAddAnnotationCode() {
        AnnotationCode annotationCode = new AnnotationCode();
        annotationCode.setId("1");
        when(repo.save(any(AnnotationCode.class))).thenReturn(annotationCode);
        when(repo.findById("1")).thenReturn(Optional.empty());

        AnnotationCode result = service.addAnnotationCode(annotationCode);
        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    public void testAddAnnotationCode_NullInput() {
        AnnotationCode result = service.addAnnotationCode(null);
        assertNull(result);
    }

    @Test
    public void testAddAnnotationCode_AlreadyExists() {
        AnnotationCode annotationCode = new AnnotationCode();
        annotationCode.setId("1");
        when(repo.findById("1")).thenReturn(Optional.of(annotationCode));

        AnnotationCode result = service.addAnnotationCode(annotationCode);
        assertNull(result);
    }

    @Test
    public void testUpdateAnnotationCode() {
        AnnotationCode annotationCode = new AnnotationCode();
        annotationCode.setId("1");
        when(repo.save(any(AnnotationCode.class))).thenReturn(annotationCode);

        AnnotationCode result = service.updateAnnotationCode(annotationCode);
        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    public void testUpdateAnnotationCode_NullInput() {
        AnnotationCode result = service.updateAnnotationCode(null);
        assertNull(result);
    }

    @Test
    public void testDeleteAnnotationCode() {
        AnnotationCode annotationCode = new AnnotationCode();
        annotationCode.setId("1");
        when(repo.findById("1")).thenReturn(Optional.of(annotationCode));

        AnnotationCode result = service.deleteAnnotationCode("1");
        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(repo, times(1)).deleteById("1");
    }

    @Test
    public void testDeleteAnnotationCode_NotFound() {
        when(repo.findById("1")).thenReturn(Optional.empty());

        AnnotationCode result = service.deleteAnnotationCode("1");
        assertNull(result);
        verify(repo, never()).deleteById("1");
    }
}
