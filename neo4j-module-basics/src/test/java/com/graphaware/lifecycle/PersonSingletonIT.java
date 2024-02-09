package com.graphaware.lifecycle;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("lifecycle")
class PersonSingletonIT extends AbstractIT {

    @Test
    void people() {
        try (Driver driver = GraphDatabase.driver(neo4jContainer.getBoltUrl(), AuthTokens.basic("neo4j", "password"));
             Session session = driver.session()
        ) {
            List<Record> matchedRecords = session.run("MATCH (n:Person) RETURN n", Collections.emptyMap()).list();
            assertThat(matchedRecords).hasSize(7);
        }
    }

    @Test
    void people_born_after_1960() {
        try (Driver driver = GraphDatabase.driver(neo4jContainer.getBoltUrl(), AuthTokens.basic("neo4j", "password"));
             Session session = driver.session()
        ) {
            List<Record> matchedRecords = session.run("MATCH (n:Person) WHERE n.born > 1960 RETURN n", Collections.emptyMap()).list();
            assertThat(matchedRecords).hasSize(5);
        }
    }

    @Test
    void producers_count() {
        try (Driver driver = GraphDatabase.driver(neo4jContainer.getBoltUrl(), AuthTokens.basic("neo4j", "password"));
             Session session = driver.session()
        ) {
            List<Record> matchedRecords = session.run("MATCH (n:Movie)<-[:PRODUCED]-(p:Person) RETURN DISTINCT p", Collections.emptyMap()).list();
            assertThat(matchedRecords).hasSize(1);
        }
    }
}
