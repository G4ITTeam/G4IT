/*
 * G4IT
 * Copyright 2023 Sopra Steria
 *
 * This product includes software developed by
 * French Ecological Ministery (https://gitlab-forge.din.developpement-durable.gouv.fr/pub/numeco/m4g/numecoeval)
 */ 
package com.soprasteria.g4it.backend.apiuser.business;

import com.soprasteria.g4it.backend.apiuser.model.OrganizationBO;
import com.soprasteria.g4it.backend.apiuser.model.SubscriberBO;
import com.soprasteria.g4it.backend.exception.AuthorizationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    // Given global
    private static final String subscriber = "subscriber";
    private static final String organization = "organization";
    private static final List<String> roles = List.of("Role 1", "Role2");

    private static final List<SubscriberBO> userSubscribers = List.of(SubscriberBO.builder()
            .name(subscriber)
            .organizations(List.of(OrganizationBO.builder()
                    .name(organization)
                    .roles(roles)
                    .build()))
            .build());


    @Test
    void testControlAccess_nominalCase_returnAllRoles() {
        List<String> actual = ReflectionTestUtils.invokeMethod(authService, "controlAccess", 0L, userSubscribers, subscriber, organization);
        Assertions.assertEquals(roles, actual);
    }

    @Test
    void testControlAccess_withUnknownSubscriber_thenUnauthorized() throws Exception {
        Assertions.assertThrows(AuthorizationException.class, () -> {
            ReflectionTestUtils.invokeMethod(authService, "controlAccess", 0L, userSubscribers, "BadSubscriber", organization);
        });
    }

    @Test
    void testControlAccess_withUnknownOrganization_thenUnauthorized() {
        Assertions.assertThrows(AuthorizationException.class, () -> {
            ReflectionTestUtils.invokeMethod(authService, "controlAccess", 0L, userSubscribers, subscriber, "BadOrganization");
        });
    }

    @Test
    void testControlAccess_withNoRole_thenForbidden() throws Exception {
        final List<SubscriberBO> userSubscribersWithoutRole = List.of(SubscriberBO.builder()
                .name(subscriber)
                .organizations(List.of(OrganizationBO.builder()
                        .name(organization)
                        .roles(List.of())
                        .build()))
                .build());

        Assertions.assertThrows(AuthorizationException.class, () -> {
            ReflectionTestUtils.invokeMethod(authService, "controlAccess", 0L, userSubscribersWithoutRole, subscriber, organization);
        });
    }

}
