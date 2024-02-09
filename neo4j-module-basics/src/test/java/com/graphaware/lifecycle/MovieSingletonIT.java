package com.graphaware.lifecycle;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("lifecycle")
class MovieSingletonIT extends AbstractIT {

    @Test
    void getMovies() {
        try (Driver driver = GraphDatabase.driver(neo4jContainer.getBoltUrl(), AuthTokens.basic("neo4j", "password"));
             Session session = driver.session()
        ) {
            List<org.neo4j.driver.Record> matchedRecords = session.run("MATCH (n:Movie) RETURN n", Collections.emptyMap()).list();
            assertThat(matchedRecords).hasSize(3);
        }
    }

    @Test
    void getMoviesReleasedIn1999() {
        try (Driver driver = GraphDatabase.driver(neo4jContainer.getBoltUrl(), AuthTokens.basic("neo4j", "password"));
             Session session = driver.session()
        ) {
            List<Record> matchedRecords = session.run("MATCH (n:Movie) WHERE n.released=1999 RETURN n", Collections.emptyMap()).list();
            assertThat(matchedRecords).hasSize(1);
        }
    }

    @Test
    void getMoviesWithKeanuAndReleasedIn1999() {
        try (Driver driver = GraphDatabase.driver(neo4jContainer.getBoltUrl(), AuthTokens.basic("neo4j", "password"));
             Session session = driver.session()
        ) {
            List<Record> matchedRecords = session.run("MATCH (n:Movie)<-[:ACTED_IN]-(p:Person) WHERE n.released > 1999 AND p.name = 'Keanu Reeves' RETURN n", Collections.emptyMap()).list();
            assertThat(matchedRecords).hasSize(2);
        }
    }
}
