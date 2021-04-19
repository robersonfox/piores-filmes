package br.com.piorfilme.adapters.databases.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.piorfilme.adapters.databases.models.Producer;

@Repository
public interface IProducerRepository extends JpaRepository<Producer, Long> {
    Optional<Producer> findOneByName(String name);
}
