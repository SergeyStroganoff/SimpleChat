package org.stroganoff.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.DataOutputStream;
import java.io.IOException;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class MessengerTest {

    @Mock
    DataOutputStream dataOutputStream;

    @InjectMocks
    Messenger messenger;

    @Test
    void sendMessage() throws IOException {
        // GIVEN
        String test = "simple string";
        // WHEN
        messenger.sendMessage(dataOutputStream, test);
        // THEN
        Mockito.verify(dataOutputStream, Mockito.times(1)).writeUTF(test);
        Mockito.verify(dataOutputStream, Mockito.times(1)).flush();
    }
}