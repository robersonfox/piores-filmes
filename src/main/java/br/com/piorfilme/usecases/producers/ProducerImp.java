package br.com.piorfilme.usecases.producers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.piorfilme.usecases.producers.port.IProducerPort;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProducerImp implements IProducer<ProducerImp.Res> {

    private final IProducerPort port;

    private Set<ProducerResponse> producers = null;

    @Override
    public ProducerImp.Res obterProdutorMaiorIntervaloEntreDoisPremios() {
        List<ProducerResponse> producerResponseMin = getProducerResponseMin();
        List<ProducerResponse> producerResponseMax = getProducerResponseMax();

        return ProducerImp.Res.builder().max(producerResponseMax).min(producerResponseMin).build();
    }

    private Set<ProducerResponse> getProducers() {
        if (producers == null) {
            List<ProducerResponse> p = port.getWinners();

            if (p == null || p.isEmpty()) {
                producers = new HashSet<>();
            } else {
                producers = port.getWinners().stream().collect(Collectors.toSet());
            }
        }

        return producers;
    }

    private List<ProducerResponse> getProducerResponseMax() {
        ProducerResponse response = new ProducerResponse();

        List<ProducerResponse> max = new ArrayList<>();
        handleProducer(max);

        if (!max.isEmpty()) {
            response = max.stream().max(Comparator.comparing(ProducerResponse::getInterval)).get();
        }

        return Arrays.asList(response);
    }

    private List<ProducerResponse> getProducerResponseMin() {
        ProducerResponse response = new ProducerResponse();

        List<ProducerResponse> min = new ArrayList<>();
        handleProducer(min);

        if (!min.isEmpty()) {
            response = min.stream().min(Comparator.comparing(ProducerResponse::getInterval)).get();
        }

        return Arrays.asList(response);
    }

    private void handleProducer(List<ProducerResponse> m) {
        for (ProducerResponse producerResponse : getProducers()) {
            Long previousWin = producerResponse.getYear();
            String strProducer = producerResponse.getProducer();

            for (ProducerResponse producerResponse2 : getProducers()) {
                String strProducer2 = producerResponse2.getProducer();
                Long followingWin = producerResponse2.getYear();

                if ((strProducer.equalsIgnoreCase(strProducer2)) && (!producerResponse.equals(producerResponse2))) {

                    Long diff = (followingWin - previousWin);

                    if (diff <= -1)
                        continue;

                    m.add(ProducerResponse.builder().followingWin(followingWin).previousWin(previousWin)
                            .producer(strProducer).interval(diff).build());
                }
            }
        }
    }

    @Data
    @Builder
    public static class Res {
        List<ProducerResponse> max;
        List<ProducerResponse> min;
    }
}
