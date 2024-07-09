/*
 * G4IT
 * Copyright 2023 Sopra Steria
 *
 * This product includes software developed by
 * French Ecological Ministery (https://gitlab-forge.din.developpement-durable.gouv.fr/pub/numeco/m4g/numecoeval)
 */
package com.soprasteria.g4it.backend.apiadministrator.business;

import com.soprasteria.g4it.backend.apiuser.model.SubscriberBO;
import com.soprasteria.g4it.backend.apiuser.model.UserBO;
import com.soprasteria.g4it.backend.apiuser.model.UserSearchBO;
import com.soprasteria.g4it.backend.apiuser.modeldb.Role;
import com.soprasteria.g4it.backend.apiuser.modeldb.Subscriber;
import com.soprasteria.g4it.backend.apiuser.modeldb.User;
import com.soprasteria.g4it.backend.apiuser.modeldb.UserOrganization;
import com.soprasteria.g4it.backend.apiuser.repository.SubscriberRepository;
import com.soprasteria.g4it.backend.apiuser.repository.UserRepository;
import com.soprasteria.g4it.backend.common.utils.Constants;
import com.soprasteria.g4it.backend.exception.G4itRestException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Organization service.
 */
@Service
@AllArgsConstructor
@Slf4j
public class AdministratorService {

    /**
     * The Administrator Role Service
     */
    AdministratorRoleService administratorRoleService;

    /**
     * Repository to access subscriber data.
     */
    SubscriberRepository subscriberRepository;

    /**
     * Repository to access user data.
     */
    UserRepository userRepository;

    /**
     * Retrieve the list of subscribers for the user which has ROLE_SUBSCRIBER_ADMINISTRATOR on it
     *
     * @param user the user.
     * @return the List<SubscriberBO>.
     */
    public List<SubscriberBO> getSubscribers(final UserBO user) {
        administratorRoleService.hasAdminRightsOnAnySubscriber(user);

        return user.getSubscribers().stream()
                .filter(subscriberBO -> subscriberBO.getRoles().contains(Constants.ROLE_SUBSCRIBER_ADMINISTRATOR))
                .toList();
    }


    /**
     * Get all the users (filtered by authorized_domains of subscriber)
     *
     * @param searchedName the string to be searched
     * @param subscriberId the subscriber's id
     * @param user         the  user
     */
    public List<UserSearchBO> searchUserByName(final String searchedName,
                                               final Long subscriberId,
                                               final Long organizationId,
                                               final UserBO user) {

        administratorRoleService.hasAdminRightOnSubscriberOrOrganization(user, subscriberId, organizationId);

        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new G4itRestException("404", String.format("Subscriber %d not found.", subscriberId)));

        if (subscriber.getAuthorizedDomains() == null) return List.of();

        Set<String> domains = Arrays.stream(subscriber.getAuthorizedDomains().replaceAll("\\s+", "").split(","))
                .collect(Collectors.toSet());

        List<User> searchedList = userRepository.findBySearchedName(searchedName, domains);
        if (searchedList.isEmpty()) return List.of();

        return searchedList.stream()
                .map(searchedUser -> {

                    List<String> userRoles = new ArrayList<>();
                    if (searchedUser.getUserOrganizations() != null) {
                        userRoles.addAll(searchedUser.getUserOrganizations().stream().filter(org -> org.getOrganization().getId() == organizationId)
                                .findFirst()
                                .orElse(UserOrganization.builder().roles(List.of()).build())
                                .getRoles().stream().map(Role::getName).toList());
                    }

                    if (searchedUser.getUserSubscribers() != null) {
                        userRoles.addAll(searchedUser.getUserSubscribers().stream()
                                .filter(userSubscriber -> userSubscriber.getSubscriber().getId() == subscriberId)
                                .map(userSubscriber -> userSubscriber.getUserRoleSubscriber().stream()
                                        .map(userRoleSubscriber -> userRoleSubscriber.getRoles().getName())
                                        .toList()
                                )
                                .flatMap(Collection::stream)
                                .toList());
                    }

                    List<Long> linkedOrgIds = searchedUser.getUserOrganizations() == null ? List.of() :
                            searchedUser.getUserOrganizations().stream()
                                    .map(userOrg -> userOrg.getOrganization().getId())
                                    .toList();

                    UserSearchBO userSearch = UserSearchBO.builder()
                            .id(searchedUser.getId())
                            .firstName(searchedUser.getFirstName())
                            .lastName(searchedUser.getLastName())
                            .email(searchedUser.getEmail())
                            .linkedOrgIds(linkedOrgIds)
                            .roles(userRoles)
                            .build();

                    return userSearch;
                })
                .toList();
    }

}
