package no.fintlabs.resource.personopplysning;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonopplysningControllerTest {

    @Test
    public void testGetPersonopplysning() {
        PersonopplysningService mockService = Mockito.mock(PersonopplysningService.class);

        Personopplysning person1 = new Personopplysning("123", "25", "Test");
        Personopplysning person2 = new Personopplysning("123", "30", "Test");
        List<Personopplysning> expectedList = Arrays.asList(person1, person2);

        Mockito.when(mockService.getPersonopplysning()).thenReturn(expectedList);

        PersonopplysningController controller = new PersonopplysningController(mockService);

        ResponseEntity<List<Personopplysning>> responseEntity = controller.getPersonopplysning();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedList, responseEntity.getBody());
    }

}