package gd.testtask.golovanova.bankAPI.services;

import gd.testtask.golovanova.bankAPI.dto.BankDTO;
import gd.testtask.golovanova.bankAPI.models.Bank;
import gd.testtask.golovanova.bankAPI.repositories.BankRepository;
import gd.testtask.golovanova.bankAPI.util.BankNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BankService {

    private final BankRepository bankRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
        this.modelMapper = new ModelMapper();
    }

    public List<BankDTO> findAll(String name, String bankIdCode, boolean sortById, boolean sortByName, boolean sortByBankIdCode) {
        List<Bank> banks = bankRepository.findAll();

        // Фильтрация
        if (name != null && !name.isEmpty()) {
            banks = banks.stream()
                    .filter(bank -> bank.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());
        }

        if (bankIdCode != null && !bankIdCode.isEmpty()) {
            banks = banks.stream()
                    .filter(bank -> bank.getBankIdCode().equalsIgnoreCase(bankIdCode))
                    .collect(Collectors.toList());
        }

        // Сортировка
        if (sortById) {
            banks.sort(Comparator.comparing(Bank::getId));
        } else if (sortByName) {
            banks.sort(Comparator.comparing(Bank::getName));
        } else if (sortByBankIdCode) {
            banks.sort(Comparator.comparing(Bank::getBankIdCode));
        }

        return banks.stream().map(this::convertToBankDTO).collect(Collectors.toList());
    }


    public BankDTO findOne(int id) {
        Optional<Bank> bank = bankRepository.findById(id);
        return convertToBankDTO(bank.orElseThrow(BankNotFoundException::new));
    }

    @Transactional
    public void save(BankDTO bankDTO) {
        bankRepository.save(convertToBank(bankDTO));
    }

    @Transactional
    public void delete(int id) {
        if (!bankRepository.existsById(id)) {
            throw new BankNotFoundException();
        }
        bankRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, BankDTO bankDTO) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(BankNotFoundException::new);
        bank.setName(bankDTO.getName());
        bank.setBankIdCode(bankDTO.getBankIdCode());
        bankRepository.save(bank);
    }

    public Bank convertToBank(BankDTO bankDTO) {
        return modelMapper.map(bankDTO, Bank.class);
    }

    public BankDTO convertToBankDTO(Bank bank) {
        return modelMapper.map(bank, BankDTO.class);
    }

}
