package org.scannotation.test;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@org.contract4j5.contract.Contract
public class ClassWithFieldAnnotation {
    private
    @SimpleAnnotation
    int field;
}
