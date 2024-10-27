package gd.testtask.golovanova.bankAPI.services;



import gd.testtask.golovanova.bankAPI.dto.DepositDTO;
import gd.testtask.golovanova.bankAPI.models.Bank;
import gd.testtask.golovanova.bankAPI.models.Client;
import gd.testtask.golovanova.bankAPI.models.Deposit;
import gd.testtask.golovanova.bankAPI.repositories.BankRepository;
import gd.testtask.golovanova.bankAPI.repositories.ClientRepository;
import gd.testtask.golovanova.bankAPI.repositories.DepositRepository;
import gd.testtask.golovanova.bankAPI.util.BankNotFoundException;
import gd.testtask.golovanova.bankAPI.util.ClientNotFoundException;
import gd.testtask.golovanova.bankAPI.util.DepositNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
public class DepositServiceTest {

    @MockBean
    private DepositRepository depositRepository;

    @Autowired
    private DepositService depositService;

    private List<Deposit> depositList;

    private List<Bank> bankList;

    private List<Client> clientList;

    @MockBean
    private BankRepository bankRepository;

    @MockBean
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        depositList = new ArrayList<>();
        bankList = new ArrayList<>();
        clientList = new ArrayList<>();

        Bank b1 = new Bank();
        b1.setId(1);
        b1.setName("b1");
        b1.setBankIdCode("123456789");
        b1.setDeposits(new ArrayList<>());
        bankList.add(b1);
        Bank b2 = new Bank();
        b2.setId(2);
        b2.setName("b2");
        b2.setBankIdCode("123456799");
        b2.setDeposits(new ArrayList<>());
        bankList.add(b2);

