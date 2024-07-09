/*
 * G4IT
 * Copyright 2023 Sopra Steria
 *
 * This product includes software developed by
 * French Ecological Ministery (https://gitlab-forge.din.developpement-durable.gouv.fr/pub/numeco/m4g/numecoeval)
 */
package com.soprasteria.g4it.backend.common.filesystem.business;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobContainerItem;
import com.azure.storage.blob.models.BlobContainerListDetails;
import com.azure.storage.blob.models.ListBlobContainersOptions;
import com.soprasteria.g4it.backend.common.filesystem.exception.FileStorageAccessExcepton;
import com.soprasteria.g4it.backend.common.filesystem.external.VaultAccessClient;
import com.soprasteria.g4it.backend.common.filesystem.model.FileStorage;
import com.soprasteria.g4it.backend.common.filesystem.model.FileSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * FileSystem implementation for Azure.
 */
@Component
@RequiredArgsConstructor
@Profile("azure")
public class AzureFileSystem implements FileSystem {

    /**
     * Blob prefix for G4IT.
     */
    public static final String G4IT_BLOB_CONTAINER_PREFIX = "g4it";

    /**
     * The G4IT client to access vault.
     */
    private final VaultAccessClient vaultAccessClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public FileStorage mount(final String subscriber, final String organization) {
        // Retrieve subscriber's connectionString.
        final String subscriberConnectionString = vaultAccessClient.getConnectionStringForSubscriber(subscriber);

        // Find first subscriber's blob storage.
        final BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(subscriberConnectionString)
                .buildClient();
        final ListBlobContainersOptions options = new ListBlobContainersOptions()
                .setPrefix(G4IT_BLOB_CONTAINER_PREFIX)
                .setDetails(new BlobContainerListDetails().setRetrieveMetadata(true));
        final BlobContainerItem blobContainer = blobServiceClient.listBlobContainers(options, null)
                .stream()
                .findFirst()
                .orElseThrow(() -> new FileStorageAccessExcepton(String.format("Unable to find the storage of the subscriber %s.", subscriber)));
        final String storageName = blobContainer.getName();

        // Create the G4IT azureFileStorage service.
        return new AzureFileStorage(
                blobServiceClient,
                blobServiceClient.getBlobContainerClient(storageName),
                organization, String.join("/", "azure-blob:/", storageName));
    }
}
