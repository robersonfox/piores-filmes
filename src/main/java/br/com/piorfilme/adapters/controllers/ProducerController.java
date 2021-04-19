package br.com.piorfilme.adapters.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.piorfilme.usecases.producers.IProducer;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/producer")
@RequiredArgsConstructor
public class ProducerController {

    private final IProducer<?> iProducer;

    @ApiOperation(value = "Obter o produtor com maior intervalo entre dois prêmios consecutivos, e o que obteve dois prêmios mais rápido")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), })
    @GetMapping(produces = "application/json")
    public ResponseEntity<?> obterProdutorComMaiorIntervaloEntreDoisPremiosConsecutivos() {
        try {
            Object response = iProducer.obterProdutorMaiorIntervaloEntreDoisPremios();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
