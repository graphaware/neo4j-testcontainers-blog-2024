package com.graphaware.helloworld;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.*;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.graphaware.Versions.NEO4J_VERSION;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@Tag("neo4j-module")
class HelloWorldIT {

    @Container
    private final Neo4jContainer neo4jContainer = new Neo4jContainer("neo4j:" + NEO4J_VERSION);

    @Test
    void should_return_one() {
        neo4jContainer.start();
        AuthToken authToken = AuthTokens.basic("neo4j", "password");
        try (
            Driver driver = GraphDatabase.driver(neo4jContainer.getBoltUrl(), authToken);
            Session session = driver.session();
        ) {
            var result = session.run("RETURN 1 as my_response").single().get("my_response").asInt();

            assertThat(result).isEqualTo(1);
        }
    }
}

