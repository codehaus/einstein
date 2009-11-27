package org.codehaus.einstein.cuoco;

import java.util.List;
import java.util.Map;
import java.io.PrintStream;


public interface RuleResultCollection {

    List<RuleResult> getAll();

    RuleResult getResult(String ruleId);

    boolean hasAllPassed();

    Map<String,RuleResult> getFailed();

    Map<String, RuleResult> getPassed();

    boolean isFailFast();

    RuleResult getLastFailure();

     void diagnose(List<? extends RuleIdentifier> allIds, PrintStream stream);

}
