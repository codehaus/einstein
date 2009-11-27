package org.codehaus.einstein.cuoco;

import java.util.Arrays;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* User: Neil.Ellis
* Date: 23-Nov-2009
* Time: 17:18:41
* To change this template use File | Settings | File Templates.
*/
public class Bob {
    private String pie = "pie";

    private String empty;

    public String getEmpty() {
        return empty;
    }

    private List<String> words = Arrays.asList("mmmmm....", "pie...");

    public String getPie() {
        return pie;
    }

    public List<String> getWords() {
        return words;
    }
}
