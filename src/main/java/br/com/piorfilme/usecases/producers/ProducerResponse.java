package br.com.piorfilme.usecases.producers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(value = {"year", "title"})
@EqualsAndHashCode
public class ProducerResponse implements Comparable<ProducerResponse> {
    private String producer;
    private Long previousWin;
    private Long followingWin;
    private Long interval;

    private String title;
    private Long year;

    @Override
    public int compareTo(ProducerResponse arg0) {
        return (int) (arg0.getInterval() - this.interval);
    }
}
