package pt.famility.backoffice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.converter.TutorConverter;
import pt.famility.backoffice.domain.DocumentFile;
import pt.famility.backoffice.domain.IdentificationCardType;
import pt.famility.backoffice.domain.Tutor;
import pt.famility.backoffice.domain.User;
import pt.famility.backoffice.domain.enumeration.StatusType;
import pt.famility.backoffice.domain.enumeration.UserType;
import pt.famility.backoffice.repository.IdentificationCardTypeRepository;
import pt.famility.backoffice.repository.TutorRepository;
import pt.famility.backoffice.repository.UserRepository;
import pt.famility.backoffice.service.dto.OrganizationDTO;
import pt.famility.backoffice.service.dto.PhotoDTO;
import pt.famility.backoffice.service.dto.TutorDTO;
import pt.famility.backoffice.service.dto.TutorRegistrationByOrganizationDTO;
import pt.famility.backoffice.service.dto.TutorUpdateDTO;
import pt.famility.backoffice.service.dto.UserDTO;
import pt.famility.backoffice.service.dto.UserUpdateStatusDTO;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.errors.InternalServerErrorException;
import pt.famility.backoffice.web.rest.errors.InvalidIDException;

import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service Implementation for managing {@link Tutor}.
 */
@Slf4j
@Service
@Transactional
public class TutorService {

    private final TutorRepository tutorRepository;

    private final TutorConverter tutorConverter;

    private final UserRepository userRepository;

    private final PhotoService photoService;

    private final UserService userService;

    private final OrganizationService organizationService;

    private final IdentificationCardTypeRepository identificationCardTypeRepository;

    public TutorService(
            TutorRepository tutorRepository,
            TutorConverter tutorConverter,
            UserRepository userRepository,
            PhotoService photoService,
            UserService userService,
            OrganizationService organizationService,
            IdentificationCardTypeRepository identificationCardTypeRepository
    ) {
        this.tutorRepository = tutorRepository;
        this.tutorConverter = tutorConverter;
        this.userRepository = userRepository;
        this.photoService = photoService;
        this.userService = userService;
        this.organizationService = organizationService;
        this.identificationCardTypeRepository = identificationCardTypeRepository;
    }

    public TutorDTO save(TutorDTO tutorDTO) throws IllegalArgumentException {
        Tutor tutor = tutorConverter.convert(tutorDTO);
        Tutor savedTutor = tutorRepository.save(tutor);

        Optional.ofNullable(tutorDTO.getPhoto()).ifPresent(photo -> {
            try {
                updatePhoto(savedTutor, new PhotoDTO(photo));
            } catch (IOException e) {
                log.error("Error update tutor photo: {}", e);
                throw new InternalServerErrorException(e.getMessage());
            }
        });

        tutorDTO.setId(savedTutor.getId());
        Optional.ofNullable(savedTutor.getPhotoFile()).ifPresent(documentFile -> tutorDTO.setPhotoId(documentFile.getId()));
        return tutorDTO;
    }

    public void deleteById(Long id) {
        tutorRepository.deleteById(id);
    }

    public TutorDTO findTutorDTOByUserId(Long id) {
        Optional<Tutor> tutorOptional = getByUserId(id);

        return tutorOptional.map(tutor -> tutorConverter.convert(tutor)).orElseGet(() -> {
            Optional<User> user = userRepository.findById(id);
            return user.map(user1 -> {
                TutorDTO tutorDTO = new TutorDTO();
                tutorDTO.setName(String.format("%s %s", user1.getFirstName(), user1.getLastName()));
                return tutorDTO;
            }).orElseThrow(() -> new InvalidIDException("tutorRegister"));
        });
    }

    public Optional<Tutor> getByUserId(Long id) {
        return tutorRepository.findByUserId(id);
    }

    public PhotoDTO getPhotoByTutorAndPhotoFileId(Long id, Long photoFileId) {
        return tutorRepository
                .findByIdAndPhotoFileId(id, photoFileId)
                .map(tutor -> new PhotoDTO(Base64.getEncoder().encodeToString(tutor.getPhotoFile().getFileContent())))
                .orElseThrow(() -> new InvalidIDException("tutorPhoto"));
    }

    public TutorDTO updatePhoto(Long id, PhotoDTO photoDTO) throws IOException {
        Tutor tutor = tutorRepository.findById(id).orElseThrow(() -> new InvalidIDException("tutorPhoto"));
        return convert(updatePhoto(tutor, photoDTO));
    }

