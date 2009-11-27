package org.codehaus.einstein.cuoco;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


public abstract class SimpleRuleBuilder<T> implements RuleFactory, RuleBuilder {

    private HashMap<String, Rule> ruleMap = new HashMap<String, Rule>();
    private List<Rule> rules = new ArrayList<Rule>();

    private final ObjectAnalyser objectAnalyser;
    private T object;
    private boolean failFast;

    public SimpleRuleBuilder(T object, boolean failFast) {
        this.object = object;
        this.failFast = failFast;
        try {
            objectAnalyser = new ObjectAnalyser(object);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void internal(T domain);

    public final Rule createRule(RuleIdentifier ruleIdentifier) {
        final Rule rule = new Rule(ruleIdentifier);
        ruleMap.put(ruleIdentifier.getId(), rule);
        rules.add(rule);
        return rule;
    }

    public final Rule createRule(RuleIdentifier ruleIdentifier, Rule rule) {
        final Rule newRule = new Rule(rule, ruleIdentifier);
        ruleMap.put(ruleIdentifier.getId(), newRule);
        rules.add(newRule);
        return newRule;
    }


    public final Rule createRule(RuleIdentifier ruleIdentifier, boolean state) {
        final Rule newRule = new Rule(ruleIdentifier, state);
        ruleMap.put(ruleIdentifier.getId(), newRule);
        rules.add(newRule);
        return newRule;
    }


    public final void dump() {
        for (ObjectWalker.ObjectFieldTuple methodTuple : objectAnalyser.methodTuples) {
            System.out.println(methodTuple.path + ":" + methodTuple.value);
        }
    }


    public final RuleCollection build() {
        internal(object);
        return new RuleCollectionImpl(rules, failFast, object);
    }

    public final <P> P getProperty(String path) {
        return (P) objectAnalyser.getObject(path);
    }

    public final <P> P getFirstNonNullProperty(String... paths) {
        for (String path : paths) {
            P property = (P) objectAnalyser.getObject(path);
            if (property != null) {
                return property;
            }
        }
        return null;
    }


    public final boolean is(boolean b) {
        return b;
    }

    public final boolean not(boolean b) {
        return !b;
    }

    public final Rule propertyEquals(final String path, final Object o) {
        if (o == null) {
            throw new IllegalArgumentException("Use propertyIsNull() for null check on " + path);
        }
        return new Rule() {
            @Override
            protected boolean evaluateInternal() {
                return o.equals(objectAnalyser.getObject(path));
            }
        };
    }

    public final boolean propertyIn(final String path, final Object... t) {

        return Arrays.asList(t).contains(objectAnalyser.getObject(path));
    }

    public final boolean propertyIn(final String path, final Collection<?> collection) {
        return collection.contains(objectAnalyser.getObject(path));
    }


    public final boolean eq(final Object o1, final Object o2) {
        if (o2 == null) {
            throw new IllegalArgumentException("Use isNull() for null checks");
        }
        return o1 != null && o1.equals(o2);
    }

    public final boolean neq(final Object o1, final Object o2) {
        if (o2 == null) {
            throw new IllegalArgumentException("Use isNotNull() for null checks");
        }
        return o1 == null || !o1.equals(o2);
    }

    public final boolean isAlphanumeric(final String s) {
        return s != null && StringUtils.isAlphanumeric(s);
    }


    public final boolean not(final Rule rule) {
        return !rule.evaluate();
    }


    public final boolean isAlpha(final String s) {
        return s != null && StringUtils.isAlpha(s);
    }


    public final boolean isNumeric(final String s) {

        return s != null && StringUtils.isNumeric(s);
    }

    public final boolean isNonNumeric(final String s) {
        return s == null || !StringUtils.isNumeric(s);
    }

    public final boolean propertyIsNull(final String path) {
        return objectAnalyser.isNull(path);
    }

    public final boolean propertyIsNotNull(final String path) {
        return objectAnalyser.isNotNull(path);
    }


    public final boolean anyPropertyIsNotBlank(final String... paths) {
        for (String path : paths) {
            if (objectAnalyser.isNotBlank(path)) {
                return true;
            }
        }
        return false;
    }


    public final boolean anyPropertyExists(final String... paths) {
        for (String path : paths) {
            if (objectAnalyser.exists(path)) {
                return true;
            }
        }
        return false;
    }


    public final boolean propertyIsNotBlank(final String path) {
        return objectAnalyser.isNotBlank(path);
    }

    public final boolean propertyIsBlank(final String path) {
        return objectAnalyser.isEmptyString(path);
    }


    public final boolean blank(final String string) {
        return string == null || StringUtils.isEmpty(string.trim());

    }

    public final boolean nonBlank(final String string) {
        return string != null && !StringUtils.isEmpty(string.trim());
    }


    public final boolean isNull(final Object o) {
        return o == null;
    }

    public final boolean isNotNull(final Object o) {
        return o != null;
    }

    public final <P> boolean in(final P object, final Collection<P> collection) {
        return collection.contains(object);
    }

    public final <P> boolean in(final P object, final P... objects) {
        return Arrays.asList(objects).contains(object);
    }

    public final <P extends Enum> boolean inEnum(final String s, final P... values) {
        for (P p : values) {
            if (p.name().equals(s)) {
                return true;
            }
        }
        return false;


    }


    public String trim(String inStr) {
        if (inStr == null) {
            return null;
        } else {
            return inStr.trim();
        }
    }


    public final boolean isNumericallyGreaterThan(final String s, final long value) {
        return s.matches("[0-9]+") && Long.valueOf(s) > value;
    }

    public final boolean isNumericallyLessThanEqual(final String s, final long value) {
        return s.matches("[0-9]+") && Long.valueOf(s) <= value;
    }

    public final boolean isNumericallyGreaterThanEqual(final String s, final long value) {
        return s.matches("[0-9]+") && Long.valueOf(s) >= value;
    }

    public final boolean isNumericallyLessThan(final String s, final long value) {
        return s.matches("[0-9]+") && Long.valueOf(s) < value;
    }


}
