package com.graphaware;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.Neo4jContainer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public abstract class AbstractIT {

    protected static final List<String> SUPPORTED_NEO4J_VERSIONS = List.of("4.4.28", "5.16.0");

    protected static Map<String, Neo4jContainer> CONTAINERS = new HashMap<>();

    @BeforeAll
    static void startContainers() {
        SUPPORTED_NEO4J_VERSIONS.stream().forEach(
            version ->
            {
                var imageName = String.format("neo4j:%s-enterprise", version);
                Neo4jContainer<?> container = new Neo4jContainer<>(
                    imageName)
                    .withoutAuthentication()
                    .withEnv("NEO4J_ACCEPT_LICENSE_AGREEMENT", "yes")
                    .withReuse(true);
                container.start();

                CONTAINERS.putIfAbsent(version, container);
            }
        );
    }

    protected static Stream<String> neo4jVersions() {
        return SUPPORTED_NEO4J_VERSIONS.stream();
    }


}