    public Tutor updatePhoto(Tutor tutor, PhotoDTO photoDTO) throws IOException {
        DocumentFile oldPhoto = tutor.getPhotoFile();
        DocumentFile newPhoto = photoService.savePhoto(photoDTO.getPhoto());
        tutor.setPhotoFile(newPhoto);
        tutorRepository.save(tutor);
        return tutor;
    }

    public boolean isTutor(Long userId) {
        return tutorRepository.findFirstByUserId(userId).isPresent();
    }

    public List<Tutor> getAllByUserId(Long userId) {
        return tutorRepository.findAllByUserId(userId);
    }

    public Page<Tutor> getTutorsByFilter(String name, StatusType statusType, Boolean isFamility, Long idOrganization, Boolean userActivated, Pageable pageable) {
        User user = new User();

        user.setOrganizationId(idOrganization);

        Tutor tutorExample = new Tutor().name(name).statusType(statusType).famility(isFamility).user(user);

        ExampleMatcher matcherTutor = ExampleMatcher.matching()
                .withMatcher("name", genericPropertyMatcher -> genericPropertyMatcher.contains().ignoreCase())
                .withMatcher("user.organizationId", ExampleMatcher.GenericPropertyMatcher::exact)
                .withIgnorePaths("createdDate", "lastModifiedDate", "user.createdDate", "user.lastModifiedDate", "user.activated");

        return tutorRepository.findAll(Example.of(tutorExample, matcherTutor), pageable);
    }

    public TutorDTO convert(Tutor tutor) {
        return tutorConverter.convert(tutor);
    }

    public Tutor update(Long id, TutorUpdateDTO tutorUpdateDTO) {
        Tutor tutor = tutorRepository.findById(id).orElseThrow(() -> new InvalidIDException("tutorUpdateStatus"));
        Optional.ofNullable(tutorUpdateDTO.getStatus()).ifPresent(statusType -> tutor.setStatusType(statusType));
        Optional.ofNullable(tutorUpdateDTO.getPhoneNumber()).ifPresent(phoneNumber -> tutor.setPhoneNumber(phoneNumber));
        tutorRepository.save(tutor);

        Optional.ofNullable(tutorUpdateDTO.getPhoto()).ifPresent(photo -> {
            try {
                updatePhoto(tutor, new PhotoDTO(photo));
            } catch (IOException e) {
                log.error("Error update tutor photo: {}", e);
                throw new InternalServerErrorException(e.getMessage());
            }
        });

        return tutor;
    }

    public Tutor updateTutor(Tutor tutor) {
        return tutorRepository.save(tutor);
    }

    public TutorDTO updateTutor(Long id, TutorUpdateDTO tutorUpdateDTO) {
        Tutor tutor = findById(id).orElseThrow(() -> new InvalidIDException("tutorPhoto"));
        AtomicReference<Boolean> update = new AtomicReference<>(false);

        Optional.ofNullable(tutorUpdateDTO.getPhoneNumber()).ifPresent(phoneNumber -> {
            tutor.setPhoneNumber(phoneNumber);
            update.set(true);
        });

        String[] names = tutor.getName().split(" ");
        Optional<User> user = userRepository.findOneWithAuthoritiesById(tutor.getUser().getId());

        if (tutorUpdateDTO.getFirstName() != null && tutorUpdateDTO.getLastName() != null) {

            String name = tutorUpdateDTO.getFirstName() + " " + tutorUpdateDTO.getLastName();

            //Set name of tutor
            if (tutorUpdateDTO.getFirstName() != null && tutorUpdateDTO.getLastName() == null) {
                name = tutorUpdateDTO.getFirstName() + " " + names[names.length - 1];
            } else if (tutorUpdateDTO.getLastName() != null && tutorUpdateDTO.getFirstName() == null) {
                name = names[0] + " " + tutorUpdateDTO.getLastName();
            }

            tutor.setName(name);
            String finalName = name;

            //Set name on firebase
            user.ifPresent(uFirebase -> {
                userService.updateNameFirebase(uFirebase.getUid(), finalName);
            });

            //Set first name of user
            Optional.ofNullable(tutorUpdateDTO.getFirstName()).ifPresent(firstName -> {
                user.ifPresent(u -> {
                    u.setFirstName(firstName);
                });
                update.set(true);
            });

            //Set last name of user
            Optional.ofNullable(tutorUpdateDTO.getLastName()).ifPresent(lastName -> {
                user.ifPresent(u -> {
                    u.setLastName(lastName);
                });
                update.set(true);
            });
        }

        Optional.ofNullable(tutorUpdateDTO.getIdentificationCardNumber()).ifPresent(identificationCardNumber -> {
            tutor.setIdentificationCardNumber(identificationCardNumber);
            update.set(true);
        });

        Optional.ofNullable(tutorUpdateDTO.getIdentificationCardTypeName()).ifPresent(identificationCardType -> {
            Optional<IdentificationCardType> idType = identificationCardTypeRepository.findByName(identificationCardType);
            idType.ifPresent(idCardType -> {
                tutor.setIdentificationCardType(idCardType);
                update.set(true);
            });
        });

        if (update.get()) {
            updateTutor(tutor);
        }

        Optional.ofNullable(tutorUpdateDTO.getPhoto()).ifPresent(photo -> {
            try {
                updatePhoto(tutor, new PhotoDTO(photo));
            } catch (IOException e) {
                log.error("Error update tutor photo: {}", e);
            }
        });

        Optional.ofNullable(tutorUpdateDTO.getStatus()).ifPresent(statusType -> {
            UserUpdateStatusDTO userUpdateStatusDTO = new UserUpdateStatusDTO();
            userUpdateStatusDTO.setActivated(StatusType.ACTIVE.equals(statusType));
            userService.updateUserStatus(tutor.getUser().getId(), userUpdateStatusDTO);
        });

        return convert(tutor);
    }

