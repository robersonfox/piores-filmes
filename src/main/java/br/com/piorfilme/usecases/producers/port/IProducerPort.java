package br.com.piorfilme.usecases.producers.port;

import java.util.List;

import br.com.piorfilme.usecases.producers.ProducerResponse;

public interface IProducerPort {

    public List<ProducerResponse> getWinners();
}
