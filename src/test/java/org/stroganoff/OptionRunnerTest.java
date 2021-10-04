package org.stroganoff;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class OptionRunnerTest {

    private static final String SERVER_START_STRING = "Server socket created, and listen to server commands";
    ByteArrayOutputStream bos;
    PrintStream printStream;
    PrintStream oldStream;
    BufferedReader reader;
    @Mock
    UserInterface userInterface;

    @Mock
    Properties properties;

    @InjectMocks
    OptionRunner optionRunner;

    @BeforeAll
    void setSystemIn() {
        String exitCommand = "exit";
        InputStream is = new ByteArrayInputStream(exitCommand.getBytes());
        //подменяем System.in
        System.setIn(is);
        bos = new ByteArrayOutputStream();
        printStream = new PrintStream(bos, true);
        oldStream = System.out;
        //подменяем System. Out
        System.setOut(printStream);
        reader = Mockito.mock(BufferedReader.class);
    }

    @AfterAll
    void afterTests() {
        System.setOut(oldStream);
    }

    @Test
    void whenOptionRun_userCommand_Is_1_Then_ServerStarts() {
        // GIVEN
        Mockito.when(userInterface.getStringFromUser(reader)).thenReturn("1");
        Mockito.when(properties.getProperty("port")).thenReturn("1212");
        String expected = SERVER_START_STRING + "\r\n" + "Для выхода введите - exit" + "\r\n";
        // WHEN
        optionRunner.optionRun(userInterface, properties, reader);
        // THEN
        Mockito.verify(properties, Mockito.times(1)).getProperty("port");
        Mockito.verify(userInterface, Mockito.times(1)).getStringFromUser(reader);
        String actualString = bos.toString(StandardCharsets.UTF_8);
        assertEquals(expected, actualString);
    }

    @Test
    void whenOptionRun_userCommand_Is_2_Then_ClientStarts() {
        // GIVEN
        Mockito.when(userInterface.getStringFromUser(reader)).thenReturn("2");
        Mockito.when(properties.getProperty("port")).thenReturn("1212");
        String expected = "Произошла ошибка: Connection refused: connect" + "\r\n";
        // WHEN
        optionRunner.optionRun(userInterface, properties, reader);
        // THEN
        Mockito.verify(properties, Mockito.times(1)).getProperty("port");
        Mockito.verify(userInterface, Mockito.times(2)).getStringFromUser(reader);
        String actualString = bos.toString(StandardCharsets.UTF_8);
        assertEquals(expected, actualString);
    }

    @Test
    void whenOptionRun_userCommand_Is_q_Then_ShowUserMessageAndExit() {
        // GIVEN
        Mockito.when(userInterface.getStringFromUser(reader)).thenReturn("q");
        Mockito.when(properties.getProperty("port")).thenReturn("1212");
        String expected = OptionRunner.EXIT_MESSAGE + "\r\n";
        // WHEN
        optionRunner.optionRun(userInterface, properties, reader);
        // THEN
        String actualString = bos.toString(StandardCharsets.UTF_8);
        Mockito.verify(userInterface, Mockito.times(1)).showUserMessage(Mockito.any());
        assertEquals(expected, actualString);
    }
}