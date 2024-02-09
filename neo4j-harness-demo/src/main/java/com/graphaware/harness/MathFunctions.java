package com.graphaware.harness;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

public class MathFunctions {

    @UserFunction
    @Description("Sum of two numbers")
    public Long sum(@Name("a") Long a, @Name("b") Long b){
        return a+b;
    }

    @UserFunction
    @Description("Difference between two numbers")
    public Long difference(@Name("a") Long a, @Name("b") Long b){
        return a-b;
    }

    @UserFunction
    @Description("Product")
    public Long product(@Name("a") Long a, @Name("b") Long b){
        return a*b;
    }

    @UserFunction
    @Description("Division")
    public Long division(@Name("a") Long a, @Name("b") Long b){
        return a/b;
    }

    @UserFunction
    @Description("Mod")
    public Long mod(@Name("a") Long a, @Name("b") Long b){
        return a%b;
    }


}
