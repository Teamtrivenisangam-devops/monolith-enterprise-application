/*
 * |-------------------------------------------------
 * | Copyright © 2018 Colin But. All rights reserved.
 * |-------------------------------------------------
 */
package com.mycompany.entapp.snowman.infrastructure.rest.mappers;

import com.mycompany.entapp.snowman.domain.model.Client;
import com.mycompany.entapp.snowman.domain.model.Project;
import com.mycompany.entapp.snowman.infrastructure.rest.resources.ClientResource;
import com.mycompany.entapp.snowman.infrastructure.rest.resources.ProjectResource;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class ClientResourceMapperUTest {

    @Test
    public void testMapToClient() throws Exception {
        int clientId = 1;
        String clientName = "Client";

        ClientResource clientResource = new ClientResource();
        clientResource.setClientId(clientId);
        clientResource.setClientName(clientName);

        ProjectResource projectResource = new ProjectResource();
        projectResource.setProjectId(10);
        projectResource.setTitle("Project");

        List<ProjectResource> projectResources = new ArrayList<>();
        projectResources.add(projectResource);
        clientResource.setProjects(projectResources);

        Client client = ClientResourceMapper.mapToClient(clientResource);

        assertEquals(clientId, client.getId());
        assertEquals(clientName, client.getClientName());
        assertEquals(1, client.getProjects().size());
    }

    @Test
    public void testMapToClientResource() throws Exception {
        int clientId = 1;
        String clientName = "Client";

        Client client = new Client();
        client.setId(clientId);
        client.setClientName(clientName);

        Project project = new Project();
        project.setId(10);
        project.setProjectTitle("Project");

        Set<Project> projects = new HashSet<>();
        projects.add(project);
        client.setProjects(projects);

        ClientResource clientResource = ClientResourceMapper.mapToClientResource(client);

        assertEquals(clientId, clientResource.getClientId());
        assertEquals(clientName, clientResource.getClientName());
        assertEquals(1, clientResource.getProjects().size());
    }

}
