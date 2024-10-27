package gd.testtask.golovanova.bankAPI.services;


import gd.testtask.golovanova.bankAPI.models.LegalForm;
import gd.testtask.golovanova.bankAPI.repositories.LegalFormRepository;
import gd.testtask.golovanova.bankAPI.util.LegalFormNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
public class LegalFormServiceTest {

    @Autowired
    private LegalFormService legalFormService;

    private List<LegalForm> legalForms;

    @MockBean
    private LegalFormRepository legalFormRepository;

    @BeforeEach
    public void setUp() {

        legalForms = new ArrayList<>();
        LegalForm legalForm1 = new LegalForm();
        legalForm1.setId(1);
        legalForm1.setName("Legal Form 1");
        legalForm1.setClients(new ArrayList<>());
        legalForms.add(legalForm1);
        LegalForm legalForm2 = new LegalForm();
        legalForm2.setId(2);
        legalForm2.setName("Legal Form 2");
        legalForm2.setClients(new ArrayList<>());
        legalForms.add(legalForm2);
    }

    @Test
    public void findAllLegalFormsTest() {
        when(legalFormRepository.findAll()).thenReturn(legalForms);
        List<LegalForm> receivedLegalForms = legalFormService.findAll();
        assertEquals(legalForms.size(), receivedLegalForms.size());
        assertIterableEquals(legalForms, receivedLegalForms);
        verify(legalFormRepository, times(1)).findAll();
    }

    @Test
    public void findOneWithExistingIdTest() {
        LegalForm existingLegalForm = legalForms.get(0);
        when(legalFormRepository.findById(0)).thenReturn(Optional.of(existingLegalForm));
        LegalForm foundLegalForm = legalFormService.findOne(0);
        assertEquals(existingLegalForm, foundLegalForm);
        verify(legalFormRepository, times(1)).findById(0);
    }


    @Test
    public void findOneWithNotExistingIdTest() {
        when(legalFormRepository.findById(7)).thenReturn(Optional.empty());
        assertThrows(LegalFormNotFoundException.class, () -> {
            legalFormService.findOne(7);
        });
        verify(legalFormRepository, times(1)).findById(7);
    }


}
