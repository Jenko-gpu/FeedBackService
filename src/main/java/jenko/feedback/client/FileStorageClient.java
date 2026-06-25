package jenko.feedback.client;

import jenko.feedback.dto.FileMetadataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "file-storage-service")
public interface FileStorageClient {

    @GetMapping("/api/v1/files/{id}/metadata")
    FileMetadataDto getMetadata(@PathVariable("id") UUID fileId);
}