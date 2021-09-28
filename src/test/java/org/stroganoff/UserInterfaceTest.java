package org.stroganoff;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserInterfaceTest {
    public static final String TEST_STRING = "TEST String";
    public static final String TEST_ERROR_STRING = "Произошла ошибка";
    @InjectMocks
    UserInterface userInterface;

    @Test
    void getStringFromUser_Return_TestString() throws IOException {
        // GIVEN
        String expectedString = "test";
        BufferedReader reader = Mockito.mock(BufferedReader.class);
        Mockito.when(reader.readLine()).thenReturn("test");
        // WHEN
        String actualString = userInterface.getStringFromUser(reader);
        // THEN
        assertEquals(expectedString, actualString);
    }

    @Test
    void showOutputMessage_System_out_PrintLn_Used() {
        // GIVEN
        byte[] expected = TEST_STRING.getBytes();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(bos, true);
        PrintStream oldStream = System.out;
        System.setOut(printStream);
        // WHEN
        userInterface.showUserMessage("");
        byte[] actualString = TEST_STRING.getBytes();
        // THEN
        assertArrayEquals(expected, actualString);
        System.setOut(oldStream);
    }

    @Test
    void showErrorMessage_System_out_PrintLn_Used() {
        // GIVEN
        byte[] expected = TEST_ERROR_STRING.getBytes();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(bos, true);
        PrintStream oldStream = System.out;
        System.setOut(printStream);
        // WHEN
        userInterface.showErrorMessage("");
        byte[] actualString = TEST_ERROR_STRING.getBytes();
        // THEN
        assertArrayEquals(expected, actualString);
        System.setOut(oldStream);
    }
}