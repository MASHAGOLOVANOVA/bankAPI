package gd.testtask.golovanova.bankAPI.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import gd.testtask.golovanova.bankAPI.dto.DepositDTO;
import gd.testtask.golovanova.bankAPI.repositories.BankRepository;
import gd.testtask.golovanova.bankAPI.services.DepositService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
public class DepositControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private DepositService depositService;

    private List<DepositDTO> depositDTOList;

    private final ObjectMapper objectMapper;

    @Autowired
    public DepositControllerTest(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void setUp() {
        depositDTOList = new ArrayList<>();
        DepositDTO depositDTO1 = new DepositDTO();
        depositDTO1.setClient_id(1);
        depositDTO1.setBank_id(1);
        depositDTO1.setPercentage(12);
        depositDTO1.setPeriod(11);
        depositDTOList.add(depositDTO1);
        DepositDTO depositDTO2 = new DepositDTO();
        depositDTO2.setClient_id(2);
        depositDTO2.setBank_id(2);
        depositDTO2.setPercentage(13);
        depositDTO2.setPeriod(12);
        depositDTOList.add(depositDTO2);
        DepositDTO depositDTO3 = new DepositDTO();
        depositDTO3.setClient_id(3);
        depositDTO3.setBank_id(3);
        depositDTO3.setPercentage(14);
        depositDTO3.setPeriod(13);
        depositDTOList.add(depositDTO3);
    }

    @Test
    public void getAllDepositsTest() throws Exception {
        when(depositService.findAll(null, null, false, false, false, false)).thenReturn(depositDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].client_id").value(depositDTOList.get(0).getClient_id()))
                .andExpect(jsonPath("$[0].bank_id").value(depositDTOList.get(0).getBank_id()))
                .andExpect(jsonPath("$[0].percentage").value(depositDTOList.get(0).getPercentage()))
                .andExpect(jsonPath("$[0].period").value(depositDTOList.get(0).getPeriod()))


                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].client_id").value(depositDTOList.get(1).getClient_id()))
                .andExpect(jsonPath("$[1].bank_id").value(depositDTOList.get(1).getBank_id()))
                .andExpect(jsonPath("$[1].percentage").value(depositDTOList.get(1).getPercentage()))
                .andExpect(jsonPath("$[1].period").value(depositDTOList.get(1).getPeriod()))


                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].client_id").value(depositDTOList.get(2).getClient_id()))
                .andExpect(jsonPath("$[2].bank_id").value(depositDTOList.get(2).getBank_id()))
                .andExpect(jsonPath("$[2].percentage").value(depositDTOList.get(2).getPercentage()))
                .andExpect(jsonPath("$[2].period").value(depositDTOList.get(2).getPeriod())
                );

        verify(depositService, times(1)).findAll(null, null, false, false, false, false);
    }

    @Test
    public void getAllDepositsEmptyTest() throws Exception {
        when(depositService.findAll(null, null, false, false, false, false)).thenReturn(emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0))
                );

        verify(depositService, times(1)).findAll(null, null, false, false, false, false);
    }

    @Test
    public void getAllDepositsFilterByPeriodTest() throws Exception {
        when(depositService.findAll(null, 11, false, false, false, false)).thenReturn(List.of(depositDTOList.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits")
                        .param("filter_by_period", "11"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].client_id").value(depositDTOList.get(0).getClient_id()))
                .andExpect(jsonPath("$[0].bank_id").value(depositDTOList.get(0).getBank_id()))
                .andExpect(jsonPath("$[0].percentage").value(depositDTOList.get(0).getPercentage()))
                .andExpect(jsonPath("$[0].period").value(depositDTOList.get(0).getPeriod())
                );

        verify(depositService, times(1)).findAll(null, 11, false, false, false, false);
    }

    @Test
    public void getAllDepositsFilterByPercentageTest() throws Exception {
        when(depositService.findAll(12, null, false, false, false, false)).thenReturn(List.of(depositDTOList.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits")
                        .param("filter_by_percentage", "12"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].client_id").value(depositDTOList.get(0).getClient_id()))
                .andExpect(jsonPath("$[0].bank_id").value(depositDTOList.get(0).getBank_id()))
                .andExpect(jsonPath("$[0].percentage").value(depositDTOList.get(0).getPercentage()))
                .andExpect(jsonPath("$[0].period").value(depositDTOList.get(0).getPeriod())
                );

        verify(depositService, times(1)).findAll(12, null, false, false, false, false);
    }


    @Test
    public void getAllDepositsSortByIdTest() throws Exception {
        when(depositService.findAll(null, null, true, false, false, false)).thenReturn(depositDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits")
                        .param("sort_by_id", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].client_id").value(depositDTOList.get(0).getClient_id()))
                .andExpect(jsonPath("$[0].bank_id").value(depositDTOList.get(0).getBank_id()))
                .andExpect(jsonPath("$[0].percentage").value(depositDTOList.get(0).getPercentage()))
                .andExpect(jsonPath("$[0].period").value(depositDTOList.get(0).getPeriod()))


                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].client_id").value(depositDTOList.get(1).getClient_id()))
                .andExpect(jsonPath("$[1].bank_id").value(depositDTOList.get(1).getBank_id()))
                .andExpect(jsonPath("$[1].percentage").value(depositDTOList.get(1).getPercentage()))
                .andExpect(jsonPath("$[1].period").value(depositDTOList.get(1).getPeriod()))


                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].client_id").value(depositDTOList.get(2).getClient_id()))
                .andExpect(jsonPath("$[2].bank_id").value(depositDTOList.get(2).getBank_id()))
                .andExpect(jsonPath("$[2].percentage").value(depositDTOList.get(2).getPercentage()))
                .andExpect(jsonPath("$[2].period").value(depositDTOList.get(2).getPeriod())
                );

        verify(depositService, times(1)).findAll(null, null, true, false, false, false);
    }

    @Test
    public void getAllDepositsSortByPeriodTest() throws Exception {
        when(depositService.findAll(null, null, false, false, true, false)).thenReturn(depositDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits")
                        .param("sort_by_period", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].client_id").value(depositDTOList.get(0).getClient_id()))
                .andExpect(jsonPath("$[0].bank_id").value(depositDTOList.get(0).getBank_id()))
                .andExpect(jsonPath("$[0].percentage").value(depositDTOList.get(0).getPercentage()))
                .andExpect(jsonPath("$[0].period").value(depositDTOList.get(0).getPeriod()))


                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].client_id").value(depositDTOList.get(1).getClient_id()))
                .andExpect(jsonPath("$[1].bank_id").value(depositDTOList.get(1).getBank_id()))
                .andExpect(jsonPath("$[1].percentage").value(depositDTOList.get(1).getPercentage()))
                .andExpect(jsonPath("$[1].period").value(depositDTOList.get(1).getPeriod()))


                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].client_id").value(depositDTOList.get(2).getClient_id()))
                .andExpect(jsonPath("$[2].bank_id").value(depositDTOList.get(2).getBank_id()))
                .andExpect(jsonPath("$[2].percentage").value(depositDTOList.get(2).getPercentage()))
                .andExpect(jsonPath("$[2].period").value(depositDTOList.get(2).getPeriod())
                );

        verify(depositService, times(1)).findAll(null, null, false, false, true, false);
    }

    @Test
    public void getAllDepositsSortByPercentageTest() throws Exception {

        when(depositService.findAll(null, null, false, true, false, false)).thenReturn(depositDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits")
                        .param("sort_by_percentage", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].client_id").value(depositDTOList.get(0).getClient_id()))
                .andExpect(jsonPath("$[0].bank_id").value(depositDTOList.get(0).getBank_id()))
                .andExpect(jsonPath("$[0].percentage").value(depositDTOList.get(0).getPercentage()))
                .andExpect(jsonPath("$[0].period").value(depositDTOList.get(0).getPeriod()))


                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].client_id").value(depositDTOList.get(1).getClient_id()))
                .andExpect(jsonPath("$[1].bank_id").value(depositDTOList.get(1).getBank_id()))
                .andExpect(jsonPath("$[1].percentage").value(depositDTOList.get(1).getPercentage()))
                .andExpect(jsonPath("$[1].period").value(depositDTOList.get(1).getPeriod()))


                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].client_id").value(depositDTOList.get(2).getClient_id()))
                .andExpect(jsonPath("$[2].bank_id").value(depositDTOList.get(2).getBank_id()))
                .andExpect(jsonPath("$[2].percentage").value(depositDTOList.get(2).getPercentage()))
                .andExpect(jsonPath("$[2].period").value(depositDTOList.get(2).getPeriod())
                );

        verify(depositService, times(1)).findAll(null, null, false, true, false, false);
    }


    @Test
    public void getAllDepositsSortByCreateDateTest() throws Exception {
        when(depositService.findAll(null, null, false, false, false, true)).thenReturn(depositDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits")
                        .param("sort_by_create_date", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].client_id").value(depositDTOList.get(0).getClient_id()))
                .andExpect(jsonPath("$[0].bank_id").value(depositDTOList.get(0).getBank_id()))
                .andExpect(jsonPath("$[0].percentage").value(depositDTOList.get(0).getPercentage()))
                .andExpect(jsonPath("$[0].period").value(depositDTOList.get(0).getPeriod()))


                .andExpect(jsonPath("$[1].id").doesNotExist())
                .andExpect(jsonPath("$[1].client_id").value(depositDTOList.get(1).getClient_id()))
                .andExpect(jsonPath("$[1].bank_id").value(depositDTOList.get(1).getBank_id()))
                .andExpect(jsonPath("$[1].percentage").value(depositDTOList.get(1).getPercentage()))
                .andExpect(jsonPath("$[1].period").value(depositDTOList.get(1).getPeriod()))


                .andExpect(jsonPath("$[2].id").doesNotExist())
                .andExpect(jsonPath("$[2].client_id").value(depositDTOList.get(2).getClient_id()))
                .andExpect(jsonPath("$[2].bank_id").value(depositDTOList.get(2).getBank_id()))
                .andExpect(jsonPath("$[2].percentage").value(depositDTOList.get(2).getPercentage()))
                .andExpect(jsonPath("$[2].period").value(depositDTOList.get(2).getPeriod())
                );

        verify(depositService, times(1)).findAll(null, null, false, false, false, true);
    }


    @Test
    public void getDepositByIdTest() throws Exception {

        when(depositService.findOne(1)).thenReturn(depositDTOList.get(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/deposits/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.client_id").value(depositDTOList.get(0).getClient_id()))
                .andExpect(jsonPath("$.bank_id").value(depositDTOList.get(0).getBank_id()))
                .andExpect(jsonPath("$.percentage").value(depositDTOList.get(0).getPercentage()))
                .andExpect(jsonPath("$.period").value(depositDTOList.get(0).getPeriod()));

        verify(depositService, times(1)).findOne(1);
    }

    @Test
    public void getDepositByNotExistingIdTest() throws Exception {
        when(depositService.findOne(anyInt())).thenThrow(new DepositNotFoundException());

        mockMvc.perform(get("/deposits/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Deposit not found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(depositService, times(1)).findOne(999);
    }


    @Test
    public void createDepositTest() throws Exception {

        DepositDTO depositDTO1 = new DepositDTO();
        depositDTO1.setClient_id(1);
        depositDTO1.setBank_id(1);
        depositDTO1.setPercentage(12);
        depositDTO1.setPeriod(11);

        doNothing().when(depositService).save(any(DepositDTO.class));
        depositDTOList.add(depositDTO1);


        mockMvc.perform(post("/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDTO1)))
                .andExpect(status().isOk());

        verify(depositService, times(1)).save(any(DepositDTO.class));
    }

    @Test
    public void createDeposit_ValidationError_BankNotFoundException() throws Exception {
        DepositDTO depositDTO1 = new DepositDTO();
        depositDTO1.setClient_id(1);
        depositDTO1.setBank_id(1);
        depositDTO1.setPercentage(12);
        depositDTO1.setPeriod(11);

        doThrow(new BankNotFoundException()).when(depositService).save(any(DepositDTO.class));
        mockMvc.perform(post("/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDTO1)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Bank not found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(depositService, times(1)).save(any(DepositDTO.class));
    }

    @Test
    public void createDeposit_ValidationError_ClientNotFoundException() throws Exception {
        DepositDTO depositDTO1 = new DepositDTO();
        depositDTO1.setClient_id(1);
        depositDTO1.setBank_id(1);
        depositDTO1.setPercentage(12);
        depositDTO1.setPeriod(11);

        doThrow(new ClientNotFoundException()).when(depositService).save(any(DepositDTO.class));

        mockMvc.perform(post("/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDTO1)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Client not found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(depositService, times(1)).save(any(DepositDTO.class));
    }

    @Test
    public void updateDepositTest() throws Exception {


        DepositDTO depositDTO1 = new DepositDTO();
        depositDTO1.setClient_id(2);
        depositDTO1.setBank_id(2);
        depositDTO1.setPercentage(13);
        depositDTO1.setPeriod(13);


        doNothing().when(depositService).update(anyInt(), any(DepositDTO.class));
        depositDTOList.add(depositDTO1);


        mockMvc.perform(MockMvcRequestBuilders.put("/deposits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDTO1)))
                .andExpect(status().isOk());

        verify(depositService, times(1)).update(anyInt(), any(DepositDTO.class));
    }

    @Test
    public void updateDeposit_ValidationError_BankNotFoundException() throws Exception {
        DepositDTO depositDTO1 = new DepositDTO();
        depositDTO1.setClient_id(2);
        depositDTO1.setBank_id(2);
        depositDTO1.setPercentage(13);
        depositDTO1.setPeriod(13);

        doThrow(new BankNotFoundException()).when(depositService).update(anyInt(), any(DepositDTO.class));
        mockMvc.perform(MockMvcRequestBuilders.put("/deposits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDTO1)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Bank not found"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(depositService, times(1)).update(anyInt(), any(DepositDTO.class));

    }

    @Test
    public void deleteDepositTest() throws Exception {

        doNothing().when(depositService).delete(anyInt());
        mockMvc.perform(delete("/deposits/1"))
                .andExpect(status().isOk()
                );
        verify(depositService, times(1)).delete(anyInt());

    }

    @Test
    public void deleteDepositByNotExistingIdTest() throws Exception {

        doThrow(new DepositNotFoundException()).when(depositService).delete(anyInt());
        mockMvc.perform(delete("/deposits/1", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(depositService, times(1)).delete(anyInt());

    }


}
