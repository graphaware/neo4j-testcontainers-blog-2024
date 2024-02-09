package com.graphaware.lifecycle;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.testcontainers.containers.Neo4jContainer;

import java.util.Collections;
import java.util.List;

import static com.graphaware.Versions.NEO4J_VERSION;
import static org.assertj.core.api.Assertions.assertThat;

class ReuseIT {

    static Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>("neo4j:" + NEO4J_VERSION)
        .withReuse(true);

    @BeforeAll
    static void setupAll(){
        neo4jContainer.start();
    }

    @BeforeEach
    protected void setup() {
        dropAll();
        executeCypher(
            """
                CREATE (TheMatrix:Movie {title:'The Matrix', released:1999, tagline:'Welcome to the Real World'})
                    CREATE (Keanu:Person {name:'Keanu Reeves', born:1964})
                    CREATE (Carrie:Person {name:'Carrie-Anne Moss', born:1967})
                    CREATE (Laurence:Person {name:'Laurence Fishburne', born:1961})
                    CREATE (Hugo:Person {name:'Hugo Weaving', born:1960})
                    CREATE (LillyW:Person {name:'Lilly Wachowski', born:1967})
                    CREATE (LanaW:Person {name:'Lana Wachowski', born:1965})
                    CREATE (JoelS:Person {name:'Joel Silver', born:1952})
                    CREATE
                    (Keanu)-[:ACTED_IN {roles:['Neo']}]->(TheMatrix),
                    (Carrie)-[:ACTED_IN {roles:['Trinity']}]->(TheMatrix),
                    (Laurence)-[:ACTED_IN {roles:['Morpheus']}]->(TheMatrix),
                    (Hugo)-[:ACTED_IN {roles:['Agent Smith']}]->(TheMatrix),
                    (LillyW)-[:DIRECTED]->(TheMatrix),
                    (LanaW)-[:DIRECTED]->(TheMatrix),
                    (JoelS)-[:PRODUCED]->(TheMatrix)


                    CREATE (TheMatrixReloaded:Movie {title:'The Matrix Reloaded', released:2003, tagline:'Free your mind'})
                    CREATE (Keanu)-[:ACTED_IN {roles:['Neo']}]->(TheMatrixReloaded),
                    (Carrie)-[:ACTED_IN {roles:['Trinity']}]->(TheMatrixReloaded),
                    (Laurence)-[:ACTED_IN {roles:['Morpheus']}]->(TheMatrixReloaded),
                    (Hugo)-[:ACTED_IN {roles:['Agent Smith']}]->(TheMatrixReloaded),
                    (LillyW)-[:DIRECTED]->(TheMatrixReloaded),
                    (LanaW)-[:DIRECTED]->(TheMatrixReloaded),
                    (JoelS)-[:PRODUCED]->(TheMatrixReloaded)

                    CREATE (TheMatrixRevolutions:Movie {title:'The Matrix Revolutions', released:2003, tagline:'Everything that has a beginning has an end'})
                    CREATE (Keanu)-[:ACTED_IN {roles:['Neo']}]->(TheMatrixRevolutions),
                    (Carrie)-[:ACTED_IN {roles:['Trinity']}]->(TheMatrixRevolutions),
                    (Laurence)-[:ACTED_IN {roles:['Morpheus']}]->(TheMatrixRevolutions),
                    (Hugo)-[:ACTED_IN {roles:['Agent Smith']}]->(TheMatrixRevolutions),
                    (LillyW)-[:DIRECTED]->(TheMatrixRevolutions),
                    (LanaW)-[:DIRECTED]->(TheMatrixRevolutions),
                    (JoelS)-[:PRODUCED]->(TheMatrixRevolutions)

                    """

        );
    }

    @Test
    void people() {
        try (Driver driver = GraphDatabase.driver(neo4jContainer.getBoltUrl(), AuthTokens.basic("neo4j", "password"));
             Session session = driver.session()
        ) {
            List<org.neo4j.driver.Record> matchedRecords = session.run("MATCH (n:Person) RETURN n", Collections.emptyMap()).list();
            assertThat(matchedRecords).hasSize(7);
        }
    }

    @Test
    void people_born_after_1960() {
        try (Driver driver = GraphDatabase.driver(neo4jContainer.getBoltUrl(), AuthTokens.basic("neo4j", "password"));
             Session session = driver.session()
        ) {
            List<org.neo4j.driver.Record> matchedRecords = session.run("MATCH (n:Person) WHERE n.born > 1960 RETURN n", Collections.emptyMap()).list();
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
