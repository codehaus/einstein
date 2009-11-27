package org.codehaus.einstein.cuoco;


public interface RuleFactory {
    boolean propertyIsNull(String path);

    boolean propertyIsNotNull(String path);

    boolean propertyIsNotBlank(String path);

    boolean propertyIsBlank(String path);

}
