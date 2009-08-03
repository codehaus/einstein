package org.cauldron.einstein.ri.test;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class MyOtherBean {

    private String name;

    public MyOtherBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
