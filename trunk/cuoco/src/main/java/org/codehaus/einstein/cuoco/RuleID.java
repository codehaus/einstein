package org.codehaus.einstein.cuoco;

public class RuleID implements RuleIdentifier {
    private final String id;
    private final String errorCode;
    public static final RuleIdentifier NOT_SET = new RuleID("N/A", "N/A");

    public RuleID(String id, String errorCode) {
        this.id = id;
        this.errorCode = errorCode;
    }

    public String getId() {
        return id;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