        Client c1 = new Client();
        c1.setId(1);
        c1.setName("c1");
        c1.setShortName("c");
        c1.setAddress("123");
        c1.setDeposits(new ArrayList<>());
        clientList.add(c1);
        Client c2 = new Client();
        c2.setId(2);
        c2.setName("c2");
        c2.setShortName("c");
        c2.setAddress("456");
        c2.setDeposits(new ArrayList<>());
        clientList.add(c2);


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -2);
        Date beforeYesterday = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date yesterday = calendar.getTime();
        calendar.setTime(new Date());
        Date today = calendar.getTime();

        Deposit deposit1 = new Deposit();
        deposit1.setId(1);
        deposit1.setClient(c1);
        deposit1.setBank(b1);
        deposit1.setCreateDate(today);
        deposit1.setPeriod(4);
        deposit1.setPercentage(12);
        depositList.add(deposit1);
        Deposit deposit2 = new Deposit();
        deposit2.setId(2);
        deposit2.setClient(c2);
        deposit2.setBank(b2);
        deposit2.setCreateDate(beforeYesterday);
        deposit2.setPeriod(3);
        deposit2.setPercentage(10);
        depositList.add(deposit2);
        Deposit deposit3 = new Deposit();
        deposit3.setId(3);
        deposit3.setClient(c1);
        deposit3.setBank(b2);
        deposit3.setCreateDate(yesterday);
        deposit3.setPeriod(3);
        deposit3.setPercentage(12);
        depositList.add(deposit3);

    }

    @Test
    public void findAllDepositsTest() {
        when(depositRepository.findAll()).thenReturn(depositList);
        List<DepositDTO> receivedDeposits = depositService.findAll(null, null,  false, false, false, false);

        assertEquals(depositList.size(), receivedDeposits.size());
        assertIterableEquals(depositList.stream().map(depositService::convertToDepositDTO).collect(Collectors.toList()), receivedDeposits);
        verify(depositRepository, times(1)).findAll();
    }

    @Test
    public void findAllDepositsIfEmptyTest() {
        when(depositRepository.findAll()).thenReturn(Collections.emptyList());
        List<DepositDTO> receivedClients = depositService.findAll(null, null,  false, false, false, false);
        assertTrue(receivedClients.isEmpty());
        verify(depositRepository, times(1)).findAll();
    }

    @Test
    public void findAllDepositsWithExistingPercentageFilterTest() {
        when(depositRepository.findAll()).thenReturn(depositList);
        List<DepositDTO> receivedDeposits = depositService.findAll(12, null, false, false, false, false);
        assertEquals(2, receivedDeposits.size());
        assertEquals(depositService.convertToDepositDTO(depositList.get(0)), receivedDeposits.get(0));
        assertEquals(depositService.convertToDepositDTO(depositList.get(2)), receivedDeposits.get(1));
        verify(depositRepository, times(1)).findAll();
    }

    @Test
    public void findAllDepositsWithExistingPeriodFilterTest() {
        when(depositRepository.findAll()).thenReturn(depositList);
        List<DepositDTO> receivedDeposits = depositService.findAll(null, 3, false, false, false, false);
        assertEquals(2, receivedDeposits.size());
        assertEquals(depositService.convertToDepositDTO(depositList.get(1)), receivedDeposits.get(0));
        assertEquals(depositService.convertToDepositDTO(depositList.get(2)), receivedDeposits.get(1));
        verify(depositRepository, times(1)).findAll();
    }

    @Test
    public void findAllDepositsWithNotExistingPercentageFilterTest() {
        when(depositRepository.findAll()).thenReturn(depositList);
        List<DepositDTO> receivedBanks = depositService.findAll(12345678, null, false,false, false, false);
        assertEquals(0, receivedBanks.size());
        verify(depositRepository, times(1)).findAll();
    }

    @Test
    public void findAllDepositsWithNotExistingPeriodFilterTest() {
        when(depositRepository.findAll()).thenReturn(depositList);
        List<DepositDTO> receivedBanks = depositService.findAll(null, 0, false,false, false, false);
        assertEquals(0, receivedBanks.size());
        verify(depositRepository, times(1)).findAll();
    }

    @Test
    public void findAllDepositsWithExistingPeriodAndPercentageFilterTest() {
        when(depositRepository.findAll()).thenReturn(depositList);
        List<DepositDTO> receivedDeposits = depositService.findAll(12, 3, false,false, false, false);
        assertEquals(1, receivedDeposits.size());
        assertEquals(depositService.convertToDepositDTO(depositList.get(2)), receivedDeposits.get(0));
        verify(depositRepository, times(1)).findAll();
    }

    @Test
    public void findAllDepositsWithNotExistingPeriodAndPercentageFilterTest() {
        when(depositRepository.findAll()).thenReturn(depositList);
        List<DepositDTO> receivedDeposits = depositService.findAll(1, 30, false,false, false, false);
        assertEquals(0, receivedDeposits.size());
        verify(depositRepository, times(1)).findAll();
    }

    @Test
    public void findAllDepositsWithSortByIdTest() {
        when(depositRepository.findAll()).thenReturn(depositList);
        List<DepositDTO> notSortedDeposits = depositList.stream().map(depositService::convertToDepositDTO).collect(Collectors.toList());
        List<DepositDTO> receivedDeposits = depositService.findAll(null, null, true, false,false, false);
        assertEquals(depositList.size(), receivedDeposits.size());
        assertEquals(notSortedDeposits, receivedDeposits);
        verify(depositRepository, times(1)).findAll();
    }

    @Test
    public void findAllDepositsWithSortByPercentageTest() {
        when(depositRepository.findAll()).thenReturn(depositList);
        List<DepositDTO> notSortedDeposits = depositList.stream().map(depositService::convertToDepositDTO).toList();
        List<DepositDTO> receivedDeposits = depositService.findAll(null, null, false, true, false,false);

        assertEquals(notSortedDeposits.get(1), receivedDeposits.get(0));
        assertEquals(notSortedDeposits.get(0), receivedDeposits.get(1));
        assertEquals(notSortedDeposits.get(2), receivedDeposits.get(2));

        verify(depositRepository, times(1)).findAll();
    }

    @Test
    public void findAllDepositsWithSortByPeriodTest() {
        when(depositRepository.findAll()).thenReturn(depositList);
        List<DepositDTO> notSortedDeposits = depositList.stream().map(depositService::convertToDepositDTO).toList();
        List<DepositDTO> receivedDeposits = depositService.findAll(null, null, false, false, true,false);

        assertEquals(notSortedDeposits.get(1), receivedDeposits.get(0));
        assertEquals(notSortedDeposits.get(2), receivedDeposits.get(1));
        assertEquals(notSortedDeposits.get(0), receivedDeposits.get(2));

        verify(depositRepository, times(1)).findAll();
    }

    @Test
    public void findAllDepositsWithSortByCreateDateTest() {
        when(depositRepository.findAll()).thenReturn(depositList);
        List<DepositDTO> notSortedDeposits = depositList.stream().map(depositService::convertToDepositDTO).toList();
        List<DepositDTO> receivedDeposits = depositService.findAll(null, null, false, false, false,true);

        assertEquals(notSortedDeposits.get(1), receivedDeposits.get(0));
        assertEquals(notSortedDeposits.get(2), receivedDeposits.get(1));
        assertEquals(notSortedDeposits.get(0), receivedDeposits.get(2));

        verify(depositRepository, times(1)).findAll();
    }

    @Test
    public void findOneWithExistingIdTest() {
        Deposit existingDeposit = depositList.get(0);
        when(depositRepository.findById(0)).thenReturn(Optional.of(existingDeposit));
        DepositDTO foundDepositDTO = depositService.findOne(0);
        assertEquals(depositService.convertToDepositDTO(existingDeposit), foundDepositDTO);
        verify(depositRepository, times(1)).findById(0);
    }

    @Test
    public void findOneWithNotExistingIdTest() {
        when(depositRepository.findById(7)).thenReturn(Optional.empty());
        assertThrows(DepositNotFoundException.class, () -> {
            depositService.findOne(7);
        });
        verify(depositRepository, times(1)).findById(7);
    }

    @Test
    public void createDepositTest(){
        DepositDTO d = new DepositDTO();
        d.setPercentage(12);
        d.setPeriod(13);
        d.setBank_id(bankList.get(0).getId());
        d.setClient_id(clientList.get(0).getId());

        when(bankRepository.findById(anyInt())).thenReturn(Optional.of(bankList.get(0)));
        when(clientRepository.findById(anyInt())).thenReturn(Optional.of(clientList.get(0)));
        when(depositRepository.save(any(Deposit.class))).thenAnswer(invocation -> {
            Deposit deposit = invocation.getArgument(0);
            deposit.setId(4);
            deposit.setClient(clientList.get(0));
            deposit.setBank(bankList.get(0));
            depositList.add(deposit);
            return null;
        });

        depositService.save(d);

        verify(depositRepository, times(1)).save(any(Deposit.class));

        assertEquals(depositList.get(3).getPercentage(), d.getPercentage());
        assertEquals(depositList.get(3).getPeriod(), d.getPeriod());
        assertEquals(depositList.get(3).getBank().getId(), d.getBank_id());
        assertEquals(depositList.get(3).getClient().getId(), d.getClient_id());
    }


    @Test
    public void deleteDepositByIdTest() {
        List<Deposit> lessDeposits = new ArrayList<>(depositList);

        doAnswer(invocation -> {
            lessDeposits.remove(0);
            return null;
        }).when(depositRepository).deleteById(anyInt());

        when(depositRepository.existsById(anyInt())).thenReturn(true);

        depositService.delete(0);


        assertEquals(depositList.size() - 1, lessDeposits.size());
        assertEquals(depositList.get(1), lessDeposits.get(0));
        assertEquals(depositList.get(2), lessDeposits.get(1));
        verify(depositRepository, times(1)).deleteById(0);

    }

    @Test
    public void deleteNotExistedDepositByIdTest() {
        when(depositRepository.existsById(7)).thenReturn(false);
        assertThrows(DepositNotFoundException.class, () -> {
            depositService.delete(7);
        });
        verify(depositRepository, never()).deleteById(7);
    }


    @Test
    public void updateDepositSuccessfully() {
        int depositId=1;
        DepositDTO depositDTO = new DepositDTO();
        depositDTO.setBank_id(bankList.get(0).getId());
        depositDTO.setClient_id(clientList.get(0).getId());
        depositDTO.setPeriod(13);
        depositDTO.setPercentage(12);

        when(depositRepository.findById(1)).thenReturn(Optional.of(depositList.get(depositId)));
        when(bankRepository.findById(anyInt())).thenReturn(Optional.of(bankList.get(0)));
        when(clientRepository.findById(anyInt())).thenReturn(Optional.of(clientList.get(0)));
        when(depositRepository.save(any(Deposit.class))).thenAnswer(invocation -> {
            Deposit savedDeposit = invocation.getArgument(0);
            depositList.get(depositId).setPeriod(savedDeposit.getPeriod());
            depositList.get(depositId).setPercentage(savedDeposit.getPercentage());
            depositList.get(depositId).setBank(savedDeposit.getBank());
            depositList.get(depositId).setClient(savedDeposit.getClient());
            return savedDeposit;
        });

        depositService.update(depositId, depositDTO);

        assertEquals(12, depositList.get(depositId).getPercentage());
        assertEquals(13, depositList.get(depositId).getPeriod());
        assertEquals(1, depositList.get(depositId).getBank().getId());
        assertEquals(1, depositList.get(depositId).getClient().getId());
        verify(depositRepository).findById(depositId);
        verify(depositRepository).save(any(Deposit.class));
    }


    @Test
    public void updateNotExistentDepositThrowsException() {
        int depositId=10;
        DepositDTO depositDTO = new DepositDTO();
        depositDTO.setBank_id(bankList.get(0).getId());
        depositDTO.setClient_id(clientList.get(0).getId());
        depositDTO.setPeriod(13);
        depositDTO.setPercentage(12);

        when(depositRepository.findById(depositId)).thenReturn(Optional.empty());

        assertThrows(DepositNotFoundException.class, () -> {
            depositService.update(depositId, depositDTO);
        });

        verify(depositRepository).findById(depositId);
        verify(depositRepository, never()).save(any(Deposit.class));
    }

    @Test
    public void updateDeposit_BankNotFoundExceptionTest() {
        int depositId=0;
        DepositDTO depositDTO = new DepositDTO();
        depositDTO.setBank_id(bankList.get(0).getId());
        depositDTO.setClient_id(clientList.get(0).getId());
        depositDTO.setPeriod(13);
        depositDTO.setPercentage(12);

        when(depositRepository.findById(depositId)).thenReturn(Optional.of(depositList.get(0)));
        when(bankRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(BankNotFoundException.class, () -> {
            depositService.update(depositId, depositDTO);
        });

        verify(depositRepository).findById(depositId);
        verify(depositRepository, never()).save(any(Deposit.class));
    }

    @Test
    public void updateDeposit_ClientNotFoundExceptionTest() {
        int depositId=0;
        DepositDTO depositDTO = new DepositDTO();
        depositDTO.setBank_id(bankList.get(0).getId());
        depositDTO.setClient_id(clientList.get(0).getId());
        depositDTO.setPeriod(13);
        depositDTO.setPercentage(12);

        when(depositRepository.findById(depositId)).thenReturn(Optional.of(depositList.get(0)));
        when(bankRepository.findById(anyInt())).thenReturn(Optional.of(bankList.get(0)));
        when(clientRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ClientNotFoundException.class, () -> {
            depositService.update(depositId, depositDTO);
        });

        verify(depositRepository).findById(depositId);
        verify(depositRepository, never()).save(any(Deposit.class));
    }



}
