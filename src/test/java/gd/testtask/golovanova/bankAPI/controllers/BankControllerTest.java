package gd.testtask.golovanova.bankAPI.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gd.testtask.golovanova.bankAPI.dto.BankDTO;
import gd.testtask.golovanova.bankAPI.services.BankService;
import gd.testtask.golovanova.bankAPI.util.BankNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
public class BankControllerTest {
    private final MockMvc mockMvc;

    @MockBean
    private BankService bankService;

    private BankDTO bankDTO;

    private List<BankDTO> bankDTOList;

    private final ObjectMapper objectMapper;


    @Autowired
    public BankControllerTest(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    public void setUp() {
        bankDTOList = new ArrayList<>();
        bankDTO = new BankDTO();
        BankDTO bankDTO1 = new BankDTO();
        bankDTO1.setName("Bank1");
        bankDTO1.setBankIdCode("123456789");
        bankDTOList.add(bankDTO1);
        BankDTO bankDTO2 = new BankDTO();
        bankDTO2.setName("Bank2");
        bankDTO2.setBankIdCode("123456788");
        bankDTOList.add(bankDTO2);
        BankDTO bankDTO3 = new BankDTO();
        bankDTO3.setName("Bank3");
        bankDTO3.setBankIdCode("123456768");
        bankDTOList.add(bankDTO3);

    }

    @Test
    public void getAllBanksTest() throws Exception {
        when(bankService.findAll("", "", false, false, false)).thenReturn(bankDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/banks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].name").value(bankDTOList.get(0).getName()))
                .andExpect(jsonPath("$[0].bankIdCode").value(bankDTOList.get(0).getBankIdCode()))

                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].name").value(bankDTOList.get(1).getName()))
                .andExpect(jsonPath("$[1].bankIdCode").value(bankDTOList.get(1).getBankIdCode()))

                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].name").value(bankDTOList.get(2).getName()))
                .andExpect(jsonPath("$[2].bankIdCode").value(bankDTOList.get(2).getBankIdCode())

                );

