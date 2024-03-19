/*
 * G4IT
 * Copyright 2023 Sopra Steria
 *
 * This product includes software developed by
 * French Ecological Ministery (https://gitlab-forge.din.developpement-durable.gouv.fr/pub/numeco/m4g/numecoeval)
 */ 
package com.soprasteria.g4it.backend.apiuser.mapper;

import com.soprasteria.g4it.backend.apiuser.model.SubscriberBO;
import com.soprasteria.g4it.backend.server.gen.api.dto.SubscriberRest;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * UserRest Mapper.
 */
@Mapper(componentModel = "spring", uses = OrganizationRestMapper.class)
public interface SubscriberRestMapper {

    /**
     * Map a business object to dto object.
     *
     * @param businessObject the source.
     * @return the SubscriberRest.
     */
    SubscriberRest toDto(final SubscriberBO businessObject);

    /**
     * Map a business object list to dto object list.
     *
     * @param businessObject the source.
     * @return the SubscriberRest list.
     */
    List<SubscriberRest> toDto(final List<SubscriberBO> businessObject);
}
