package pt.famility.backoffice.converter;

import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.Tutor;
import pt.famility.backoffice.domain.User;
import pt.famility.backoffice.repository.IdentificationCardTypeRepository;
import pt.famility.backoffice.repository.UserRepository;
import pt.famility.backoffice.service.dto.TutorDTO;
import pt.famility.backoffice.web.rest.errors.InvalidIDException;
import pt.famility.backoffice.web.rest.errors.InvalidIdentificationCardTypeException;

import java.util.Optional;

@Component
public class TutorConverter {

    private final ObjectMapper objectMapper;

    private IdentificationCardTypeRepository identificationCardTypeRepository;

    private UserRepository userRepository;

    public TutorConverter(ObjectMapper objectMapper, IdentificationCardTypeRepository identificationCardTypeRepository, UserRepository userRepository) {
        this.objectMapper = objectMapper;
        this.identificationCardTypeRepository = identificationCardTypeRepository;
        this.userRepository = userRepository;
    }

    public Tutor convert(TutorDTO tutorDTO) {
        Tutor tutor = objectMapper.convertValue(tutorDTO, Tutor.class);

        User user = userRepository.findById(tutorDTO.getUserId()).orElseThrow(() -> new InvalidIDException("tutorRegister"));
        tutor.setUser(user);
        if (StringUtils.isBlank(tutor.getName())) {
            tutor.setName(String.format("%s %s", user.getFirstName(), user.getLastName()));
        }

        tutor.setIdentificationCardType(identificationCardTypeRepository.findByName(tutorDTO.getIdentificationCardTypeName()).orElseThrow(() -> new InvalidIdentificationCardTypeException("tutorRegister")));
        return tutor;
    }

    public TutorDTO convert(Tutor tutor) {
        TutorDTO tutorDTO = objectMapper.convertValue(tutor, TutorDTO.class);
        tutorDTO.setIdentificationCardTypeName(tutor.getIdentificationCardType().getName());
        Optional.ofNullable(tutor.getPhotoFile()).ifPresent(documentFile -> tutorDTO.setPhotoId(documentFile.getId()));
        tutorDTO.setCreatedBy(tutor.getCreatedBy());
        tutorDTO.setCreatedDate(tutor.getCreatedDate());
        tutorDTO.setLastModifiedBy(tutor.getLastModifiedBy());
        tutorDTO.setLastModifiedDate(tutor.getLastModifiedDate());

        Optional.ofNullable(tutor.getUser()).ifPresent(user -> tutorDTO.setUserId(user.getId()));

        return tutorDTO;
    }
}
