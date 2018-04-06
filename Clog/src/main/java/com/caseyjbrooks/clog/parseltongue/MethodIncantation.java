package com.caseyjbrooks.clog.parseltongue;

import java.lang.reflect.Method;
import java.util.ArrayList;

public final class MethodIncantation implements Incantation {

    private final String name;
    private final Method method;

    public MethodIncantation(String name, Method method) {
        this.name = name;
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public Object call(Object reagent, Object... reagents) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        ArrayList<Object> params = new ArrayList<>();
        if(reagent != null) {
            params.add(reagent);
        }
        else {
            params.add(new Parseltongue.NullObject());
        }

        if(reagents != null && reagents.length > 0) {
            for(int i = 0; i < reagents.length; i++) {
                if(reagents[i] != null) {
                    params.add(reagents[i]);
                }
                else {
                    params.add(new Parseltongue.NullObject());
                }
            }
        }

        //we are passing the same number of arguments as this method accepts. Check the types
        // for a type match
        if(parameterTypes.length == params.size()) {
            boolean methodMatch = true;

            for (int i = 0; i < params.size(); i++) {

                //if the object passed in is null, we cannot determine if it matches the param type, but
                // we can just pass in the object at that index as a null object
                if(params.get(i) instanceof Parseltongue.NullObject) {
                    continue;
                }

                //the parser gives us the concrete wrapper classes of primitives, which are not directly
                // assignable to their primitive counterparts, so we must manually check each primitive param type
                else if(parameterTypes[i].equals(byte.class) && params.get(i).getClass().equals(Byte.class)) {
                    continue;
                }
                else if(parameterTypes[i].equals(short.class) && params.get(i).getClass().equals(Short.class)) {
                    continue;
                }
                else if(parameterTypes[i].equals(int.class) && params.get(i).getClass().equals(Integer.class)) {
                    continue;
                }
                else if(parameterTypes[i].equals(long.class) && params.get(i).getClass().equals(Long.class)) {
                    continue;
                }
                else if(parameterTypes[i].equals(float.class) && params.get(i).getClass().equals(Float.class)) {
                    continue;
                }
                else if(parameterTypes[i].equals(double.class) && params.get(i).getClass().equals(Double.class)) {
                    continue;
                }
                else if(parameterTypes[i].equals(boolean.class) && params.get(i).getClass().equals(Boolean.class)) {
                    continue;
                }
                else if(parameterTypes[i].isAssignableFrom(params.get(i).getClass())) {
                    continue;
                }
                else {
                    methodMatch = false;
                    break;
                }
            }

            //all parameter types match, go ahead and cast the spell!
            if(methodMatch) {
                Object[] objects = new Object[params.size()];
                for(int i = 0; i < params.size(); i++) {
                    if(params.get(i) instanceof Parseltongue.NullObject) {
                        objects[i] = null;
                    }
                    else {
                        objects[i] = params.get(i);
                    }
                }

                try {
                    return method.invoke(null, objects);
                }
                catch(Exception e) {
//                    e.printStackTrace();
                }
            }
        }

        return null;
    }

}
