package com.graphaware.config.adminpassword;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.*;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.HttpURLConnection;

import static com.graphaware.Versions.NEO4J_VERSION;
import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@Tag("neo4j-module")
class Neo4jContainerChangeAdminPasswordIT {

    private static final String ANOTHER_PASSWORD = "QuickSilver";

    @Container
    private final Neo4jContainer neo4jContainer = new Neo4jContainer("neo4j:" + NEO4J_VERSION)
        .withAdminPassword(ANOTHER_PASSWORD);


    @Test
    void change_admin_password() {
        neo4jContainer.start();
        AuthToken authToken = AuthTokens.basic("neo4j", ANOTHER_PASSWORD);
        try (
            Driver driver = GraphDatabase.driver(neo4jContainer.getBoltUrl(), authToken);
            Session session = driver.session();
        ) {
            var result = session.run("RETURN 1 as test").list();

            assertThat(result).hasSize(1);
        }
    }
}
