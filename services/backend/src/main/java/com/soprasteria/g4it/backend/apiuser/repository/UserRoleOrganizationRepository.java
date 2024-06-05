/*
 * G4IT
 * Copyright 2023 Sopra Steria
 *
 * This product includes software developed by
 * French Ecological Ministery (https://gitlab-forge.din.developpement-durable.gouv.fr/pub/numeco/m4g/numecoeval)
 */
package com.soprasteria.g4it.backend.apiuser.repository;

import com.soprasteria.g4it.backend.apiuser.modeldb.UserOrganization;
import com.soprasteria.g4it.backend.apiuser.modeldb.UserRoleOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleOrganizationRepository extends JpaRepository<UserRoleOrganization, Long> {

    /**
     * Find by userOrganization and return a list of matching UserRoleOrganizations
     *
     * @param userOrganization the linked userOrganization
     * @return UserRoleOrganization list
     */
    List<UserRoleOrganization> findByUserOrganizations(final UserOrganization userOrganization);

}
