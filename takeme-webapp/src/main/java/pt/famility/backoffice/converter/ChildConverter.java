package pt.famility.backoffice.converter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.Child;
import pt.famility.backoffice.service.dto.ChildDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class ChildConverter {

    private final ObjectMapper objectMapper;

    public ChildConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Child convert(ChildDTO childDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        LocalDateDeserializer deserializer = new LocalDateDeserializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        JavaTimeModule module = new JavaTimeModule();
        module.addDeserializer(LocalDate.class, deserializer);
        objectMapper.registerModule(module);
        return objectMapper.convertValue(childDTO, Child.class);
    }

    public ChildDTO convert(Child child) {
        ChildDTO childDTO = objectMapper.convertValue(child, ChildDTO.class);
        Optional.ofNullable(child.getPhotoFile()).ifPresent(documentFile -> childDTO.setPhotoId(documentFile.getId()));
        return childDTO;
    }
}