    public Optional<Tutor> findById(Long id) {
        return tutorRepository.findById(id);
    }

    public TutorDTO createTutorByOrganization(Long id, TutorRegistrationByOrganizationDTO tutorRegistrationByOrganizationDTO) {

        OrganizationDTO organizationDTO = organizationService.convert(organizationService.findById(id));

        User userSaved = null;

        Optional<User> optinalUserByEmail = userService.loadUserByEmail(tutorRegistrationByOrganizationDTO.getEmail());

        if (optinalUserByEmail.isPresent()) {
            userSaved = optinalUserByEmail.get();

            Optional<Tutor> optionalTutor = getByUserId(userSaved.getId());

            // Protection for not using email if used for famility tutor or in the system
            if (optionalTutor.isPresent()) {
                Tutor tutor = optionalTutor.get();

                if (tutor.isFamility()) {
                    throw new BadRequestAlertException("Email already in use for famility user", "user", "email");
                }
            } else {
                throw new BadRequestAlertException("Email already in use", "user", "email");
            }

        } else {
            UserDTO userDTO = new UserDTO();
            userDTO.setFirstName(tutorRegistrationByOrganizationDTO.getFirstName());
            userDTO.setLastName(tutorRegistrationByOrganizationDTO.getLastName());
            userDTO.setEmail(tutorRegistrationByOrganizationDTO.getEmail());
            userDTO.setLogin(tutorRegistrationByOrganizationDTO.getEmail());
            userDTO.setOrganization(organizationDTO);
            userDTO.setOrganizationId(id);

            HashSet<String> authorities = new HashSet<>();
            authorities.add(UserType.TUTOR.name());
            userDTO.setAuthorities(authorities);

            userSaved = userService.createUser(userDTO);
        }

        TutorDTO tutorDTO = new TutorDTO();
        tutorDTO.setUserId(userSaved.getId());
        tutorDTO.setName(tutorRegistrationByOrganizationDTO.getFirstName() + " " + tutorRegistrationByOrganizationDTO.getLastName());
        tutorDTO.setNifCountry(tutorRegistrationByOrganizationDTO.getNifCountry());
        tutorDTO.setNifNumber(tutorRegistrationByOrganizationDTO.getNifNumber());
        tutorDTO.setIdentificationCardTypeName(tutorRegistrationByOrganizationDTO.getIdentificationCardTypeName());
        tutorDTO.setIdentificationCardNumber(tutorRegistrationByOrganizationDTO.getIdentificationCardNumber());
        tutorDTO.setPhoneNumber(tutorRegistrationByOrganizationDTO.getPhoneNumber());
        tutorDTO.setFamility(false);
        tutorDTO.setStatusType(StatusType.ACTIVE);
        tutorDTO.setPhoto(tutorRegistrationByOrganizationDTO.getPhoto());

        TutorDTO tutorDTOSaved = save(tutorDTO);

        return tutorDTOSaved;
    }

    public Long getOrganizationIdByTutorId(Long tutorId) {
        return tutorRepository
                .findById(tutorId)
                .map(tutor -> tutor.getUser().getOrganizationId())
                .orElse(-1L);
    }
}
