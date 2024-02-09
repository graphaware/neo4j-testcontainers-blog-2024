package com.graphaware;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

import java.util.Collections;
import java.util.List;

public class MovieService implements MovieAPI {

    private final Driver driver;

    public MovieService() {
        final String databaseUrl = AppConfig.getDatabaseUrl();
        this.driver = GraphDatabase.driver(databaseUrl, AuthTokens.basic(AppConfig.getDatabaseUsername(), AppConfig.getDatabasePassword()));
    }

    @Override
    public void create(Movie movie) {
        Session session = driver.session();
        session.run("CREATE (m:Movie {title:'%s'})".formatted(movie.title()));
    }


    @Override
    public List<Movie> getAll() {
        Session session = driver.session();
        final List<Movie> movies = session.run("MATCH (n:Movie) RETURN n", Collections.emptyMap()).stream().map(r -> r.get("n").asMap())
            .map(m -> new Movie(m.get("title").toString())).toList();
        return movies;
    }

    @Override
    public void deleteAll() {
        try (Session session = driver.session();){
            session.run("MATCH (n) DETACH DELETE n");
        }
    }

}
