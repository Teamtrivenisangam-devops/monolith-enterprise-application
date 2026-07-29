/*
 * |-------------------------------------------------
 * | Copyright © 2018 Colin But. All rights reserved.
 * |-------------------------------------------------
 */
package com.mycompany.entapp.snowman.domain.service.impl;

import com.mycompany.entapp.snowman.domain.exception.SnowmanException;
import com.mycompany.entapp.snowman.domain.model.Client;
import com.mycompany.entapp.snowman.domain.model.Project;
import com.mycompany.entapp.snowman.domain.repository.ClientRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceImplUTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientServiceImpl;

    @Test
    public void testGetClient() throws Exception {
        int clientId = 1;

        Client client = getClient();

        Mockito.when(clientRepository.getClient(clientId)).thenReturn(client);

        Client actualClient = clientServiceImpl.getClient(clientId);

        assertNotNull(actualClient);
        assertEquals(client, actualClient);
        Mockito.verify(clientRepository, times(1)).getClient(clientId);
    }

    @Test
    public void testCreateClient() throws Exception {
        Client client = getClient();

        Mockito.when(clientRepository.getClient(client.getId())).thenReturn(null);
        Mockito.doNothing().when(clientRepository).createClient(client);

        clientServiceImpl.createClient(client);

        Mockito.verify(clientRepository, times(1)).createClient(client);
    }

    @Test(expected = SnowmanException.class)
    public void testCreateClientThrowsException() throws SnowmanException {
        Client client = getClient();

        Mockito.when(clientRepository.getClient(client.getId())).thenReturn(client);
        clientServiceImpl.createClient(client);
    }

    @Test
    public void testUpdateClient() throws Exception {
        Client client = getClient();

        Mockito.when(clientRepository.getClient(client.getId())).thenReturn(client);
        Mockito.doNothing().when(clientRepository).updateClient(client);

        clientServiceImpl.updateClient(client);

        Mockito.verify(clientRepository, times(1)).updateClient(client);
    }

    @Test
    public void testDeleteClient() throws Exception {
        int clientId = 1;

        Mockito.when(clientRepository.getClient(clientId)).thenReturn(new Client());
        Mockito.doNothing().when(clientRepository).deleteClient(clientId);

        clientServiceImpl.deleteClient(clientId);

        Mockito.verify(clientRepository, Mockito.times(1)).deleteClient(clientId);
    }

    @Test(expected = SnowmanException.class)
    public void testDeleteClientThrowException_whenNothingToDelete() throws SnowmanException {
        int clientId = 1;

        Mockito.when(clientRepository.getClient(clientId)).thenReturn(null);

        clientServiceImpl.deleteClient(clientId);
    }

    private Client getClient() {
        Client client = new Client();
        client.setId(1);
        client.setClientName("Client");
        client.setProjects(Collections.<Project>emptySet());
        return client;
    }
}
