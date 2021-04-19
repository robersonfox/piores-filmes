package br.com.piorfilme.adapters.databases.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.piorfilme.adapters.databases.models.Movie;

@Repository
public interface IMovieRepository extends JpaRepository<Movie, Long> {

}
