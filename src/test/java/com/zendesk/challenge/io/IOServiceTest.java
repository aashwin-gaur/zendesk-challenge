package com.zendesk.challenge.io;

import com.zendesk.challenge.domain.commons.Entity;
import com.zendesk.challenge.domain.commons.Scope;
import com.zendesk.challenge.exception.ExitException;
import com.zendesk.challenge.exception.UserInputException;
import com.zendesk.challenge.search.Index;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class IOServiceTest {

    private IOService toTest;

    private PrintStream out;

    private List<Index<? extends Entity>> indices = new ArrayList<>();

    private Field testField;

    private ByteArrayOutputStream baos;

    @Mock
    private Index<? extends Entity> index;

    @BeforeEach
    public void setup() throws UnsupportedEncodingException, NoSuchFieldException {
        MockitoAnnotations.openMocks(this);
        indices.add(index);

        baos = new ByteArrayOutputStream();
        String utf8 = StandardCharsets.UTF_8.name();
        out = new PrintStream(baos, true, utf8);

        testField = this.getClass().getDeclaredField("testField");
        Mockito.when(index.getFieldsIndexed()).thenReturn(Set.of(testField));
        Mockito.when(index.getScope()).thenReturn(Scope.ORGANIZATIONS);

    }

    @Test
    public void testValid() throws Throwable {
        String inputString = "1\n1\ntestString\n";
        toTest = new IOService(new ByteArrayInputStream(inputString.getBytes()), out, indices);

        UserInput in = toTest.getNext();

        Assertions.assertEquals(in.getCategory(), Scope.ORGANIZATIONS);
        Assertions.assertEquals(in.getField(), "testField");
        Assertions.assertEquals(in.getValue(), "testString");
        String output = baos.toString();
        Assertions.assertTrue(output.contains("You selected - " + Scope.ORGANIZATIONS.getValue()));
        Assertions.assertTrue(output.contains("You selected - " + "testField"));
    }

    @Test
    public void testEmptyStringSearchValid() throws Throwable {
        String inputString = "1\n1\n\n";
        toTest = new IOService(new ByteArrayInputStream(inputString.getBytes()), out, indices);

        UserInput in = toTest.getNext();
        Assertions.assertEquals(in.getValue(), "");
    }

    @Test
    public void testErrorInFieldSelection() {
        String inputString = "1\n10\n";
        toTest = new IOService(new ByteArrayInputStream(inputString.getBytes()), out, indices);
        Assertions.assertThrows(UserInputException.class, () -> toTest.getNext());
    }

    @Test
    public void testErrorInScopeSelection() {
        String inputString = "10\n";
        toTest = new IOService(new ByteArrayInputStream(inputString.getBytes()), out, indices);
        Assertions.assertThrows(UserInputException.class, () -> toTest.getNext());
    }

    @Test
    public void testErrorUnrecognizedInput() {
        String inputString = "test\n";
        toTest = new IOService(new ByteArrayInputStream(inputString.getBytes()), out, indices);
        Assertions.assertThrows(UserInputException.class, () -> toTest.getNext());
    }

    @Test
    public void testExit() {
        String inputString = "4\n";
        toTest = new IOService(new ByteArrayInputStream(inputString.getBytes()), out, indices);
        Assertions.assertThrows(ExitException.class, () -> toTest.getNext());
    }

    @Test
    public void testExit2() {
        String inputString = "1\n2\n";
        toTest = new IOService(new ByteArrayInputStream(inputString.getBytes()), out, indices);
        Assertions.assertThrows(ExitException.class, () -> toTest.getNext());
    }

    @AfterEach
    public void destroy() {
    }

}
