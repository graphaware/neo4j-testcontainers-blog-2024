package com.graphaware;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.containers.Neo4jContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MoviesIT extends AbstractIT {


    @ParameterizedTest
    @MethodSource("neo4jVersions")
    void moviesLifecycle(String version) {
        final Neo4jContainer neo4jContainer = CONTAINERS.get(version);
        AppConfig.setDatabaseUrl(neo4jContainer.getBoltUrl());
        MovieAPI movieService = new MovieService();
        movieService.deleteAll();
        movieService.create(new Movie("xdelox"));
        movieService.create(new Movie("The Matrix"));
        assertThat(movieService.getAll())
            .hasSize(2)
            .allMatch(movie -> List.of("xdelox", "The Matrix").contains(movie.title()));

    }
}
