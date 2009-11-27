package org.codehaus.einstein.cuoco;

import java.util.Arrays;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* User: Neil.Ellis
* Date: 23-Nov-2009
* Time: 17:18:27
* To change this template use File | Settings | File Templates.
*/
public class Weebl {
    private String dont = "fall down";
    private String wobble = "wobble";
    private List<String> words = Arrays.asList("badgers", "badgers");
    private List<Bob> bobs =  Arrays.asList(new Bob(), new Bob());

    public String getWobble() {
        return wobble;
    }

    public List<String> getWords() {
        return words;
    }

    public List<Bob> getBobs() {
        return bobs;
    }

    public String getDont() {
        return dont;
    }
}
