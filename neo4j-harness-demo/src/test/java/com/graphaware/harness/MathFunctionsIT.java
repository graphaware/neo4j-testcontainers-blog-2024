package com.graphaware.harness;

import org.junit.jupiter.api.*;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MathFunctionsIT {

    private Neo4j neo4jEmbedded;

    @BeforeAll
    void initializeNeo4j() {
        this.neo4jEmbedded =
            Neo4jBuilders.newInProcessBuilder()
                .withFunction(MathFunctions.class)
                .withDisabledServer()
                .build();
    }

    @AfterAll
    void closeNeo4j() {
        this.neo4jEmbedded.close();
    }

    @Test
    void sum() {
        try (Driver driver = GraphDatabase.driver(neo4jEmbedded.boltURI());
             Session session = driver.session();
        ) {
            // When
            Long result = session.run("RETURN com.graphaware.harness.sum(5,2) AS result").single().get("result").asLong();
            // Then
            assertThat(result).isEqualTo(7);
        }
    }

    @Test
    void diff() {
        try (Driver driver = GraphDatabase.driver(neo4jEmbedded.boltURI());
             Session session = driver.session();
        ) {
            // When
            Long result = session.run("RETURN com.graphaware.harness.difference(5,2) AS result").single().get("result").asLong();
            // Then
            assertThat(result).isEqualTo(3);
        }
    }

    @Test
    void product() {
        try (Driver driver = GraphDatabase.driver(neo4jEmbedded.boltURI());
             Session session = driver.session();
        ) {
            // When
            Long result = session.run("RETURN com.graphaware.harness.product(5,2) AS result").single().get("result").asLong();
            // Then
            assertThat(result).isEqualTo(10);
        }
    }

    @Test
    void division() {
        try (Driver driver = GraphDatabase.driver(neo4jEmbedded.boltURI());
             Session session = driver.session();
        ) {
            // When
            Long result = session.run("RETURN com.graphaware.harness.division(5,2) AS result").single().get("result").asLong();
            // Then
            assertThat(result).isEqualTo(2);
        }
    }

    @Test
    void module() {
        try (Driver driver = GraphDatabase.driver(neo4jEmbedded.boltURI());
             Session session = driver.session();
        ) {
            // When
            Long result = session.run("RETURN com.graphaware.harness.mod(5,2) AS result").single().get("result").asLong();
            // Then
            assertThat(result).isEqualTo(1);
        }
    }

    @Test
    void getVersion(){
        try (Driver driver = GraphDatabase.driver(neo4jEmbedded.boltURI());
             Session session = driver.session();
        ) {
            // When
            final Record result = session.run("call dbms.components() yield name, versions, edition unwind versions as version return name, version, edition;").single();
            // Then
            assertThat(result.get("version").asString()).startsWith("5.");
            assertThat(result.get("edition").asString()).isEqualTo("community");

        }
    }
}
