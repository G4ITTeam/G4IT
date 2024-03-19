/*
 * G4IT
 * Copyright 2023 Sopra Steria
 *
 * This product includes software developed by
 * French Ecological Ministery (https://gitlab-forge.din.developpement-durable.gouv.fr/pub/numeco/m4g/numecoeval)
 */ 
package com.soprasteria.g4it.backend.apiuser.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * User Business Object.
 */
@Data
@SuperBuilder
public class UserBO {

    /**
     * Email domain.
     */
    private String username;

    /**
     * User's subscriber.
     */
    private List<SubscriberBO> subscribers;

}
