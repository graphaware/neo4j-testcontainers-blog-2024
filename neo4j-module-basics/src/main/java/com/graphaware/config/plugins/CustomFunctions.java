package com.graphaware.config.plugins;

import net.datafaker.Faker;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.UserFunction;

public class CustomFunctions {

    @UserFunction
    @Description("random quote from Chuck Norris")
    public String chuckNorris(){
        Faker faker = new Faker();
        return faker.chuckNorris().fact();
    }

}
