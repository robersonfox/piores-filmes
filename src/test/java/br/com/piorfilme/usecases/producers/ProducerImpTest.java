package br.com.piorfilme.usecases.producers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.piorfilme.usecases.producers.port.IProducerPort;

@RunWith(MockitoJUnitRunner.class)
public class ProducerImpTest {
    @InjectMocks
    private ProducerImp inject;

    @Mock
    private IProducerPort port;

    @Before
    public void init() {
        inject = new ProducerImp(port);
    }

    @Test
    public void caminhoFeliz() {
        when(port.getWinners()).thenReturn(getMovieList());

        ProducerImp.Res response = inject.obterProdutorMaiorIntervaloEntreDoisPremios();

        assertNotNull(response);
    }

    @Test
    public void deveriaTerUmRegistroEmMin() {
        when(port.getWinners()).thenReturn(getMovieList());

        ProducerImp.Res response = inject.obterProdutorMaiorIntervaloEntreDoisPremios();

        assertEquals(1, response.min.size());
    }

    @Test
    public void deveriaRetornarArrayVazio() {
        when(port.getWinners()).thenReturn(null);

        ProducerImp.Res response = inject.obterProdutorMaiorIntervaloEntreDoisPremios();

        assertEquals(0, response.min.size());
    }

    private List<ProducerResponse> getMovieList() {
        return Arrays.asList(ProducerResponse.builder().year(2000L).producer("producer").title("title").build(),
                ProducerResponse.builder().year(1999L).producer("producer").title("title").build());
    }
}
