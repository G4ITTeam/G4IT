/*
 * G4IT
 * Copyright 2023 Sopra Steria
 *
 * This product includes software developed by
 * French Ecological Ministery (https://gitlab-forge.din.developpement-durable.gouv.fr/pub/numeco/m4g/numecoeval)
 */ 
package com.soprasteria.g4it.backend.apibatchloading.steps.virtualequipment.tasklet;

import com.soprasteria.g4it.backend.apiinventory.repository.VirtualEquipmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Date;

/**
 * Tasklet to delete all not linked virtual equipments.
 */
@AllArgsConstructor
public class DeleteAfterConsistencyCheckTasklet implements Tasklet {

    /**
     * Repository to access data.
     */
    private VirtualEquipmentRepository virtualEquipmentRepository;

    /**
     * The session date.
     */
    private Date sessionDate;

    /**
     * {@inheritDoc}
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        virtualEquipmentRepository.deleteAfterConsistencyControl(sessionDate);
        return RepeatStatus.FINISHED;
    }
}
