package org.codehaus.einstein.cuoco;

/**
 * Created by IntelliJ IDEA. User: neilellis Date: Nov 24, 2009 Time: 12:43:41 AM To change this template use File |
 * Settings | File Templates.
 */
public interface RuleResult {

    String getRuleId();

    String getErrorCode();

    boolean passed();
}
