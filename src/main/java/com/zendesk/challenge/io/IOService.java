package com.zendesk.challenge.io;

import com.zendesk.challenge.domain.commons.Entity;
import com.zendesk.challenge.domain.commons.Scope;
import com.zendesk.challenge.exception.ExitException;
import com.zendesk.challenge.exception.UserInputException;
import com.zendesk.challenge.search.Index;

import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IOService {
    private static final Scope[] SCOPES = Scope.values();
    private static final Map<Scope, String[]> FIELD_BY_SCOPE = null;

    private Scanner scanner;
    private PrintStream out;
    private Map<Scope, List<String>> indexKeys;

    public IOService(InputStream in, PrintStream out, List<Index<? extends Entity>> indices) {
        this.scanner = new Scanner(in);
        this.out = out;
        this.indexKeys = indices.stream()
                .map(i -> Map.entry(i.getScope(), i.getFieldsIndexed()
                        .stream()
                        .map(Field::getName)
                        .collect(Collectors.toList())))
                .collect(Collectors.toMap(k -> (Scope) k.getKey()
                        , k -> (List<String>) k.getValue()));
    }

    public UserInput getNext() throws UserInputException, ExitException {
        try {
            out.println(getScopesString());
            String value = scanner.nextLine();
            if (Integer.parseInt(value) == SCOPES.length + 1) {
                throw new ExitException();
            }
            Scope scope = SCOPES[Integer.parseInt(value) - 1];
            out.println("You selected - " + scope.getValue());
            out.println(getScopeProperties(scope));
            value = scanner.nextLine();
            if (Integer.parseInt(value) == indexKeys.get(scope).size() + 1) {
                throw new ExitException();
            }
            String indexBy = indexKeys.get(scope).get(Integer.parseInt(value) - 1);
            out.println("You selected - " + indexBy);
            out.println("Enter the value you'd like to search for - ");
            value = scanner.nextLine();
            return new UserInput(scope, indexBy, value);
        } catch (ExitException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UserInputException();
        }
    }

    private String getScopesString() {
        StringBuilder builder = new StringBuilder("Choose from the following scopes ");
        builder.append("(Type the number corresponding to the scope/data type you would like to search and hit enter when done) - \n");
        IntStream.range(0, SCOPES.length)
                .forEach(index -> builder.append(String.format("%d. %s\n", index + 1, SCOPES[index].getValue())));
        builder.append(String.format("%d. %s\n", SCOPES.length + 1, "Exit"));
        builder.append("Enter your choice now - \n");
        return builder.toString();
    }

    private String getScopeProperties(Scope scope) {
        StringBuilder builder = new StringBuilder(String.format("Choose from the following properties available for %s \n", scope.getValue()));
        builder.append("(Type the number corresponding to the property/data type you would like to search and hit enter when done) - \n");
        List<String> keys = indexKeys.get(scope);
        IntStream.range(0, keys.size())
                .forEach(index -> builder.append(String.format("%d. %s\n", index + 1, keys.get(index))));
        builder.append(String.format("%d. %s\n", keys.size() + 1, "Exit"));
        builder.append("Enter your choice now - \n");
        return builder.toString();
    }

    public void displayResult(UserInput input, Set<? extends Entity> results) {
        out.println("********************************************Search Result ************************************************");
        out.println(IOUtils.prettyFormat(results));
        out.println("********************************************End Search Result ********************************************");
    }
}
