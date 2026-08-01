/*
 * |-------------------------------------------------
 * | Copyright © 2017 Colin But. All rights reserved.
 * |-------------------------------------------------
 */
package com.mycompany.entapp.snowman.infrastructure.rest.endpoint;

import com.mycompany.entapp.snowman.domain.model.User;
import com.mycompany.entapp.snowman.domain.service.impl.UserServiceImpl;
import com.mycompany.entapp.snowman.infrastructure.rest.resources.UserResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class UserRestEndpointUTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserRestEndpoint classInTest = new UserRestEndpoint();


    @Test
    public void getUserWithUserIdShouldReturnTheUser() {
        User user = new User();
        user.setUserId(1);
        user.setUsername("Username");
        user.setPassword("Password");
        user.setFirstname("Firstname");
        user.setLastname("Secondname");
        user.setEmail("Email");

        Mockito.when(userService.findUser("1")).thenReturn(user);

        ResponseEntity<UserResource> responseEntity = classInTest.getUser("1");

        assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
        assertEquals(user.getUserId(), responseEntity.getBody().getUserId());
        assertEquals(user.getUsername(), responseEntity.getBody().getUsername());
        assertEquals(user.getEmail(), responseEntity.getBody().getEmail());
    }

    @Test
    public void createUserShouldCreateUser(){

        UserResource userResource = new UserResource();
        userResource.setUserId(1);
        userResource.setUsername("Username");
        userResource.setPassword("Password");
        userResource.setFirstName("Firstname");
        userResource.setSecondName("Lastname");
        userResource.setEmail("Email");

        Mockito.doNothing().when(userService).createUser(Mockito.any(User.class));

        classInTest.createNewUser(userResource);

        Mockito.verify(userService, Mockito.times(1)).createUser(Mockito.any(User.class));
    }

    @Test
    public void testUpdateUser(){
        UserResource userResource = new UserResource();
        userResource.setUserId(1);
        userResource.setUsername("Username");
        userResource.setPassword("Password");
        userResource.setFirstName("Firstname");
        userResource.setSecondName("Lastname");
        userResource.setEmail("Email");

        Mockito.doNothing().when(userService).updateUser(Mockito.any(User.class));

        classInTest.updateExistingUser(userResource);

        Mockito.verify(userService, Mockito.times(1)).updateUser(Mockito.any(User.class));
    }

    @Test
    public void testDeleteUser(){
        int userId = 1;

        Mockito.doNothing().when(userService).deleteUser(userId);

        classInTest.deleteUser(userId);

        Mockito.verify(userService, Mockito.times(1)).deleteUser(userId);
    }
}
