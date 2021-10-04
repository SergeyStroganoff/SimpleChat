package org.stroganoff.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.stroganoff.UserInterface;
import org.stroganoff.util.IMessenger;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ClientTest {
    @Mock
    IMessenger iMessenger;
    @Mock
    Socket socket;
    @Mock
    DataOutputStream dataOutputStream;
    @Mock
    DataInputStream dataInputStream;
    @Mock
    BufferedReader reader;
    @Mock
    UserInterface userInterface;

    @Test
    void clientStartTest() throws IOException {
        //GIVEN
        //WHEN
        Client client = new Client(socket, dataOutputStream, dataInputStream,
                reader, "testName", iMessenger, userInterface);
        //THEN
        Mockito.verify(userInterface, Mockito.times(1)).showErrorMessage(Mockito.anyString());
    }
}