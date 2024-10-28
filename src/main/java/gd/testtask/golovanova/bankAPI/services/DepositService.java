package gd.testtask.golovanova.bankAPI.services;

import gd.testtask.golovanova.bankAPI.dto.DepositDTO;
import gd.testtask.golovanova.bankAPI.models.Bank;
import gd.testtask.golovanova.bankAPI.models.Client;
import gd.testtask.golovanova.bankAPI.models.Deposit;
import gd.testtask.golovanova.bankAPI.repositories.BankRepository;
import gd.testtask.golovanova.bankAPI.repositories.ClientRepository;
import gd.testtask.golovanova.bankAPI.repositories.DepositRepository;
import gd.testtask.golovanova.bankAPI.util.DepositNotFoundException;
import gd.testtask.golovanova.bankAPI.util.ClientNotFoundException;
import gd.testtask.golovanova.bankAPI.util.BankNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DepositService {

    public final DepositRepository depositRepository;
    public final ModelMapper modelMapper;
    private final BankRepository bankRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public DepositService(DepositRepository depositRepository, BankRepository bankRepository, ClientRepository clientRepository) {
        this.depositRepository = depositRepository;
        this.modelMapper = new ModelMapper();
        this.bankRepository = bankRepository;
        this.clientRepository = clientRepository;
    }

    public List<DepositDTO> findAll(Integer filterByPercentage, Integer filterByPeriod, boolean sortById, boolean sortByPercentage, boolean sortByPeriod, boolean sortByCreateDate) {
        List<Deposit> deposits = depositRepository.findAll();

        // Фильтрация
        if (filterByPercentage != null) {
            deposits = deposits.stream()
                    .filter(deposit -> deposit.getPercentage() == filterByPercentage)
                    .collect(Collectors.toList());
        }
        if (filterByPeriod != null) {
            deposits = deposits.stream()
                    .filter(deposit -> deposit.getPeriod() == filterByPeriod)
                    .collect(Collectors.toList());
        }

        // Сортировка
        if (sortById) {
            deposits.sort(Comparator.comparing(Deposit::getId));
        } else if (sortByPercentage) {
            deposits.sort(Comparator.comparing(Deposit::getPercentage));
        } else if (sortByPeriod) {
            deposits.sort(Comparator.comparing(Deposit::getPeriod));
        } else if (sortByCreateDate) {
            deposits.sort(Comparator.comparing(Deposit::getCreateDate));
        }

        List<DepositDTO> depositDTOs = new ArrayList<>();
        for (Deposit deposit : deposits) {
            DepositDTO depositDTO = modelMapper.map(deposit, DepositDTO.class);
            depositDTO.setBank_id(deposit.getBank().getId());
            depositDTO.setClient_id(deposit.getClient().getId());
            depositDTOs.add(depositDTO);

        }
        return depositDTOs;
    }

    public DepositDTO findOne(int id) {
        Optional<Deposit> deposit = depositRepository.findById(id);
        DepositDTO depositDTO = convertToDepositDTO(deposit.orElseThrow(DepositNotFoundException::new));
        depositDTO.setBank_id(deposit.get().getBank().getId());
        depositDTO.setClient_id(deposit.get().getClient().getId());
        return depositDTO;
    }

    @Transactional
    public void save(DepositDTO depositDTO) {
        Bank bank = bankRepository.findById(depositDTO.getBank_id())
                .orElseThrow(BankNotFoundException::new);
        Client client = clientRepository.findById(depositDTO.getClient_id())
                .orElseThrow(ClientNotFoundException::new);
        Deposit deposit = convertToDeposit(depositDTO);
        deposit.setBank(bank);
        deposit.setClient(client);
        deposit.setCreateDate(new Date());
        depositRepository.save(deposit);
    }

    @Transactional
    public void delete(int id) {
        if (!depositRepository.existsById(id)) {
            throw new DepositNotFoundException();
        }
        depositRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, DepositDTO depositDTO) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(DepositNotFoundException::new);
        Bank bank = bankRepository.findById(depositDTO.getBank_id())
                .orElseThrow(BankNotFoundException::new);
        Client client = clientRepository.findById(depositDTO.getClient_id())
                .orElseThrow(ClientNotFoundException::new);
        deposit.setBank(bank);
        deposit.setClient(client);
        deposit.setPercentage(depositDTO.getPercentage());
        deposit.setPeriod(depositDTO.getPeriod());
        depositRepository.save(deposit);
    }

    public Deposit convertToDeposit(DepositDTO depositDTO) {
        return modelMapper.map(depositDTO, Deposit.class);
    }

    public DepositDTO convertToDepositDTO(Deposit deposit) {
        return modelMapper.map(deposit, DepositDTO.class);
    }

}
