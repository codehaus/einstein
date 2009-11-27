package org.codehaus.einstein.cuoco;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class ObjectAnalyser {
    
    protected HashMap<String, Object> nonNull = new HashMap<String, Object>();
    protected HashMap<String, Object> nonEmptyStrings = new HashMap<String, Object>();
    protected HashMap<String, Object> emptyStrings = new HashMap<String, Object>();
    protected HashMap<String, Object> all = new HashMap<String, Object>();
    protected Set<ObjectWalker.ObjectFieldTuple> methodTuples = new HashSet<ObjectWalker.ObjectFieldTuple>();

    public ObjectAnalyser(Object object) throws InvocationTargetException {
        super();
        final ObjectWalker objectWalker = new ObjectWalker(object);
        objectWalker.walkMethods(methodTuples);
        for (ObjectWalker.ObjectFieldTuple fieldTuple : methodTuples) {
            all.put(fieldTuple.path, fieldTuple.value);
            if (fieldTuple.value != null) {
                nonNull.put(fieldTuple.path, fieldTuple.value);
                if (fieldTuple.method.getReturnType().equals(String.class)) {
                    if (fieldTuple.value != null && StringUtils.isEmpty(((String) (fieldTuple.value)).trim())) {
                        nonEmptyStrings.put(fieldTuple.path, fieldTuple.value);
                    } else {
                        emptyStrings.put(fieldTuple.path, fieldTuple.value);
                    }
                }
            }
        }
    }


    public boolean isNull(String path) {
        return !isNotNull(path);
    }

    public boolean isNotNull(String path) {
        return nonNull.containsKey(path);
    }

    public boolean isNotBlank(String path) {
        return nonEmptyStrings.containsKey(path);
    }

    public boolean isEmptyString(String path) {
        return emptyStrings.containsKey(path);
    }

    public Object getObject(String path) {
        return all.get(path);
    }

    public boolean exists(String path) {
        return all.containsKey(path);
    }
}
