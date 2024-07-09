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
 * User Info Business Object.
 */
@Data
@SuperBuilder
public class UserInfoBO {

    long id;

    /**
     * The email.
     */
    private String email;

    /**
     * The firstName of the user.
     */
    private String firstName;

    /**
     * The lastName of the user.
     */
    private String lastName;
    /**
     * The roles of the user.
     */
    private List<String> roles;

}