        verify(bankService, times(1)).findAll("", "", false, false, false);
    }

    @Test
    public void getAllBanksEmptyTest() throws Exception {
        when(bankService.findAll("", "", false, false, false)).thenReturn(emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/banks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0))
                );

        verify(bankService, times(1)).findAll("", "", false, false, false);
    }

    @Test
    public void getAllBanksFilterByNameTest() throws Exception {
        when(bankService.findAll("Bank1", "", false, false, false)).thenReturn(List.of(bankDTOList.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/banks")
                        .param("filter_by_name", "Bank1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].name").value(bankDTOList.get(0).getName()))
                .andExpect(jsonPath("$[0].bankIdCode").value(bankDTOList.get(0).getBankIdCode())
                );

        verify(bankService, times(1)).findAll("Bank1", "", false, false, false);
    }

    @Test
    public void getAllBanksFilterByBicTest() throws Exception {
        when(bankService.findAll("", "123456788", false, false, false)).thenReturn(List.of(bankDTOList.get(1)));

        mockMvc.perform(MockMvcRequestBuilders.get("/banks")
                        .param("filter_by_bankIdCode", "123456788"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].name").value(bankDTOList.get(1).getName()))
                .andExpect(jsonPath("$[0].bankIdCode").value(bankDTOList.get(1).getBankIdCode())
                );

        verify(bankService, times(1)).findAll("", "123456788", false, false, false);
    }

    @Test
    public void getAllBanksSortByIdTest() throws Exception {
        when(bankService.findAll("", "", true, false, false)).thenReturn(bankDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/banks")
                        .param("sort_by_id", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].name").value(bankDTOList.get(0).getName()))
                .andExpect(jsonPath("$[0].bankIdCode").value(bankDTOList.get(0).getBankIdCode()))

                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].name").value(bankDTOList.get(1).getName()))
                .andExpect(jsonPath("$[1].bankIdCode").value(bankDTOList.get(1).getBankIdCode()))

                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].name").value(bankDTOList.get(2).getName()))
                .andExpect(jsonPath("$[2].bankIdCode").value(bankDTOList.get(2).getBankIdCode())
                );

        verify(bankService, times(1)).findAll("", "", true, false, false);
    }

    @Test
    public void getAllBanksSortByNameTest() throws Exception {
        BankDTO bankDTO1 = new BankDTO();
        bankDTO1.setName("Aank1");
        bankDTO1.setBankIdCode("123456789");
        bankDTOList.add(0, bankDTO1);

        when(bankService.findAll("", "", false, true, false)).thenReturn(bankDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/banks")
                        .param("sort_by_name", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)))

                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].name").value(bankDTOList.get(0).getName()))
                .andExpect(jsonPath("$[0].bankIdCode").value(bankDTOList.get(0).getBankIdCode()))

                .andExpect(jsonPath("$[1].name").value(bankDTOList.get(1).getName()))
                .andExpect(jsonPath("$[1].bankIdCode").value(bankDTOList.get(1).getBankIdCode()))

                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].name").value(bankDTOList.get(2).getName()))
                .andExpect(jsonPath("$[2].bankIdCode").value(bankDTOList.get(2).getBankIdCode()))

                .andExpect(jsonPath("$[3].id").doesNotExist())
                .andExpect(jsonPath("$[3].name").value(bankDTOList.get(3).getName()))
                .andExpect(jsonPath("$[3].bankIdCode").value(bankDTOList.get(3).getBankIdCode())
                );

        verify(bankService, times(1)).findAll("", "", false, true, false);
    }

    @Test
    public void getAllBanksSortByBicTest() throws Exception {
        BankDTO temp = bankDTOList.get(0); // 1-й элемент
        bankDTOList.set(0, bankDTOList.get(2)); // Установка 3-го элемента на 1-ю позицию
        bankDTOList.set(2, temp);

        when(bankService.findAll("", "", false, false, true)).thenReturn(bankDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/banks")
                        .param("sort_by_bank_id_code", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].name").value(bankDTOList.get(0).getName()))
                .andExpect(jsonPath("$[0].bankIdCode").value(bankDTOList.get(0).getBankIdCode()))

                .andExpect(jsonPath("$[1].name").value(bankDTOList.get(1).getName()))
                .andExpect(jsonPath("$[1].bankIdCode").value(bankDTOList.get(1).getBankIdCode()))

                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].name").value(bankDTOList.get(2).getName()))
                .andExpect(jsonPath("$[2].bankIdCode").value(bankDTOList.get(2).getBankIdCode())

                );

        verify(bankService, times(1)).findAll("", "", false, false, true);
    }

    @Test
    public void getAllBanksFilterByNotExistingNameTest() throws Exception {

        when(bankService.findAll("lalala", "", false, false, false)).thenReturn(emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/banks")
                        .param("filter_by_name", "lalala"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0))
                );

        verify(bankService, times(1)).findAll("lalala", "", false, false, false);
    }

    @Test
    public void getAllBanksFilterByNotExistingBicTest() throws Exception {

        when(bankService.findAll("", "bic", false, false, false)).thenReturn(emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/banks")
                        .param("filter_by_bankIdCode", "bic"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0))
                );

        verify(bankService, times(1)).findAll("", "bic", false, false, false);
    }

    @Test
    public void getBankByIdTest() throws Exception {

        when(bankService.findOne(1)).thenReturn(bankDTOList.get(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/banks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.name").value(bankDTOList.get(0).getName()))
                .andExpect(jsonPath("$.bankIdCode").value(bankDTOList.get(0).getBankIdCode()));

        verify(bankService, times(1)).findOne(1);
    }

    @Test
    public void getBankByNotExistingIdTest() throws Exception {
        when(bankService.findOne(anyInt())).thenThrow(new BankNotFoundException());

        mockMvc.perform(get("/banks/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Bank not found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(bankService, times(1)).findOne(999);
    }

    @Test
    public void createBankTest() throws Exception {

        BankDTO bankDTO = new BankDTO();
        bankDTO.setName("name");
        bankDTO.setBankIdCode("123454321");


        doNothing().when(bankService).save(any(BankDTO.class));
        bankDTOList.add(bankDTO);


        mockMvc.perform(post("/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankDTO)))
                .andExpect(status().isOk());

        verify(bankService, times(1)).save(any(BankDTO.class));
    }

    @Test
    public void createBank_ValidationError_BankNotCreatedExceptionTest() throws Exception {

        BankDTO bankDTO = new BankDTO();
        bankDTO.setName("");
        bankDTO.setBankIdCode("123456");


        mockMvc.perform(post("/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Name should be between 1 and 300 characters"))) // Ожидаем сообщение об ошибке
                .andExpect(jsonPath("$.message").value(containsString("Bank id code must have 9 numbers")));// Ожидаем сообщение об ошибке
    }

    @Test
    public void updateBankTest() throws Exception {

        BankDTO bankDTO = new BankDTO();
        bankDTO.setName("name");
        bankDTO.setBankIdCode("123454321");

        doNothing().when(bankService).update(anyInt(), any(BankDTO.class));
        bankDTOList.add(bankDTO);

        mockMvc.perform(put("/banks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankDTO)))
                .andExpect(status().isOk());

        verify(bankService, times(1)).update(anyInt(), any(BankDTO.class));
    }

    @Test
    public void updateBank_ValidationError_BankNotUpdatedExceptionTest() throws Exception {

        BankDTO bankDTO = new BankDTO();
        bankDTO.setName("");
        bankDTO.setBankIdCode("123456");

        doNothing().when(bankService).update(anyInt(), any(BankDTO.class));

        mockMvc.perform(put("/banks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Name should be between 1 and 300 characters"))) // Ожидаем сообщение об ошибке
                .andExpect(jsonPath("$.message").value(containsString("Bank id code must have 9 numbers")));// Ожидаем сообщение об ошибке
    }

    @Test
    public void deleteBankTest() throws Exception {

        doNothing().when(bankService).delete(anyInt());
        mockMvc.perform(delete("/banks/1"))
                .andExpect(status().isOk()
                );
        verify(bankService, times(1)).delete(anyInt());

    }

    @Test
    public void deleteBank_BankNotFoundExceptionTest() throws Exception {
        int bankId = 999;
        doThrow(new BankNotFoundException()).when(bankService).delete(bankId);

        mockMvc.perform(delete("/banks/999", bankId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(bankService, times(1)).delete(bankId);
    }


}

