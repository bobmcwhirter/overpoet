package com.overpoet.core.rule;

import org.junit.Test;

public class RuleTest {

    @Test
    public void testSimple() throws Exception {

        Rule rule = new Rule("yo");

        rule.when( ()->
                null
        ).then( ()->{

        });

    }
}
