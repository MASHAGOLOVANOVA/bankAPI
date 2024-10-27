package gd.testtask.golovanova.bankAPI.services;

import gd.testtask.golovanova.bankAPI.dto.BankDTO;
import gd.testtask.golovanova.bankAPI.models.Bank;
import gd.testtask.golovanova.bankAPI.repositories.BankRepository;
import gd.testtask.golovanova.bankAPI.util.BankNotFoundException;
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
public class BankServiceTest {
    @MockBean
    private BankRepository bankRepository;

    @Autowired
    private BankService bankService;

    private List<Bank> banks;


    @BeforeEach
    public void setUp() {
        banks = new ArrayList<>();

        Bank b1 = new Bank();
        b1.setId(1);
        b1.setName("bank1");
        b1.setBankIdCode("123456789");
        banks.add(b1);
        Bank b2 = new Bank();
        b2.setId(2);
        b2.setName("bank2");
        b2.setBankIdCode("987654321");
        banks.add(b2);
        Bank b3 = new Bank();
        b3.setId(3);
        b3.setName("bank3");
        b3.setBankIdCode("987654321");
        banks.add(b3);
        Bank b4 = new Bank();
        b4.setId(4);
        b4.setName("aban4");
        b4.setBankIdCode("123456789");
        banks.add(b4);

    }

    @Test
    public void findAllBanksTest() {
        when(bankRepository.findAll()).thenReturn(banks);
        List<BankDTO> receivedBanks = bankService.findAll(null, null, false, false, false);

        assertEquals(banks.size(), receivedBanks.size());
        assertIterableEquals(banks.stream().map(bankService::convertToBankDTO).collect(Collectors.toList()), receivedBanks);
        verify(bankRepository, times(1)).findAll();
    }

    @Test
    public void findAllBanksIfEmptyTest() {
        when(bankRepository.findAll()).thenReturn(Collections.emptyList());
        List<BankDTO> receivedBanks = bankService.findAll("", "", false, false, false);
        assertTrue(receivedBanks.isEmpty());

        verify(bankRepository, times(1)).findAll();
    }

    @Test
    public void findAllBanksWithExistingNameFilterTest() {
        when(bankRepository.findAll()).thenReturn(banks);
        List<BankDTO> receivedBanks = bankService.findAll("bank1", null, false, false, false);
        assertEquals(1, receivedBanks.size());
        assertEquals(bankService.convertToBankDTO(banks.get(0)), receivedBanks.get(0));
        verify(bankRepository, times(1)).findAll();
    }

    @Test
    public void findAllBanksWithNotExistingNameFilterTest() {
        when(bankRepository.findAll()).thenReturn(banks);
        List<BankDTO> receivedBanks = bankService.findAll("bank112", null, false, false, false);
        assertEquals(0, receivedBanks.size());
        verify(bankRepository, times(1)).findAll();
    }

    @Test
    public void findAllBanksWithExistingBicFilterTest() {
        when(bankRepository.findAll()).thenReturn(banks);
        List<BankDTO> receivedBanks = bankService.findAll(null, "987654321", false, false, false);
        assertEquals(2, receivedBanks.size());
        assertEquals(bankService.convertToBankDTO(banks.get(1)), receivedBanks.get(0));
        assertEquals(bankService.convertToBankDTO(banks.get(2)), receivedBanks.get(1));
        verify(bankRepository, times(1)).findAll();
    }

    @Test
    public void findAllBanksWithNotExistingBicFilterTest() {
        when(bankRepository.findAll()).thenReturn(banks);
        List<BankDTO> receivedBanks = bankService.findAll(null, "999999999", false, false, false);
        assertEquals(0, receivedBanks.size());
        verify(bankRepository, times(1)).findAll();
    }

    @Test
    public void findAllBanksWithExistingNameAndBicFilterTest() {
        when(bankRepository.findAll()).thenReturn(banks);
        List<BankDTO> receivedBanks = bankService.findAll("bank1", "123456789", false, false, false);
        assertEquals(1, receivedBanks.size());
        assertEquals(bankService.convertToBankDTO(banks.get(0)), receivedBanks.get(0));
        verify(bankRepository, times(1)).findAll();
    }

    @Test
    public void findAllBanksWithExistingNameAndNotExistingBicFilterTest() {
        when(bankRepository.findAll()).thenReturn(banks);
        List<BankDTO> receivedBanks = bankService.findAll("bank1", "099999999", false, false, false);
        assertEquals(0, receivedBanks.size());
        verify(bankRepository, times(1)).findAll();
    }

    @Test
    public void findAllBanksWithNotExistingNameAndBicFilterTest() {
        when(bankRepository.findAll()).thenReturn(banks);
        List<BankDTO> receivedBanks = bankService.findAll("bank112", "123456789", false, false, false);
        assertEquals(0, receivedBanks.size());
        verify(bankRepository, times(1)).findAll();
    }

    @Test
    public void findAllBanksWithSortByIdTest() {
        when(bankRepository.findAll()).thenReturn(banks);
        List<BankDTO> notSortedBanks = banks.stream().map(bankService::convertToBankDTO).collect(Collectors.toList());
        List<BankDTO> receivedBanks = bankService.findAll(null, null, true, false, false);
        assertEquals(banks.size(), receivedBanks.size());
        assertEquals(notSortedBanks, receivedBanks);
        verify(bankRepository, times(1)).findAll();
    }

