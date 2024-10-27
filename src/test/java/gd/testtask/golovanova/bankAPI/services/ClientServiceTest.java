package gd.testtask.golovanova.bankAPI.services;

import gd.testtask.golovanova.bankAPI.dto.ClientDTO;
import gd.testtask.golovanova.bankAPI.models.Client;
import gd.testtask.golovanova.bankAPI.models.LegalForm;
import gd.testtask.golovanova.bankAPI.repositories.ClientRepository;
import gd.testtask.golovanova.bankAPI.util.ClientNotFoundException;
import gd.testtask.golovanova.bankAPI.util.LegalFormNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
public class ClientServiceTest {
    @MockBean
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    private List<Client> clients;

    private List<LegalForm> legalForms;

    @MockBean
    private LegalFormService legalFormService;

    @BeforeEach
    public void setUp() {
        clients = new ArrayList<>();
        legalForms = new ArrayList<>();

        LegalForm legalForm = new LegalForm();
        legalForm.setId(1);
        legalForm.setName("lf1");
        legalForm.setClients(new ArrayList<>());
        legalForms.add(legalForm);
        LegalForm legalForm1 = new LegalForm();
        legalForm1.setId(2);
        legalForm1.setName("lf2");
        legalForm1.setClients(new ArrayList<>());
        legalForms.add(legalForm1);

        Client c1 = new Client();
        c1.setId(1);
        c1.setName("client1");
        c1.setShortName("c1");
        c1.setAddress("c1_address");
        c1.setLegalForm(legalForms.get(0));
        c1.getLegalForm().getClients().add(c1);
        clients.add(c1);
        Client c2 = new Client();
        c2.setId(2);
        c2.setName("client2");
        c2.setShortName("c2");
        c2.setAddress("c2_address");
        c2.setLegalForm(legalForms.get(0));
        c2.getLegalForm().getClients().add(c2);
        clients.add(c2);
        Client c3 = new Client();
        c3.setId(3);
        c3.setName("client3");
        c3.setShortName("c3");
        c3.setAddress("a3_address");
        c3.setLegalForm(legalForms.get(1));
        c3.getLegalForm().getClients().add(c3);
        clients.add(c3);
        Client c4 = new Client();
        c4.setId(4);
        c4.setName("alient4");
        c4.setShortName("a4");
        c4.setAddress("c4_address");
        c4.setLegalForm(legalForms.get(1));
        c4.getLegalForm().getClients().add(c4);
        clients.add(c4);

    }

