package gd.testtask.golovanova.bankAPI.services;

import gd.testtask.golovanova.bankAPI.dto.ClientDTO;
import gd.testtask.golovanova.bankAPI.dto.LegalFormDTO;
import gd.testtask.golovanova.bankAPI.models.Client;
import gd.testtask.golovanova.bankAPI.models.LegalForm;
import gd.testtask.golovanova.bankAPI.repositories.ClientRepository;
import gd.testtask.golovanova.bankAPI.repositories.LegalFormRepository;
import gd.testtask.golovanova.bankAPI.util.ClientNotFoundException;
import gd.testtask.golovanova.bankAPI.util.LegalFormNotFoundException;
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
public class ClientService {

    private final ClientRepository clientRepository;
    private final LegalFormService legalFormService;
    private final ModelMapper modelMapper;

    @Autowired
    public ClientService(ClientRepository clientRepository, LegalFormService legalFormService) {
        this.clientRepository = clientRepository;
        this.legalFormService = legalFormService;
        this.modelMapper = new ModelMapper();
    }

    public List<ClientDTO> findAll(String name, String shortName, String address, boolean sortById, boolean sortByName, boolean sortByShortName, boolean sortByAddress) {
        List<Client> clients = clientRepository.findAll();

        // Фильтрация
        if (name != null && !name.isEmpty()) {
            clients = clients.stream()
                    .filter(client -> client.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());
        }
        if (shortName != null && !shortName.isEmpty()) {
            clients = clients.stream()
                    .filter(client -> client.getShortName().equalsIgnoreCase(shortName))
                    .collect(Collectors.toList());
        }

        if (address != null && !address.isEmpty()) {
            clients = clients.stream()
                    .filter(client -> client.getAddress().equalsIgnoreCase(address))
                    .collect(Collectors.toList());
        }

        // Сортировка
        if (sortById) {
            clients.sort(Comparator.comparing(Client::getId));
        } else if (sortByName) {
            clients.sort(Comparator.comparing(Client::getName));
        } else if (sortByShortName) {
            clients.sort(Comparator.comparing(Client::getShortName));
        } else if (sortByAddress) {
            clients.sort(Comparator.comparing(Client::getAddress));
        }

        return clients.stream().map(this::convertToClientDTO).collect(Collectors.toList());
    }

    public ClientDTO findOne(int id){
        Optional<Client> client = clientRepository.findById(id);
        return convertToClientDTO(client.orElseThrow(ClientNotFoundException::new));
    }

    @Transactional
    public void save(ClientDTO clientDTO){
        LegalForm legalForm = legalFormService.findOne(clientDTO.getLegalFormId());
        Client client = convertToClient(clientDTO);
        client.setLegalForm(legalForm);
        clientRepository.save(client);
    }

    @Transactional
    public void delete(int id){
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException();
        }
        clientRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, ClientDTO clientDTO){
        Client client = clientRepository.findById(id)
                .orElseThrow(ClientNotFoundException::new);
        LegalForm legalForm = legalFormService.findOne(clientDTO.getLegalFormId());
        client.setLegalForm(legalForm);
        client.setName(clientDTO.getName());
        client.setShortName(clientDTO.getShortName());
        client.setAddress(clientDTO.getAddress());
        clientRepository.save(client);
    }

    public Client convertToClient(ClientDTO clientDTO) {
        return modelMapper.map(clientDTO, Client.class);
    }

    public ClientDTO convertToClientDTO(Client client) {
        return modelMapper.map(client, ClientDTO.class);
    }

}
