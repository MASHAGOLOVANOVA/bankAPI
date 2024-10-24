package gd.testtask.golovanova.bankAPI.services;

import gd.testtask.golovanova.bankAPI.dto.LegalFormDTO;
import gd.testtask.golovanova.bankAPI.models.LegalForm;
import gd.testtask.golovanova.bankAPI.repositories.LegalFormRepository;
import gd.testtask.golovanova.bankAPI.util.LegalFormNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LegalFormService {

    private final LegalFormRepository legalFormRepository;
    private final ModelMapper modelMapper;

    public LegalFormService(LegalFormRepository legalFormRepository) {
        this.legalFormRepository = legalFormRepository;
        this.modelMapper = new ModelMapper();
    }

    public List<LegalForm> findAll() {
        return legalFormRepository.findAll();
    }

    public LegalForm findOne(int id) {
        Optional<LegalForm> legalForm = legalFormRepository.findById(id);
        return legalForm.orElseThrow(LegalFormNotFoundException::new);
    }

    private LegalForm convertToLegalForm(LegalFormDTO legalFormDTO) {
        return modelMapper.map(legalFormDTO, LegalForm.class);
    }

    public LegalFormDTO convertToLegalFormDTO(LegalForm legalForm) {
        return modelMapper.map(legalForm, LegalFormDTO.class);
    }

}
