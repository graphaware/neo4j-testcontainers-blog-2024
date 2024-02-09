package com.graphaware.config.adminpassword;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.net.HttpURLConnection;
import java.time.Duration;
import java.util.List;

import static com.graphaware.Versions.NEO4J_VERSION;
import static com.graphaware.strategies.WaitingStrategies.WAIT_FOR_BOLT;
import static com.graphaware.strategies.WaitingStrategies.WAIT_FOR_HTTP;
import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@Tag("generic")
class GenericContainerChangeAdminPasswordIT {

    private static final String ANOTHER_PASSWORD = "QuickSilver";


    @Container
    private final GenericContainer genericContainer = new GenericContainer<>(DockerImageName.parse("neo4j:" + NEO4J_VERSION))
        .withEnv("NEO4J_AUTH", "neo4j/" + ANOTHER_PASSWORD)
        .withExposedPorts(7687, 7474)
        .waitingFor(new WaitAllStrategy()
            .withStrategy(WAIT_FOR_BOLT)
            .withStrategy(WAIT_FOR_HTTP)
            .withStartupTimeout(Duration.ofMinutes(2)
            )
        );

    @Test
    void change_admin_password() {
        genericContainer.start();

        AuthToken authToken = AuthTokens.basic("neo4j", ANOTHER_PASSWORD);
        String boltUrl = "bolt://" + genericContainer.getHost() + ":" + genericContainer.getMappedPort(7687);
        System.out.println(boltUrl);
        try (
            Driver driver = GraphDatabase.driver(boltUrl, authToken);
            Session session = driver.session();
        ) {
            final List<org.neo4j.driver.Record> result = session.run("RETURN 1 as test").list();
            assertThat(result).hasSize(1);
        }
    }


}
