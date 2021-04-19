package br.com.piorfilme.config;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import br.com.piorfilme.adapters.databases.models.Movie;
import br.com.piorfilme.adapters.databases.models.Producer;
import br.com.piorfilme.adapters.databases.repositorys.IMovieRepository;
import br.com.piorfilme.adapters.databases.repositorys.IProducerRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class DataLoader {
    private Environment env;
    private IMovieRepository userRepository;

    private IProducerRepository producerRepository;

    @Autowired
    public DataLoader(IMovieRepository userRepository, IProducerRepository producerRepository, Environment env) {
        this.userRepository = userRepository;
        this.producerRepository = producerRepository;
        this.env = env;

        handleData();
    }

    private void handleData() {
        try {
            List<Movie> movies = getMoviesFromCSV();

            log.info("Inserindo " + movies.size() + " registros.");
            userRepository.saveAll(movies);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private List<Movie> getMoviesFromCSV() throws IOException {
        List<Movie> moviesList = new ArrayList<>();

        String path = env.getProperty("app.CVS_FILE");

        Reader reader = Files.newBufferedReader(Paths.get(path));

        CSVParser parser = new CSVParserBuilder().withSeparator(';').withIgnoreQuotations(true).build();
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).withCSVParser(parser).build();

        Movie movie = null;
        List<String[]> movies = csvReader.readAll();

        for (String[] m : movies) {
            try {
                Long year = m[0] == null ? Long.valueOf(0) : Long.valueOf(m[0]);
                String title = m[1] == null ? "" : m[1];
                String studios = m[2] == null ? "" : m[2];
                String producers = m[3] == null ? "" : m[3];
                boolean winner = (m[4] == null || !"yes".equalsIgnoreCase(m[4])) ? Boolean.FALSE : Boolean.TRUE;

                List<Producer> producer = getProducer(producers);

                movie = Movie.builder().title(title).studios(studios).producers(producer).winner(winner).year(year)
                        .build();

                moviesList.add(movie);

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        csvReader.close();

        return moviesList;
    }

    private List<Producer> getProducer(String strNames) {
        Producer producer = null;

        List<Producer> producers = new ArrayList<>();

        String[] n1 = strNames.split(",");

        for (String n : n1) {
            String[] n2 = n.split(" and ");

            for (String name : n2) {
                name = name.trim();

                if (name == null || name.isEmpty()) {
                    continue;
                }
                
                Optional<Producer> opProducer = producerRepository.findOneByName(name);


                if (opProducer.isEmpty()) {
                    producer = Producer.builder().name(name).build();

                    producers.add(producerRepository.save(producer));
                } else {
                    producers.add(opProducer.get());
                }
            }
        }

        return producers;
    }
}