    @Test
    public void findAllClientsTest() {
        when(clientRepository.findAll()).thenReturn(clients);
        List<ClientDTO> receivedClients = clientService.findAll(null, null, null, false, false, false, false);

        assertEquals(clients.size(), receivedClients.size());
        assertIterableEquals(clients.stream().map(clientService::convertToClientDTO).collect(Collectors.toList()), receivedClients);
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void findAllClientsIfEmptyTest() {
        when(clientRepository.findAll()).thenReturn(Collections.emptyList());
        List<ClientDTO> receivedClients = clientService.findAll("", "", "", false, false, false, false);
        assertTrue(receivedClients.isEmpty());

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void findAllClientsWithExistingNameFilterTest() {
        when(clientRepository.findAll()).thenReturn(clients);
        List<ClientDTO> receivedClients = clientService.findAll("client1", "", null, false, false, false, false);
        assertEquals(1, receivedClients.size());
        assertEquals(clientService.convertToClientDTO(clients.get(0)), receivedClients.get(0));
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void findAllClientsWithNotExistingNameFilterTest() {
        when(clientRepository.findAll()).thenReturn(clients);
        List<ClientDTO> receivedClients = clientService.findAll("client112", null, "", false, false, false, false);
        assertEquals(0, receivedClients.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void findAllClientsWithExistingShortNameFilterTest() {
        when(clientRepository.findAll()).thenReturn(clients);
        List<ClientDTO> receivedClients = clientService.findAll("", "c1", null, false, false, false, false);
        assertEquals(1, receivedClients.size());
        assertEquals(clientService.convertToClientDTO(clients.get(0)), receivedClients.get(0));
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void findAllClientsWithNotExistingShortNameFilterTest() {
        when(clientRepository.findAll()).thenReturn(clients);
        List<ClientDTO> receivedClients = clientService.findAll("", "c231", "", false, false, false, false);
        assertEquals(0, receivedClients.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void findAllClientsWithExistingAddressFilterTest() {
        when(clientRepository.findAll()).thenReturn(clients);
        List<ClientDTO> receivedClients = clientService.findAll("", "", "c1_address", false, false, false, false);
        assertEquals(1, receivedClients.size());
        assertEquals(clientService.convertToClientDTO(clients.get(0)), receivedClients.get(0));
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void findAllClientsWithNotExistingAddressFilterTest() {
        when(clientRepository.findAll()).thenReturn(clients);
        List<ClientDTO> receivedClients = clientService.findAll("", "", "c1adddddress", false, false, false, false);
        assertEquals(0, receivedClients.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void findAllClientsWithExistingNameAndShortNameFilterTest() {
        when(clientRepository.findAll()).thenReturn(clients);
        List<ClientDTO> receivedClients = clientService.findAll("client1", "c1", null, false, false, false, false);
        assertEquals(1, receivedClients.size());
        assertEquals(clientService.convertToClientDTO(clients.get(0)), receivedClients.get(0));
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void findAllClientsWithExistingNameAndNotExistingShortNameTest() {
        when(clientRepository.findAll()).thenReturn(clients);
        List<ClientDTO> receivedClients = clientService.findAll("client1", "client", null, false, false, false, false);
        assertEquals(0, receivedClients.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void findAllClientsWithSortByIdTest() {
        when(clientRepository.findAll()).thenReturn(clients);
        List<ClientDTO> notSortedClients = clients.stream().map(clientService::convertToClientDTO).toList();
        List<ClientDTO> receivedClients = clientService.findAll(null, null, null, true, false, false, false);
        assertEquals(clients.size(), receivedClients.size());
        assertEquals(notSortedClients, receivedClients);
        verify(clientRepository, times(1)).findAll();
    }


    @Test
    public void findAllClientsWithSortByNameTest() {
        when(clientRepository.findAll()).thenReturn(clients);
        List<ClientDTO> notSortedClients = clients.stream().map(clientService::convertToClientDTO).toList();
        List<ClientDTO> receivedClients = clientService.findAll(null, null, null, false, true, false, false);

        assertEquals(notSortedClients.get(3), receivedClients.get(0));
        assertEquals(notSortedClients.get(0), receivedClients.get(1));
        assertEquals(notSortedClients.get(1), receivedClients.get(2));
        assertEquals(notSortedClients.get(2), receivedClients.get(3));

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void findAllClientsWithSortByShortNameTest() {
        when(clientRepository.findAll()).thenReturn(clients);
        List<ClientDTO> notSortedClients = clients.stream().map(clientService::convertToClientDTO).toList();
        List<ClientDTO> receivedClients = clientService.findAll(null, null, null, false, false, true, false);

        assertEquals(notSortedClients.get(3), receivedClients.get(0));
        assertEquals(notSortedClients.get(0), receivedClients.get(1));
        assertEquals(notSortedClients.get(1), receivedClients.get(2));
        assertEquals(notSortedClients.get(2), receivedClients.get(3));

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void findAllClientsWithSortByAddressTest() {
        when(clientRepository.findAll()).thenReturn(clients);
        List<ClientDTO> notSortedClients = clients.stream().map(clientService::convertToClientDTO).toList();
        List<ClientDTO> receivedClients = clientService.findAll(null, null, null, false, false, false, true);
        assertEquals(notSortedClients.get(2), receivedClients.get(0));
        assertEquals(notSortedClients.get(0), receivedClients.get(1));
        assertEquals(notSortedClients.get(1), receivedClients.get(2));
        assertEquals(notSortedClients.get(3), receivedClients.get(3));

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void findOneWithExistingIdTest() {
        Client existingClient = clients.get(0);
        when(clientRepository.findById(0)).thenReturn(Optional.of(existingClient));
        ClientDTO foundClientDTO = clientService.findOne(0);
        assertEquals(clientService.convertToClientDTO(existingClient), foundClientDTO);
        verify(clientRepository, times(1)).findById(0);
    }


    @Test
    public void findOneWithNotExistingIdTest() {
        when(clientRepository.findById(7)).thenReturn(Optional.empty());
        assertThrows(ClientNotFoundException.class, () -> {
            clientService.findOne(7);
        });
        verify(clientRepository, times(1)).findById(7);
    }

    @Test
    public void saveClientTest() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("c555555555");
        clientDTO.setShortName("c55");
        clientDTO.setAddress("address");
        clientDTO.setLegalFormId(1);


        when(legalFormService.findOne(anyInt())).thenReturn(legalForms.get(0));
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client c = clientService.convertToClient(clientDTO);
            c.setLegalForm(legalForms.get(clientDTO.getLegalFormId() - 1));
            clients.add(c);
            return null;
        });

        clientService.save(clientDTO);
        verify(clientRepository, times(1)).save(any(Client.class));
        assertEquals(clients.get(4).getName(), clientDTO.getName());
        assertEquals(clients.get(4).getShortName(), clientDTO.getShortName());
        assertEquals(clients.get(4).getAddress(), clientDTO.getAddress());
        assertEquals(clients.get(4).getLegalForm().getId(), clientDTO.getLegalFormId());
    }

    @Test
    public void saveClient_LegalFormNotFoundExceptionTest() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("c555555555");
        clientDTO.setShortName("c55");
        clientDTO.setAddress("address");
        clientDTO.setLegalFormId(3);

        when(legalFormService.findOne(anyInt())).thenThrow(new LegalFormNotFoundException());


        assertThrows(LegalFormNotFoundException.class, () -> {
            clientService.save(clientDTO);
        });


        verify(clientRepository, never()).save(any(Client.class));
    }


    @Test
    public void deleteClientByIdTest() {
        List<Client> lessClients = new ArrayList<>(clients);

        doAnswer(invocation -> {
            lessClients.remove(0);
            return null;
        }).when(clientRepository).deleteById(anyInt());

        when(clientRepository.existsById(anyInt())).thenReturn(true);

        clientService.delete(0);


        assertEquals(clients.size() - 1, lessClients.size());
        assertEquals(clients.get(1), lessClients.get(0));
        assertEquals(clients.get(2), lessClients.get(1));
        assertEquals(clients.get(3), lessClients.get(2));
        verify(clientRepository, times(1)).deleteById(0);

    }

    @Test
    public void deleteNotExistedClientByIdTest() {
        when(clientRepository.existsById(7)).thenReturn(false);
        assertThrows(ClientNotFoundException.class, () -> {
            clientService.delete(7);
        });
        verify(clientRepository, never()).deleteById(7);
    }


    @Test
    public void updateClientSuccessfully() {
        int clientId = 1;
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("client new");
        clientDTO.setShortName("c new");
        clientDTO.setAddress("c adress new");
        clientDTO.setLegalFormId(1);

        when(legalFormService.findOne(anyInt())).thenReturn(legalForms.get(0));
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(clients.get(clientId)));
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client savedClient = invocation.getArgument(0);
            clients.get(clientId).setShortName(savedClient.getShortName());
            clients.get(clientId).setName(savedClient.getName());
            clients.get(clientId).setAddress(savedClient.getAddress());
            clients.get(clientId).setLegalForm(savedClient.getLegalForm());
            return savedClient;
        });

        clientService.update(clientId, clientDTO);

        assertEquals("client new", clients.get(clientId).getName());
        assertEquals("c new", clients.get(clientId).getShortName());
        assertEquals("c adress new", clients.get(clientId).getAddress());
        assertEquals(1, clients.get(clientId).getLegalForm().getId());
        verify(clientRepository).findById(clientId);
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    public void updateNotExistentClientThrowsException() {
        int clientId = 1;
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("client new");
        clientDTO.setShortName("c new");
        clientDTO.setAddress("c adress new");
        clientDTO.setLegalFormId(1);

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> {
            clientService.update(clientId, clientDTO);
        });

        verify(clientRepository).findById(clientId);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    public void updateClientWithNotExistedLegalFormThrowsException() {
        int clientId = 1;
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("client new");
        clientDTO.setShortName("c new");
        clientDTO.setAddress("c adress new");
        clientDTO.setLegalFormId(5);


        when(legalFormService.findOne(anyInt())).thenThrow(new LegalFormNotFoundException());
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(clients.get(0)));

        assertThrows(LegalFormNotFoundException.class, () -> {
            clientService.update(clientId, clientDTO);
        });

        verify(clientRepository).findById(clientId);
        verify(clientRepository, never()).save(any(Client.class));
    }

}
