package org.cauldron.einstein.ri.examples;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class SleepHack {

    public Object sleepHack(Object o) throws InterruptedException {
        Thread.sleep(100000000);
        return null;
    }
}
