package com.graphaware.lifecycle;

import org.junit.jupiter.api.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.testcontainers.containers.Neo4jContainer;

import java.util.Collections;
import java.util.List;

import static com.graphaware.Versions.NEO4J_VERSION;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("lifecycle")
class OneContainterPerClassLifeCycleCallbackIT {

    static Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>("neo4j:" + NEO4J_VERSION );

    @BeforeAll
    static void startContainer(){
        neo4jContainer.start();
    }

    @BeforeEach
    void setup() {
        dropAll();
        executeCypher("CREATE (:Movie {name: 'Dune', year: 2020}) CREATE (:Movie {name: 'Space Jam - New Legends', year: 2021})");
    }

    @AfterEach
    void tearDown(){
        //Stopping the testcontainer is automatic
    }

    @Test
    void testOne() {
        try (Driver driver = GraphDatabase.driver(neo4jContainer.getBoltUrl(), AuthTokens.basic("neo4j", "password"));
             Session session = driver.session()
        ) {
            List<Record> matchedRecords = session.run("MATCH (n:Movie) RETURN n", Collections.emptyMap()).list();
            assertThat(matchedRecords).hasSize(2);
        }
    }

    @Test
    void testTwo() {
        try (Driver driver = GraphDatabase.driver(neo4jContainer.getBoltUrl(), AuthTokens.basic("neo4j", "password"));
             Session session = driver.session()
        ) {
            List<Record> matchedRecords = session.run("MATCH (n:Movie) WHERE n.year = 2021 RETURN n", Collections.emptyMap()).list();
            assertThat(matchedRecords).hasSize(1);
        }
    }

    @Test
    void testThree() {
        try (Driver driver = GraphDatabase.driver(neo4jContainer.getBoltUrl(), AuthTokens.basic("neo4j", "password"));
             Session session = driver.session()
        ) {
            List<Record> matchedRecords = session.run("MATCH (n) WHERE n.year > 2023 RETURN n", Collections.emptyMap()).list();
            assertThat(matchedRecords).isEmpty();
        }
    }

    private void dropAll() {
        executeCypher("MATCH (n) DETACH delete n");
    }

    private void executeCypher(String statement) {
        try (Driver driver = GraphDatabase.driver(neo4jContainer.getBoltUrl(), AuthTokens.basic("neo4j", "password"));
             Session session = driver.session()
        ) {
            driver.session().run(statement, Collections.emptyMap());
        }
    }
}
