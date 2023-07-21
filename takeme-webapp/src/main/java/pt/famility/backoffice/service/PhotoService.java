package pt.famility.backoffice.service;

import org.springframework.stereotype.Service;
import pt.famility.backoffice.config.ApplicationProperties;
import pt.famility.backoffice.domain.DocumentFile;
import pt.famility.backoffice.repository.DocumentFileRepository;
import pt.famility.backoffice.service.dto.PhotoDTO;
import pt.famility.backoffice.util.ResizeImage;

import java.io.IOException;
import java.util.Base64;

@Service
public class PhotoService {

    private ApplicationProperties applicationProperties;

    private DocumentFileRepository documentFileRepository;

    public PhotoService(ApplicationProperties applicationProperties, DocumentFileRepository documentFileRepository) {
        this.applicationProperties = applicationProperties;
        this.documentFileRepository = documentFileRepository;
    }

    public DocumentFile savePhoto(String photo) throws IOException {
        byte[] photoBytes = ResizeImage.resize(Base64.getDecoder().decode(photo), applicationProperties.getPhoto().getWidth(), applicationProperties.getPhoto().getHeight(), applicationProperties.getPhoto().getContentType());
        DocumentFile documentFile = new DocumentFile();
        documentFile.setFileContent(photoBytes);
        return documentFileRepository.save(documentFile);
    }

    public void deletePhoto(DocumentFile documentFile) {
        documentFileRepository.delete(documentFile);
    }

    public PhotoDTO getPhoto(DocumentFile photoFile) {
        return new PhotoDTO(Base64.getEncoder().encodeToString(photoFile.getFileContent()));
    }
}
