package org.stroganoff.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class HistoryTest {
    public static final String TEST_STRING = "Тестовое сообщение";
    @InjectMocks
    HistoryManager historyManager;

    @Test
    void putMessage_Must_Return_True() {
        // GIVEN
        String testMessage = TEST_STRING;
        // WHEN
        boolean actual = historyManager.putMessage(testMessage);
        // THEN
        assertTrue(actual);
    }

    @Test
    void getHistoryMessage_WhenPut_7_Messages_ThenReturn_5_Messages() {
        // GIVEN
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            stringBuilder.append(TEST_STRING).append("\n");
        }
        String expected = stringBuilder.toString();
        // WHEN
        for (int i = 0; i < 7; i++) {
            historyManager.putMessage(TEST_STRING);
        }
        String actual = historyManager.getHistoryMessage(5).toString();
        // THEN
        assertEquals(expected, actual);
    }
}