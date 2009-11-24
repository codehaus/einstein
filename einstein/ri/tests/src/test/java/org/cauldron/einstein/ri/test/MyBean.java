package org.cauldron.einstein.ri.test;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class MyBean {

    private String information = "My Information";
    private MyOtherBean myOtherBean = new MyOtherBean("dave");

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public MyOtherBean getMyOtherBean() {
        return myOtherBean;
    }

    public void setMyOtherBean(MyOtherBean myOtherBean) {
        this.myOtherBean = myOtherBean;
    }
}
