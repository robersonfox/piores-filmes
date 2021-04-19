package br.com.piorfilme.usecases.producers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
            producers = port.getWinners().stream().collect(Collectors.toSet());
        }

        return producers;
    }

    private List<ProducerResponse> getProducerResponseMax() {
        List<ProducerResponse> max = new ArrayList<>();

        for (ProducerResponse producerResponse : getProducers()) {
            Long previousWin = producerResponse.getYear();
            String strProducer = producerResponse.getProducer();

            for (ProducerResponse producerResponse2 : getProducers()) {
                String strProducer2 = producerResponse2.getProducer();
                Long followingWin = producerResponse2.getYear();

                if ((strProducer.equalsIgnoreCase(strProducer2)) && (!producerResponse.equals(producerResponse2))) {

                    Long diff = (followingWin - previousWin);

                    if (diff <= 1)
                        continue;

                    max.add(ProducerResponse.builder().followingWin(followingWin).previousWin(previousWin)
                            .producer(strProducer).interval(diff).build());
                }
            }
        }

        Collections.sort(max);

        return max.stream().distinct().collect(Collectors.toList());
    }

    private List<ProducerResponse> getProducerResponseMin() {
        List<ProducerResponse> min = new ArrayList<>();

        for (ProducerResponse producerResponse : getProducers()) {
            Long previousWin = producerResponse.getYear();
            String strProducer = producerResponse.getProducer();

            for (ProducerResponse producerResponse2 : getProducers()) {
                String strProducer2 = producerResponse2.getProducer();
                Long followingWin = producerResponse2.getYear();

                if ((strProducer.equalsIgnoreCase(strProducer2)) && (!producerResponse.equals(producerResponse2))) {

                    Long diff = (followingWin - previousWin);

                    if (diff <= -1 || diff > 1)
                        continue;

                    min.add(ProducerResponse.builder().followingWin(followingWin).previousWin(previousWin)
                            .producer(strProducer).interval(diff).build());
                }
            }
        }

        Collections.sort(min);

        return min.stream().distinct().collect(Collectors.toList());
    }

    @Data
    @Builder
    public static class Res {
        List<ProducerResponse> max;
        List<ProducerResponse> min;
    }
}
