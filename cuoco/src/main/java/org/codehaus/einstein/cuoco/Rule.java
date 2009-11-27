package org.codehaus.einstein.cuoco;


public class Rule {

    private Rule delegate;

    private RuleIdentifier ruleIdentifier;

    public Rule(Rule rule, RuleIdentifier ruleIdentifier) {
        delegate = rule;
        this.ruleIdentifier = ruleIdentifier;
    }

    public Rule(final boolean state) {
        this();
        delegate = new Rule() {
            @Override
            protected boolean evaluateInternal() {
                return state;
            }
        };
    }

    protected Rule() {
        ruleIdentifier = RuleID.NOT_SET;

    }

    public Rule(RuleIdentifier ruleIdentifier) {
        this.ruleIdentifier = ruleIdentifier;
    }

    public Rule(RuleIdentifier ruleIdentifier, boolean state) {
        this.ruleIdentifier = ruleIdentifier;
        this.delegate = new Rule(state);
    }


    public Rule(Rule rule, final boolean state) {
        this.delegate = new Rule(rule) {
            @Override
            protected boolean evaluateInternal() {
                return state;
            }
        };
        ruleIdentifier = rule.ruleIdentifier;

    }

    public Rule(Rule delegate) {
        this.delegate = delegate;
        ruleIdentifier = delegate.ruleIdentifier;

    }

    private Rule(Rule rule, final Rule newRuleState) {
        this.delegate = new Rule(delegate) {
            @Override
            protected boolean evaluateInternal() {
                return newRuleState.evaluate();
            }
        };
        ruleIdentifier = rule.ruleIdentifier;
    }

    public final RuleResult execute() {
        final boolean result = evaluate();
        return new RuleResult() {
            public String getRuleId() {
                return ruleIdentifier.getId();
            }

            public String getErrorCode() {
                return ruleIdentifier.getErrorCode();
            }

            public boolean passed() {
                return result;
            }

            public boolean failed() {
                return !result;
            }
        };
    }

    protected boolean evaluate(boolean otherCondition) {
        return evaluate() && otherCondition;
    }

    public final boolean evaluate() {
        return delegate == null ? evaluateInternal() : delegate.evaluate(evaluateInternal());
    }


    protected boolean evaluateInternal() {
        return true;
    }


    public <T> Rule eq(final T dis, final T dat) {
        return new Rule(this) {
            @Override
            protected boolean evaluateInternal() {
//                System.err.println("Equals : " + (dis != null && dis.equals(dat)));
                return dis != null && dis.equals(dat);
            }
        };


    }

    public Rule and(final Rule rule) {
        return new Rule(this) {
            @Override
            protected boolean evaluateInternal() {
                return rule.evaluate();
            }
        };


    }


    public Rule or(final Rule rule) {
        return new Rule(this) {
            @Override
            protected boolean evaluateInternal() {
                return rule.evaluate();
            }

            @Override
            protected boolean evaluate(boolean otherCondition) {
                return (delegate == null || delegate.evaluate() || otherCondition);
            }
        };


    }

    public Rule not(final Rule rule) {
        return new Rule(this) {
            @Override
            protected boolean evaluateInternal() {
                return !rule.evaluate();
            }
        };


    }


    public Rule and(final boolean value) {
        return new Rule(this) {
            @Override
            protected boolean evaluateInternal() {
                return value;
            }
        };
    }

    public Rule is(final boolean value) {
        return and(value);
    }

    public Rule is(final Rule rule) {
        return and(rule);
    }


    public Rule or() {
        return new Rule(this) {
            @Override
            protected boolean evaluate(boolean otherCondition) {
                return (delegate == null || delegate.evaluate() || otherCondition);
            }
        };


    }


    public RuleIdentifier getRuleId() {
        return ruleIdentifier;
    }

}
