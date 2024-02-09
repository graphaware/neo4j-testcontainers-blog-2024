package com.graphaware.helloworld;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static com.graphaware.Versions.NEO4J_VERSION;
import static com.graphaware.strategies.WaitingStrategies.WAIT_FOR_BOLT;
import static com.graphaware.strategies.WaitingStrategies.WAIT_FOR_HTTP;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@Tag("neo4j-module")
class GenericHelloWorldIT {

    @Container
    private final GenericContainer genericContainer = new GenericContainer<>(DockerImageName.parse("neo4j:" + NEO4J_VERSION))
        .withEnv("NEO4J_AUTH", "neo4j/password")
        .withExposedPorts(7687, 7474)
        .waitingFor(new WaitAllStrategy()
            .withStrategy(WAIT_FOR_BOLT)
            .withStrategy(WAIT_FOR_HTTP)
            .withStartupTimeout(Duration.ofMinutes(2)
            )
        );
    ;

    @Test
    void should_return_one() {
        genericContainer.start();
        AuthToken authToken = AuthTokens.basic("neo4j", "password");
        try (
            Driver driver = GraphDatabase.driver(getBoltUrl(), authToken);
            Session session = driver.session();
        ) {
            var result = session.run("RETURN 1 as my_response").single().get("my_response").asInt();

            assertThat(result).isEqualTo(1);
        }
    }

    @NotNull
    private String getBoltUrl() {
        return "bolt://" + genericContainer.getHost() + ":" + genericContainer.getMappedPort(7687);
    }
}

