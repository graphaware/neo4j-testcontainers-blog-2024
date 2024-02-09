package com.graphaware.config.plugins;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

import java.util.stream.Stream;

public class CustomProcedures {

    @Procedure(value = "example.getRelationshipTypes")
    @Description("Get the different relationships going in and out of a node.")
    public Stream<RelationshipTypes> getRelationshipTypes(@Name("node") Node node) {
        Long outgoing =
            node.getRelationships(Direction.OUTGOING).stream()
                .map(n -> n.getType().name())
                .distinct().count();

        Long incoming =
            node.getRelationships(Direction.INCOMING).stream()
                .map(n -> n.getType().name())
                .distinct().count();

        return Stream.of(new RelationshipTypes(incoming, outgoing));
    }

}