    @Test
    public void findAllBanksWithSortByNameTest() {
        when(bankRepository.findAll()).thenReturn(banks);
        List<BankDTO> notSortedBanks = banks.stream().map(bankService::convertToBankDTO).toList();
        List<BankDTO> receivedBanks = bankService.findAll(null, null, false, true, false);

        assertEquals(notSortedBanks.get(3), receivedBanks.get(0));
        assertEquals(notSortedBanks.get(0), receivedBanks.get(1));
        assertEquals(notSortedBanks.get(1), receivedBanks.get(2));
        assertEquals(notSortedBanks.get(2), receivedBanks.get(3));

        verify(bankRepository, times(1)).findAll();
    }

    @Test
    public void findAllBanksWithSortByBicTest() {
        when(bankRepository.findAll()).thenReturn(banks);
        List<BankDTO> notSortedBanks = banks.stream().map(bankService::convertToBankDTO).toList();
        List<BankDTO> receivedBanks = bankService.findAll(null, null, false, false, true);
        assertEquals(notSortedBanks.get(0), receivedBanks.get(0));
        assertEquals(notSortedBanks.get(3), receivedBanks.get(1));
        assertEquals(notSortedBanks.get(1), receivedBanks.get(2));
        assertEquals(notSortedBanks.get(2), receivedBanks.get(3));
        verify(bankRepository, times(1)).findAll();
    }

    @Test
    public void findAllBanksWithExistingBicFilterWithSortByNameTest() {
        when(bankRepository.findAll()).thenReturn(banks);
        List<BankDTO> notSortedBanks = banks.stream().map(bankService::convertToBankDTO).toList();
        List<BankDTO> receivedBanks = bankService.findAll(null, "987654321", false, true, true);
        assertEquals(notSortedBanks.get(1), receivedBanks.get(0));
        assertEquals(notSortedBanks.get(2), receivedBanks.get(1));
        verify(bankRepository, times(1)).findAll();
    }

    @Test
    public void findOneWithExistingIdTest() {
        Bank existingBank = banks.get(0);
        when(bankRepository.findById(0)).thenReturn(Optional.of(existingBank));
        BankDTO foundBankDTO = bankService.findOne(0);
        assertEquals(bankService.convertToBankDTO(existingBank), foundBankDTO);
        verify(bankRepository, times(1)).findById(0);
    }


    @Test
    public void findOneWithNotExistingIdTest() {
        when(bankRepository.findById(7)).thenReturn(Optional.empty());
        assertThrows(BankNotFoundException.class, () -> {
            bankService.findOne(7);
        });
        verify(bankRepository, times(1)).findById(7);
    }

    @Test
    public void saveBankTest() {
        BankDTO bankDTO = new BankDTO();
        bankDTO.setName("bank5");
        bankDTO.setBankIdCode("555555555");

        when(bankRepository.save(any(Bank.class))).thenAnswer(invocation -> {
            banks.add(bankService.convertToBank(bankDTO));
            return null;
        });

        bankService.save(bankDTO);

        verify(bankRepository, times(1)).save(any(Bank.class));

        assertEquals(banks.get(4).getName(), bankDTO.getName());
        assertEquals(banks.get(4).getBankIdCode(), bankDTO.getBankIdCode());
    }

    @Test
    public void deleteBankByIdTest() {
        List<Bank> lessBanks = new ArrayList<>(banks);

        doAnswer(invocation -> {
            lessBanks.remove(0);
            return null;
        }).when(bankRepository).deleteById(anyInt());

        when(bankRepository.existsById(anyInt())).thenReturn(true);

        bankService.delete(0);


        assertEquals(banks.size() - 1, lessBanks.size());
        assertEquals(banks.get(1), lessBanks.get(0));
        assertEquals(banks.get(2), lessBanks.get(1));
        assertEquals(banks.get(3), lessBanks.get(2));
        verify(bankRepository, times(1)).deleteById(0);

    }

    @Test
    public void deleteNotExistedBankByIdTest() {
        when(bankRepository.existsById(7)).thenReturn(false);
        assertThrows(BankNotFoundException.class, () -> {
            bankService.delete(7);
        });
        verify(bankRepository, never()).deleteById(7);
    }

    @Test
    public void updateBankSuccessfully() {
        int bankId = 1;
        BankDTO bankDTO = new BankDTO();
        bankDTO.setName("New Bank Name");
        bankDTO.setBankIdCode("65432167");

        when(bankRepository.findById(bankId)).thenReturn(Optional.of(banks.get(bankId)));
        when(bankRepository.save(any(Bank.class))).thenAnswer(invocation -> {
            Bank savedBank = invocation.getArgument(0);
            banks.get(bankId).setBankIdCode(savedBank.getBankIdCode());
            banks.get(bankId).setName(savedBank.getName());
            return savedBank;
        });

        bankService.update(bankId, bankDTO);

        assertEquals("New Bank Name", banks.get(bankId).getName());
        assertEquals("65432167", banks.get(bankId).getBankIdCode());
        verify(bankRepository).findById(bankId);
        verify(bankRepository).save(any(Bank.class));
    }


    @Test
    public void updateNotExistentBankThrowsException() {
        int bankId = 11;
        BankDTO bankDTO = new BankDTO();
        bankDTO.setName("Some Bank");
        bankDTO.setBankIdCode("99999999");

        when(bankRepository.findById(bankId)).thenReturn(Optional.empty());

        assertThrows(BankNotFoundException.class, () -> {
            bankService.update(bankId, bankDTO);
        });

        verify(bankRepository).findById(bankId);
        verify(bankRepository, never()).save(any(Bank.class));
    }

}