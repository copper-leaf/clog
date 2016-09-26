package com.caseyjbrooks.clog.parseltongue;

import com.caseyjbrooks.clog.ClogFormatter;
import com.caseyjbrooks.clog.Spell;
import com.caseyjbrooks.clog.TheStandardBookOfSpells;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Parseltongue implements ClogFormatter {
    private List<ParseltonguePair<String, Method>> spells;
    boolean privateFieldsAccessible;

    public Parseltongue() {
        spells = new ArrayList<>();
        privateFieldsAccessible = false;
        findSpells(TheStandardBookOfSpells.class);
    }

    public void findSpells(Class c) {
        for (final Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Spell.class)) {
                Spell methodAnnotation = method.getAnnotation(Spell.class);
                String spellName = methodAnnotation.name();
                if(spellName.length() == 0) {
                    spellName = method.getName();
                }

                spells.add(new ParseltonguePair<>(spellName, method));
            }
        }
    }

    public boolean arePrivateFieldsAccessible() {
        return privateFieldsAccessible;
    }

    public void setPrivateFieldsAccessible(boolean privateFieldsAccessible) {
        this.privateFieldsAccessible = privateFieldsAccessible;
    }

    @Override
    public String format(String messagae, Object... params) {
        if(params != null && params.length > 0) {
            return new Parser().parse(messagae, params);
        }
        else {
            return new Parser().parse(messagae, null);
        }
    }

    public Object transfigureObject(String key, Object reagent, Object... reagents) {
        for(ParseltonguePair <String, Method> method : spells) {

            //this method is annotated with the same name as our key
            if(method.first.equals(key)) {

                Class<?>[] parameterTypes = method.second.getParameterTypes();
                ArrayList<Object> params = new ArrayList<>();
                if(reagent != null) {
                    params.add(reagent);
                }
                else {
                    params.add(new NullObject());
                }

                if(reagents != null && reagents.length > 0) {
                    for(int i = 0; i < reagents.length; i++) {
                        if(reagents[i] != null) {
                            params.add(reagents[i]);
                        }
                        else {
                            params.add(new NullObject());
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
                        if(params.get(i) instanceof NullObject) {
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
                            if(params.get(i) instanceof NullObject) {
                                objects[i] = null;
                            }
                            else {
                                objects[i] = params.get(i);
                            }
                        }

                        try {
                            return method.second.invoke(null, objects);
                        }
                        catch(Exception e) {
//                            e.printStackTrace();
                        }
                        break;
                    }
                    else {
                        continue;
                    }

                }
            }
        }

        return null;
    }

// keeping the parser implementation as a private class, instantiated new each time, helps keep
// each run unpolluted

    private class Parser {

        private ArrayList<Object> params;
        private ArrayList<Object> results;

        TokenStream ts;
        String input;
        String output;

        private ArrayList<String> messages;

        public String parse(String input, Object[] params) {

            if(params != null && params.length > 0) {
                this.params = new ArrayList<>(Arrays.asList(params));
            }
            else {
                this.params = new ArrayList<>();
            }

            this.results = new ArrayList<>();
            this.messages = new ArrayList<>();

            this.input = input;
            this.output = "";

            this.ts = new TokenStream(input);

            while(ts.hasTokens()) {
                any();

                if(ts.hasTokens()) {
                    clog();
                }
            }

            return output;
        }

        //any ::= (anything but '#{')
        private void any() {
            Token t = ts.getAny();

            output += t.getStringValue();
        }

        //clog ::= CLOG_START reagent spellbook RCURLYBRACE
        private void clog() {
            Token a = ts.get();

            if(a != null && a.equals(Token.Type.CLOG_START)) {
                ParseltonguePair<Boolean, Object> object = reagent();

                if(object.first) {
                    ParseltonguePair<Boolean, Object> spellbookResult = spellbook(object.second);
                    results.add(spellbookResult.second);

                    if(spellbookResult.first) {
                        Token b = ts.get();

                        if (b != null && b.equals(Token.Type.RCURLYBRACE)) {
                            if (spellbookResult.second != null) {
                                output += spellbookResult.second.toString();
                            }
                        }
                        else {
                            ts.unget(b);

                            if (b != null) {
                                messages.add("Expecting '}' after clog, got '" + b.getStringValue() + "' (at column " + ts.getColumn() + ")");
                            } else {
                                messages.add("Expecting '}' after clog, got 'null' (at column " + ts.getColumn() + ")");
                            }

                            unclog();
                        }
                    }
                    else {
                        unclog();
                    }
                }
                else {
                    unclog();
                    results.add(null);
                }
            }
        }

        //spellbook ::= (PIPE castSpell)+
        private ParseltonguePair<Boolean, Object> spellbook(Object initialReagent) {
            Object pipelineObject = initialReagent;

            while (true) {
                Token a = ts.get();

                if (a != null && a.equals(Token.Type.PIPE)) {
                    ParseltonguePair<Boolean, Object> spellResult = castSpell(pipelineObject);

                    if(spellResult.first) {
                        ParseltonguePair<Boolean, Object> indexedSpell = indexer(spellResult);

                        if(indexedSpell.first) {
                            pipelineObject = indexedSpell.second;
                        }
                        else {
                            pipelineObject = spellResult.second;
                        }
                    }
                    else {
                        return new ParseltonguePair<>(false, null);
                    }
                }
                else {
                    ts.unget(a);
                    break;
                }
            }

            return new ParseltonguePair<>(true, pipelineObject);
        }

        //castSpell ::= spellName (LPAREN reagentList RPAREN)
        private ParseltonguePair<Boolean, Object> castSpell(Object reagent) {
            Token a = ts.get();

            if (a != null && a.equals(Token.Type.WORD)) {
                Token b = ts.get();

                if (b != null && b.equals(Token.Type.LPAREN)) {
                    ParseltonguePair<Boolean, Object[]> reagents = reagentList();

                    if(reagents.first) {
                        Token c = ts.get();

                        if (c != null && c.equals(Token.Type.RPAREN)) {
                            return new ParseltonguePair<>(true, transfigureObject(a.getStringValue(), reagent, reagents.second));
                        }
                        else {
                            ts.unget(c);
                            ts.unget(b);
                            ts.unget(a);

                            if(c != null) {
                                messages.add("Expecting ')' after param list, got '" + c.getStringValue() + "' (at column " + ts.getColumn() + ")");
                            }
                            else {
                                messages.add("Expecting ')' after param list, got 'null' (at column " + ts.getColumn() + ")");
                            }

                            return new ParseltonguePair<>(false, null);
                        }
                    }
                    else {
                        return new ParseltonguePair<>(false, null);
                    }
                }
                else{
                    ts.unget(b);
                    return new ParseltonguePair<>(true, transfigureObject(a.getStringValue(), reagent, null));
                }
            }
            else{
                ts.unget(a);
                return new ParseltonguePair<>(false, null);
            }
        }

        //reagentList ::= (reagent (COMMA reagent)+)
        private ParseltonguePair<Boolean, Object[]> reagentList() {
            ArrayList<Object> reagents = new ArrayList<>();

            ParseltonguePair<Boolean, Object> reagent = reagent();

            if(reagent.first) {
                reagents.add(reagent.second);

                while (true) {
                    Token a = ts.get();

                    if (a != null && a.equals(Token.Type.COMMA)) {
                        ParseltonguePair<Boolean, Object> otherReagent = reagent();
                        if(otherReagent.first) {
                            reagents.add(otherReagent.second);
                        }
                        else {
                            return new ParseltonguePair<>(false, null);
                        }
                    }
                    else {
                        ts.unget(a);
                        break;
                    }
                }

                Object[] reagentsList = new Object[reagents.size()];
                reagents.toArray(reagentsList);
                return new ParseltonguePair<>(true, reagentsList);
            }
            else {
                return new ParseltonguePair<>(true, null);
            }
        }

        //reagent ::= param indexer | result indexer | booleanLit | integerLit | doubleLit | stringLit
        private ParseltonguePair<Boolean, Object> reagent() {
            ParseltonguePair<Boolean, Object> param = param();
            if(param.first) {
                return indexer(param);
            }

            ParseltonguePair<Boolean, Object> result = result();
            if(result.first) {
                return indexer(result);
            }

            ParseltonguePair<Boolean, Boolean> booleanLit = booleanLit();
            if(booleanLit.first) {
                return new ParseltonguePair<Boolean, Object>(true, booleanLit.second);
            }

            ParseltonguePair<Boolean, Double> doubleLit = doubleLit();
            if(doubleLit.first) {
                return new ParseltonguePair<Boolean, Object>(true, doubleLit.second);
            }

            ParseltonguePair<Boolean, Integer> integerLit = integerLit();
            if(integerLit.first) {
                return new ParseltonguePair<Boolean, Object>(true, integerLit.second);
            }

            ParseltonguePair<Boolean, String> stringLit = stringLit();
            if(stringLit.first) {
                return new ParseltonguePair<Boolean, Object>(true, stringLit.second);
            }

            ParseltonguePair<Boolean, NullObject> nullLit = nullLit();
            if(nullLit.first) {
                return new ParseltonguePair<Boolean, Object>(true, nullLit.second);
            }

            return new ParseltonguePair<>(false, null);
        }

        //param ::= DOLLARSIGN NUMBER
        private ParseltonguePair<Boolean, Object> param() {
            Token a = ts.get();

            if(a != null && a.equals(Token.Type.DOLLARSIGN)) {
                Token b = ts.get();

                if(b != null && b.equals(Token.Type.NUMBER)) {
                    int index = b.getIntValue();

                    if(index > 0 && (index - 1) < params.size()) {
                        return new ParseltonguePair<>(true, params.get(index - 1));
                    }
                    else {
                        return new ParseltonguePair<>(true, null);
                    }
                }
                else {
                    ts.unget(b);
                    ts.unget(a);
                    if(b != null) {
                        messages.add("Expecting a number after '$', got '" + b.getStringValue() + "' (at column " + ts.getColumn() + ")");
                    }
                    else {
                        messages.add("Expecting a number after '$', got 'null' (at column " + ts.getColumn() + ")");
                    }

                    return new ParseltonguePair<>(false, null);
                }
            }
            else {
                ts.unget(a);
                return new ParseltonguePair<>(false, null);
            }
        }

        //result ::= ATSIGN NUMBER
        private ParseltonguePair<Boolean, Object> result() {
            Token a = ts.get();

            if(a != null && a.equals(Token.Type.AT)) {
                Token b = ts.get();

                if(b != null && b.equals(Token.Type.NUMBER)) {
                    int index = b.getIntValue();

                    if(index > 0 && (index - 1) < results.size()) {
                        return new ParseltonguePair<>(true, results.get(index - 1));
                    }
                    else {
                        return new ParseltonguePair<>(true, null);
                    }
                }
                else {
                    ts.unget(b);
                    ts.unget(a);
                    if(b != null) {
                        messages.add("Expecting a number after '@', got '" + b.getStringValue() + "' (at column " + ts.getColumn() + ")");
                    }
                    else {
                        messages.add("Expecting a number after '@', got 'null' (at column " + ts.getColumn() + ")");
                    }

                    return new ParseltonguePair<>(false, null);
                }
            }
            else {
                ts.unget(a);
                return new ParseltonguePair<>(false, null);
            }
        }

        //booleanLit ::= WORD=true | WORD=false
        private ParseltonguePair<Boolean, Boolean> booleanLit() {
            Token a = ts.get();

            if(a != null && a.equals(Token.Type.WORD)) {
                if(a.getStringValue().equalsIgnoreCase("true")) {
                    return new ParseltonguePair<>(true, true);
                }
                else if(a.getStringValue().equalsIgnoreCase("false")) {
                    return new ParseltonguePair<>(true, false);
                }
            }

            ts.unget(a);
            return new ParseltonguePair<>(false, false);
        }

        //doubleLit ::= NUMBER DOT NUMBER
        private ParseltonguePair<Boolean, Double> doubleLit() {
            Token a = ts.get();

            if(a != null && a.equals(Token.Type.NUMBER)) {
                Token b = ts.get();

                if(b != null && b.equals(Token.Type.DOT)) {
                    Token c = ts.get();
                    if(c != null && c.equals(Token.Type.NUMBER)) {
                        return new ParseltonguePair<>(true, Double.parseDouble(a.getIntValue() + "." + c.getIntValue()));
                    }
                    else {
                        ts.unget(c);
                        ts.unget(b);
                        ts.unget(a);
                    }
                }
                else {
                    ts.unget(b);
                    ts.unget(a);
                }
            }
            else {
                ts.unget(a);
            }

            return new ParseltonguePair<>(false, 0.0);
        }

        //integerLit ::= NUMBER
        private ParseltonguePair<Boolean, Integer> integerLit() {
            Token a = ts.get();

            if(a != null && a.equals(Token.Type.NUMBER)) {
                return new ParseltonguePair<>(true, a.getIntValue());
            }

            ts.unget(a);
            return new ParseltonguePair<>(false, 0);
        }

        //stringLit ::= QUOTE anything QUOTE
        private ParseltonguePair<Boolean, String> stringLit() {
            Token a = ts.get();

            if(a != null && a.equals(Token.Type.QUOTE)) {
                Token b = ts.getString();

                if(b != null) {
                    Token c = ts.get();
                    if(c != null && c.equals(Token.Type.QUOTE)) {
                        return new ParseltonguePair<>(true, b.getStringValue());
                    }
                    else {
                        ts.unget(c);
                        ts.unget(b);
                        ts.unget(a);
                        messages.add("String literal is never closed (at column " + ts.getColumn() + ")");
                        unclogString();
                    }
                }
                else {
                    ts.unget(b);
                    ts.unget(a);
                    messages.add("String literal is never closed (at column " + ts.getColumn() + ")");
                    unclogString();
                }
            }
            else {
                ts.unget(a);
            }

            return new ParseltonguePair<>(false, "");
        }

        //stringLit ::= WORD
        private ParseltonguePair<Boolean, NullObject> nullLit() {
            Token a = ts.get();

            if(a != null && a.equals(Token.Type.WORD)) {
                if(a.getStringValue().equalsIgnoreCase("null")) {
                    return new ParseltonguePair<>(true, new NullObject());
                }
            }

            ts.unget(a);
            return new ParseltonguePair<>(false, null);
        }

        //indexer :== ( LBRACKET NUMBER RBRACKET | LBRACKET WORD RBRACKET | LBRACKET stringLit RBRACKET)
        private ParseltonguePair<Boolean, Object> indexer(ParseltonguePair<Boolean, Object> object) {
            Token a = ts.get();

            if(a != null && a.equals(Token.Type.LBRACKET)) {
                Token b = ts.get();

                if(b != null && b.equals(Token.Type.NUMBER)) {
                    Token c = ts.get();
                    if(c != null && c.equals(Token.Type.RBRACKET)) {
                        return new ParseltonguePair<>(true, arrayIndexer(object.second, b.getIntValue()));
                    }
                    else {
                        ts.unget(c);
                        ts.unget(b);
                        ts.unget(a);
                        return new ParseltonguePair<>(false, null);
                    }
                }
                else if(b != null && b.equals(Token.Type.WORD)) {
                    Token c = ts.get();
                    if(c != null && c.equals(Token.Type.RBRACKET)) {
                        return new ParseltonguePair<>(true, propertyIndexer(object.second, b.getStringValue()));
                    }
                    else {
                        ts.unget(c);
                        ts.unget(b);
                        ts.unget(a);
                        return new ParseltonguePair<>(false, null);
                    }
                }
                else if(b != null && b.equals(Token.Type.QUOTE)) {
                    ts.unget(b);

                    ParseltonguePair<Boolean, String> stringKey = stringLit();

                    if(stringKey.first) {
                        Token c = ts.get();
                        if(c != null && c.equals(Token.Type.RBRACKET)) {
                            return new ParseltonguePair<>(true, mapIndexer(object.second, stringKey.second));
                        }
                        else {
                            ts.unget(c);
                            ts.unget(b);
                            ts.unget(a);
                            return new ParseltonguePair<>(false, null);
                        }
                    }
                    else {
                        return new ParseltonguePair<>(false, null);
                    }
                }
                else {
                    ts.unget(b);
                    ts.unget(a);
                    return new ParseltonguePair<>(false, null);
                }
            }
            else {
                ts.unget(a);
                return object;
            }
        }

        private void unclog() {
            ts.unclog();
        }

        private void unclogString() {

        }

        private Object arrayIndexer(Object object, int index) {
            if(object instanceof Object[]) {
                Object[] array = (Object[]) object;

                if(index >= 0 && index < array.length) {
                    return array[index];
                }
                else {
                    return null;
                }
            }
            else if(object instanceof List) {
                List list = (List) object;

                if(index >= 0 && index < list.size()) {
                    return list.get(index);
                }
                else {
                    return null;
                }
            }
            else {
                return null;
            }
        }

        private Object propertyIndexer(Object object, String property) {
            try {
                Class<?> c = object.getClass();
                Field field = c.getField(property);
                return field.get(object);
            }
            catch(Exception e) {
//                e.printStackTrace();

                if(privateFieldsAccessible) {
                    try {
                        Class<?> c = object.getClass();
                        Field field = c.getDeclaredField(property);
                        field.setAccessible(privateFieldsAccessible);
                        return field.get(object);
                    }
                    catch (Exception ee) {
//                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        private Object mapIndexer(Object object, String key) {
            if(object instanceof Map) {
                Map map = (Map) object;

                if(map.containsKey(key)) {
                    return map.get(key);
                }
                else {
                    return null;
                }
            }
            else {
                try {
                    Method method = object.getClass().getMethod("get", String.class);
                    return method.invoke(object, key);
                }
                catch(Exception e) {
//                    e.printStackTrace();
                }

                return null;
            }
        }
    }

    private class NullObject {

    }
}
