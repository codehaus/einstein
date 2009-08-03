package org.cauldron.einstein.ri.examples.esper;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class AbstractWidget {

    AbstractWidget() {
    }

    private double value;

    AbstractWidget(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
