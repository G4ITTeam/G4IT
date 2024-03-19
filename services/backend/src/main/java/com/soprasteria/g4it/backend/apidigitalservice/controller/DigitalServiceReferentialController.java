/*
 * G4IT
 * Copyright 2023 Sopra Steria
 *
 * This product includes software developed by
 * French Ecological Ministery (https://gitlab-forge.din.developpement-durable.gouv.fr/pub/numeco/m4g/numecoeval)
 */ 
package com.soprasteria.g4it.backend.apidigitalservice.controller;

import com.soprasteria.g4it.backend.apidigitalservice.business.DigitalServiceReferentialService;
import com.soprasteria.g4it.backend.apidigitalservice.mapper.DigitalServiceReferentialRestMapper;
import com.soprasteria.g4it.backend.server.gen.api.DigitalServiceReferentialApiDelegate;
import com.soprasteria.g4it.backend.server.gen.api.dto.DeviceTypeRefRest;
import com.soprasteria.g4it.backend.server.gen.api.dto.NetworkTypeRefRest;
import com.soprasteria.g4it.backend.server.gen.api.dto.ServerHostRefRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Digital Service referential endpoints.
 */
@Service
public class DigitalServiceReferentialController implements DigitalServiceReferentialApiDelegate {

    /**
     * Referential Service.
     */
    @Autowired
    private DigitalServiceReferentialService digitalServiceReferentialService;

    /**
     * Device type ref mapper.
     */
    @Autowired
    private DigitalServiceReferentialRestMapper digitalServiceReferentialRestMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<List<DeviceTypeRefRest>> getTerminalDeviceTypeRef(final String subscriber, final String organization) {
        return ResponseEntity.ok(digitalServiceReferentialRestMapper.toDeviceTypeDto(digitalServiceReferentialService.getTerminalDeviceType()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<List<String>> getCountryRef(final String subscriber, final String organization) {
        return ResponseEntity.ok(digitalServiceReferentialService.getCountry());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<List<NetworkTypeRefRest>> getNetworkTypeRef(final String subscriber, final String organization) {
        return ResponseEntity.ok(digitalServiceReferentialRestMapper.toNetworkTypeDto(digitalServiceReferentialService.getNetworkType()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<List<ServerHostRefRest>> getServerHost(final String subscriber,
                                                                 final String organization,
                                                                 final String type) {
        return ResponseEntity.ok(digitalServiceReferentialRestMapper.toServerHostDto(digitalServiceReferentialService.getServerHosts(type)));
    }

}
