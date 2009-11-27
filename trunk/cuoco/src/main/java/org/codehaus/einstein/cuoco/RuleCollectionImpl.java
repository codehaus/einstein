package org.codehaus.einstein.cuoco;

import java.io.PrintStream;
import java.util.*;
import java.beans.XMLEncoder;

/**
 * Created by IntelliJ IDEA. User: neilellis Date: Nov 24, 2009 Time: 12:28:54 PM To change this template use File |
 * Settings | File Templates.
 */
class RuleCollectionImpl implements RuleCollection {
    private Collection<Rule> rules;
    private boolean failFast;
    private Object domainObject;

    public RuleCollectionImpl(Collection<Rule> rules, boolean failFast, Object domainObject) {
        this.rules = rules;
        this.failFast = failFast;
        this.domainObject = domainObject;
    }

    public RuleResultCollection execute() {
        final List<RuleResult> results = new ArrayList<RuleResult>();
        final Map<String, RuleResult> resultMap = new HashMap<String, RuleResult>();
        final Map<String, RuleResult> passedMap = new HashMap<String, RuleResult>();
        final Map<String, RuleResult> failedMap = new HashMap<String, RuleResult>();
        RuleResult lastFailure = null;

        for (Rule rule : rules) {
            final RuleResult result = rule.execute();
            results.add(result);
            resultMap.put(rule.getRuleId().getId(), result);
            if (result.passed()) {
                passedMap.put(rule.getRuleId().getId(), result);
            } else {
                failedMap.put(rule.getRuleId().getId(), result);
                lastFailure = result;
            }
            if (!result.passed() && failFast) {
                break;
            }
        }

        final RuleResult finalLastFailure = lastFailure;
        return new RuleResultCollection() {

            public List<RuleResult> getAll() {
                return results;
            }

            public RuleResult getResult(String ruleId) {
                return resultMap.get(ruleId);
            }

            public boolean hasAllPassed() {
                return failedMap.isEmpty();
            }

            public Map<String, RuleResult> getFailed() {
                return failedMap;
            }

            public Map<String, RuleResult> getPassed() {
                return passedMap;
            }

            public boolean isFailFast() {
                return failFast;
            }

            public RuleResult getLastFailure() {
                return finalLastFailure;
            }

            public void diagnose(List<? extends RuleIdentifier> ruleIds, PrintStream stream) {
                for (RuleIdentifier ruleId : ruleIds) {
                    stream.print("Rule "+ruleId.getId()+" ("+ruleId.getErrorCode()+") ");
                   if(resultMap.containsKey(ruleId.getId())) {
                       RuleResult result= resultMap.get(ruleId.getId());
                       stream.print(result.passed() ? "PASSED" : "FAILED");
                   } else {
                       stream.print("NOT EXECUTED");
                   }
                    stream.println();
                }

                new XMLEncoder(stream).writeObject(domainObject);
            }


        };
    }


}
