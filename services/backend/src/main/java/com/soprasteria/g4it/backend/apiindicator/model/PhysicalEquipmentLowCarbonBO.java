/*
 * G4IT
 * Copyright 2023 Sopra Steria
 *
 * This product includes software developed by
 * French Ecological Ministery (https://gitlab-forge.din.developpement-durable.gouv.fr/pub/numeco/m4g/numecoeval)
 */ 
package com.soprasteria.g4it.backend.apiindicator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PhysicalEquipmentLowCarbonBO {

    private String organisation;

    private String inventoryName;

    private String paysUtilisation;

    private String type;

    private String nomEntite;

    private String statut;

    private Integer quantite;

    private Boolean lowCarbon;

}
