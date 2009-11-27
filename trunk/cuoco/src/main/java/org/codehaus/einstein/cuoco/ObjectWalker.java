package org.codehaus.einstein.cuoco;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.math.BigInteger;
import java.math.BigDecimal;

public class ObjectWalker {
    private final Object o;
    private List doNotTraverse = Arrays.asList(String.class, StringBuffer.class, Long.class, Integer.class, Boolean.class, BigDecimal.class, BigInteger.class, Float.class, Double.class, Character.class);

    public ObjectWalker(Object o) {
        this.o = o;
    }


    /**
     * Walk all the fields and place them in a {@link java.util.Set}, recursive method.
     *
     * @param values the (mutable) set to collect the results into.
     */
    public void walkMethods(Set<ObjectFieldTuple> values) throws InvocationTargetException {
        walkMethodsInternal("", o, values);
    }

    /**
     * Walk all the fields and place them in a {@link java.util.Set}, recursive method.
     *
     * @param objectMethods the (mutable) set to collect the results into.
     */
    public void walkMethodsInternal(String path, Object object, Set<ObjectFieldTuple> objectMethods) throws InvocationTargetException {
        if (!validForNavigation(object)) {
            return;
        }
        Method[] methods;
        try {
            Class clazz = object.getClass();
            methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
                    try {
                        final Object value = method.invoke(object);
                        if (Class.forName("java.util.Collection").isInstance(value)) {
                            Collection c = (Collection) value;
                            for (Object oc : c) {
                                if (oc != null && !doNotTraverse.contains(oc.getClass())) {
                                    walkMethodsInternal(methodToPath(path, method), oc, objectMethods);
                                }
                            }
                            continue;
                        } else {
                            if (method.getReturnType().isMemberClass() && !doNotTraverse.contains(method.getReturnType())) {
                                walkMethodsInternal(methodToPath(path, method), value, objectMethods);
                            }
                        }
                        objectMethods.add(new ObjectFieldTuple(methodToPath(path, method), value, method));
                    } catch (ClassNotFoundException e) {
                        throw new Error(e);
                    }
                }
            }
        } catch (SecurityException e) {
            throw new WalkException("Exception walking fields on object " + object, e);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        }
    }

    private static String methodToPath(String path, Method method) {
        final String methodNameAsProperty = Character.toLowerCase(method.getName().charAt(3)) + method.getName().substring(4);
        if (path.length() == 0) {
            return methodNameAsProperty;
        } else {
            return path + "." + methodNameAsProperty;
        }
    }

    public boolean validForNavigation(Object o) {
        //TODO: optionaly filter objects that may not be walked
        return o != null;
    }

    /**
     * Simple tuple class to hold {@link Object}s and {@link java.lang.reflect.Field}s.
     */
    public final class ObjectFieldTuple {
        public String path;
        public Object value;
        public Method method;

        private ObjectFieldTuple(String path, Object value, Method method) {
            this.path = path;
            this.value = value;
            this.method = method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ObjectFieldTuple that = (ObjectFieldTuple) o;

            if (!method.equals(that.method)) return false;
            if (!path.equals(that.path)) return false;
            if (value != null ? !value.equals(that.value) : that.value != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = path.hashCode();
            result = 31 * result + (value != null ? value.hashCode() : 0);
            result = 31 * result + method.hashCode();
            return result;
        }
    }


}
