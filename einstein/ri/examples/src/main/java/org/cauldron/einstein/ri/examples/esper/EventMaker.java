package org.cauldron.einstein.ri.examples.esper;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class EventMaker {

    public Object createEvent(Object o) {
        int type = (int) (5 * Math.random());
        double amount = 100 * Math.random();
        switch (type) {
            case 0:
                return new CylindricalWidget(amount);
            case 1:
                return new RoundWidget(amount);
            case 2:
                return new SquareWidget(amount);
            case 3:
                return new TessaractWidget(amount);
            case 4:
                return new TriangularWidget(amount);
        }
        throw new IllegalArgumentException();

    }


}
