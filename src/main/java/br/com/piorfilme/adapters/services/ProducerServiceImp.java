package br.com.piorfilme.adapters.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.piorfilme.adapters.databases.repositorys.IProducerRepository;
import br.com.piorfilme.usecases.producers.ProducerResponse;
import br.com.piorfilme.usecases.producers.port.IProducerPort;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ProducerServiceImp implements IProducerPort {
    
    @Autowired
    private IProducerRepository repository;

    @Override
    public List<ProducerResponse> getWinners() {
        String[] allWinners = null;

        try {
            allWinners = repository.findAllWinners();

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        List<ProducerResponse> response = new ArrayList<>();

        if (allWinners != null)
        for (String winner : allWinners) {
            String[] s = winner.split(",");
            
            ProducerResponse producerResponse = ProducerResponse.builder()
            .producer (s[0])
            .year(Long.valueOf(s[1]))
            .title(s[2])
            .build();

            response.add(producerResponse);
        }

        return response;
    }
}
