package br.com.piorfilme.adapters.databases.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.piorfilme.adapters.databases.models.Producer;

@Repository
public interface IProducerRepository extends JpaRepository<Producer, Long> {
    Optional<Producer> findOneByName(String name);

    @Query(nativeQuery = true, value = "SELECT p.name, m.year, m.title FROM MOVIE m inner join movie_producers mp on mp.movie_id = m.id inner join producer p on p.id = mp.producers_id where m.winner = true order by p.name, m.year")
    String[] findAllWinners();
}
