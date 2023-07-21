package pt.famility.backoffice.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.config.ApplicationProperties;
import pt.famility.backoffice.converter.ChildConverter;
import pt.famility.backoffice.converter.ChildSubscriptionConverter;
import pt.famility.backoffice.domain.Child;
import pt.famility.backoffice.domain.ChildSubscription;
import pt.famility.backoffice.domain.IdentificationCardType;
import pt.famility.backoffice.domain.Organization;
import pt.famility.backoffice.domain.Tutor;
import pt.famility.backoffice.domain.User;
import pt.famility.backoffice.domain.enumeration.NotificationTypeType;
import pt.famility.backoffice.domain.enumeration.OrganizationType;
import pt.famility.backoffice.domain.enumeration.StatusType;
import pt.famility.backoffice.repository.ChildRepository;
import pt.famility.backoffice.repository.ChildSubscriptionRepository;
import pt.famility.backoffice.repository.IdentificationCardTypeRepository;
import pt.famility.backoffice.repository.OrganizationRepository;
import pt.famility.backoffice.repository.TutorRepository;
import pt.famility.backoffice.repository.UserRepository;
import pt.famility.backoffice.service.dto.ChildSubscriptionChildViewDTO;
import pt.famility.backoffice.service.dto.ChildSubscriptionDTO;
import pt.famility.backoffice.service.dto.ChildSubscriptionDetailDTO;
import pt.famility.backoffice.service.dto.ChildSubscriptionRegisterDTO;
import pt.famility.backoffice.service.dto.ChildSubscriptionUpdateDTO;
import pt.famility.backoffice.service.dto.ChildrenImportDTO;
import pt.famility.backoffice.service.dto.ImportDTO;
import pt.famility.backoffice.service.dto.ItineraryStopPointDTO;
import pt.famility.backoffice.service.dto.UserDTO;
import pt.famility.backoffice.web.rest.errors.InvalidIDException;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ChildSubscriptionService {

    private final ChildSubscriptionRepository childSubscriptionRepository;

    private final ChildSubscriptionConverter childSubscriptionConverter;

    private final TutorService tutorService;

    private final ChildRepository childRepository;

    private final TutorRepository tutorRepository;

    private final UserRepository userRepository;

    private final OrganizationRepository organizationRepository;

    private final ItineraryStopPointService itineraryStopPointService;

    private final UserService userService;

    private final IdentificationCardTypeRepository identificationCardTypeRepository;

    private final ApplicationProperties applicationProperties;

    private ChildConverter childConverter;

    private final OrganizationService organizationService;

    private final NotificationSenderService notificationSenderService;

    private final MailService mailService;

    private static final int FIRST_ROW_INDEX = 3;

    private static final int CHILD_INDEX = 0;
    private static final int CHILD_NIF_INDEX = 1;
    private static final int CHILD_DATE_OF_BIRTH_INDEX = 2;
    private static final int ADDITIONAL_INFORMATION_INDEX = 3;
    private static final int CHILD_CARD_NUMBER_INDEX = 4;
    private static final int TUTOR_FIRST_NAME_INDEX = 5;
    private static final int TUTOR_LAST_NAME_INDEX = 6;
    private static final int TUTOR_EMAIL_INDEX = 7;
    private static final int TUTOR_NIF_INDEX = 8;
    private static final int TUTOR_PHONE_INDEX = 9;
    private static final int IDENTIFICATION_CARD_TYPE_ID_INDEX = 10;
    private static final int IDENTIFICATION_CARD_NUMBER_INDEX = 11;
    private static final int TUTOR_CARD_NUMBER_INDEX = 12;


    private static final int A_CHILD_NIF_INDEX = 0;
    private static final int A_AUTHORIZED_NAME_INDEX = 1;
    private static final int A_AUTHORIZED_NIF_INDEX = 2;
    private static final int A_AUTHORIZED_PHONE_INDEX = 3;
    private static final int A_AUTHORIZED_IDENTIFICATION_CARD_TYPE_ID_INDEX = 4;
    private static final int A_AUTHORIZED_IDENTIFICATION_CARD_NUMBER_INDEX = 5;
    private static final int A_AUTHORIZED_CARD_NUMBER_INDEX = 6;

    private final MessageSource messageSource;

    public ChildSubscriptionService(
        ChildSubscriptionRepository childSubscriptionRepository,
        ChildSubscriptionConverter childSubscriptionConverter,
        TutorService tutorService,
        ChildRepository childRepository,
        TutorRepository tutorRepository, UserRepository userRepository, OrganizationRepository organizationRepository, ItineraryStopPointService itineraryStopPointService,
        UserService userService, IdentificationCardTypeRepository identificationCardTypeRepository, ApplicationProperties applicationProperties,
        ChildConverter childConverter,
        OrganizationService organizationService,
        NotificationSenderService notificationSenderService,
        MailService mailService, MessageSource messageSource) {
        this.childSubscriptionRepository = childSubscriptionRepository;
        this.childSubscriptionConverter = childSubscriptionConverter;
        this.tutorService = tutorService;
        this.childRepository = childRepository;
        this.tutorRepository = tutorRepository;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.itineraryStopPointService = itineraryStopPointService;
        this.userService = userService;
        this.identificationCardTypeRepository = identificationCardTypeRepository;
        this.applicationProperties = applicationProperties;
        this.childConverter = childConverter;
        this.organizationService = organizationService;
        this.notificationSenderService = notificationSenderService;
        this.mailService = mailService;
        this.messageSource = messageSource;
    }

    public Page<ChildSubscription> findAllByOrganizationIdAndParams(Long organizationId, StatusType statusType, String childName,
                                                                    Boolean famility, String additionalInformation, Pageable pageable) {

        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setCreatedDate(null);
        organization.setLastModifiedDate(null);

        Child child = new Child();
        child.setName(childName);

        ChildSubscription example = new ChildSubscription();
        example.setOrganization(organization);
        example.setChild(child);
        example.setStatusType(statusType);

        Optional.ofNullable(additionalInformation).ifPresent(presentAdditionalInformation -> {
            example.setAdditionalInformation(presentAdditionalInformation.isEmpty() ? null : presentAdditionalInformation);
        });

        if (famility != null) {
            example.setFamility(famility);
        }

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withMatcher("child.name", genericPropertyMatcher -> genericPropertyMatcher.contains().ignoreCase())
            .withMatcher("additionalInformation", genericPropertyMatcher -> genericPropertyMatcher.contains().ignoreCase());
        return childSubscriptionRepository.findAll(Example.of(example, matcher), pageable);

    }

    @Transactional(readOnly = true)
    public ChildSubscriptionDetailDTO findById(Long id) {
        ChildSubscriptionDetailDTO cSDto = childSubscriptionConverter.convertDetail(findChildSubscriptionById(id));
        if (cSDto.getStatusType().equals(StatusType.PENDING)) {

            Optional<ChildSubscription> childSubscriptionOptional = childSubscriptionRepository.findFirstByStatusTypeAndChildIdAndOrganizationIdAndFamilityFalse(StatusType.ACTIVE,
                cSDto.getChild().getId(),
                cSDto.getOrganization().getId());

            childSubscriptionOptional.ifPresent( cSOpt -> {
                cSDto.setAdditionalInformation(cSOpt.getAdditionalInformation());
                cSDto.setCardNumber(cSOpt.getCardNumber());
            });
        }
        return cSDto;
    }

    @Transactional(readOnly = true)
    public ChildSubscriptionDTO findChildSubscriptionDTOById(Long id) {
        return childSubscriptionConverter.convert(findChildSubscriptionById(id));
    }

    public void updateStatus(Long id, ChildSubscriptionUpdateDTO childSubscriptionUpdateStatusDTO) {
        ChildSubscription childSubscriptionById = findChildSubscriptionById(id);
        childSubscriptionById.setStatusType(childSubscriptionUpdateStatusDTO.getStatusType());
        childSubscriptionById.setComments(childSubscriptionUpdateStatusDTO.getComments());

        if (StatusType.ACTIVE.equals(childSubscriptionUpdateStatusDTO.getStatusType())) {
            childSubscriptionById.setAdditionalInformation(childSubscriptionUpdateStatusDTO.getAdditionalInformation());
            childSubscriptionById.setActivationDate(Instant.now());

            //search for other subscriptions (non famility) and deactivate
            childSubscriptionRepository
                .findFirstByStatusTypeAndChildIdAndOrganizationIdAndFamilityFalse(StatusType.ACTIVE,
                    childSubscriptionById.getChild().getId(),
                    childSubscriptionById.getOrganization().getId())
                .ifPresent(childS -> {
                    log.info("Non famility active subscription with id {} found for child {} and organization {}. It will become inactive and replaced by famility subscription {}.",
                        childS.getId(), childS.getChild().getId(), childS.getOrganization().getId());
                    if (StringUtils.isBlank(childSubscriptionById.getAdditionalInformation())) {
                        childSubscriptionById.setAdditionalInformation(childS.getAdditionalInformation());
                    }

                    childSubscriptionById.setCardNumber(childS.getCardNumber());

                    childS.setStatusType(StatusType.INACTIVE);
                    childS.setDeactivationDate(Instant.now());
                    childSubscriptionRepository.save(childS);

                });
        } else if (StatusType.INACTIVE.equals(childSubscriptionUpdateStatusDTO.getStatusType())) {
            childSubscriptionById.setDeactivationDate(Instant.now());
        }

        if (applicationProperties.getNotifications().isChildSubscriptionServiceToggle() && !StatusType.PENDING.equals(childSubscriptionUpdateStatusDTO.getStatusType())) {
            sendNotificationTutor(childSubscriptionUpdateStatusDTO, childSubscriptionById);
        }

        childSubscriptionRepository.save(childSubscriptionById);
    }

    private void sendNotificationTutor(ChildSubscriptionUpdateDTO childSubscriptionUpdateStatusDTO, ChildSubscription childSubscription) {
        Locale locale = Locale.forLanguageTag(childSubscription.getUser().getLangKey());
        Tutor tutor = tutorRepository.findFirstByUserId((childSubscription.getUser().getId()))
            .orElseThrow( () ->
                new IllegalArgumentException("There is no tutor associated with this child id: "+ childSubscription.getChild().getId()));

        if (tutor.isFamility()) {
            Object[] objects = new Object[]{childSubscription.getChild().getName(), childSubscription.getOrganization().getName()};
            notificationSenderService.sendPushNotificationToTutor(
                tutor.getId(),
                messageSource.getMessage("tutor.subscription.title", null, locale),
                (childSubscriptionUpdateStatusDTO.getStatusType().equals(StatusType.ACTIVE) ?
                    messageSource.getMessage("tutor.subscription.messageA", objects, locale) :
                    messageSource.getMessage("tutor.subscription.messageR", objects, locale)));
        }
    }

    private ChildSubscription findChildSubscriptionById(Long id) {
        Optional<ChildSubscription> optionalChildSubscription = childSubscriptionRepository.findById(id);
        return optionalChildSubscription.orElseThrow(() -> new InvalidIDException("childSubscription"));
    }

    public void create(ChildSubscriptionRegisterDTO childSubscriptionRegisterDTO) {

        List<@NotNull Long> orgIds = Arrays.asList(childSubscriptionRegisterDTO.getOrganizationIds());

        orgIds.forEach(id -> {
            ChildSubscription childSubscription = childSubscriptionConverter.convertRegister(childSubscriptionRegisterDTO.getChildId(), id, childSubscriptionRegisterDTO.getUserId());
            childSubscriptionRepository.save(childSubscription);
        });
    }

    @Transactional(readOnly = true)
    public Page<ChildSubscriptionChildViewDTO> findAllByChildIdAndOrganizationType(Long childId, OrganizationType organizationType, StatusType status, Pageable pageable) {
        return childSubscriptionRepository.listLastStateByChildIdAndOrganizations(childId, organizationType, status, pageable).map(this::convertChildView);
    }

    public ChildSubscriptionChildViewDTO convertChildView(ChildSubscription childSubscription) {
        return childSubscriptionConverter.convertChildView(childSubscription);
    }

    public ChildSubscriptionDTO convert(ChildSubscription childSubscription) {
        return childSubscriptionConverter.convert(childSubscription);
    }

    public void updateTutor(Long id, ChildSubscriptionUpdateDTO childSubscriptionUpdateDTO){
        ChildSubscription childSubscription = findChildSubscriptionById(id);
        Tutor tutor = tutorService.findById(childSubscriptionUpdateDTO.getIdTutor()).orElseThrow(() -> new InvalidIDException("Tutor does not exist"));
        Child child = childSubscription.getChild();

        childSubscription.setUser(tutor.getUser());

        child.setTutors(new HashSet<>());
        child.addTutor(tutor);

        childSubscriptionRepository.save(childSubscription);
        childRepository.save(child);
    }

    public void updateAdditionalInformation(Long id, ChildSubscriptionUpdateDTO childSubscriptionUpdateDTO){
        ChildSubscription childSubscription = findChildSubscriptionById(id);
        childSubscription.setAdditionalInformation(childSubscriptionUpdateDTO.getAdditionalInformation());
        childSubscriptionRepository.save(childSubscription);
    }

    public void updateCardNumber(Long id, ChildSubscriptionUpdateDTO childSubscriptionUpdateDTO){
        ChildSubscription childSubscription = findChildSubscriptionById(id);
        childSubscription.setCardNumber(childSubscriptionUpdateDTO.getCardNumber());
        childSubscriptionRepository.save(childSubscription);
    }

    public List<ChildSubscriptionChildViewDTO> findAllActiveByChild(Long childId) {
        List<ChildSubscriptionChildViewDTO> result = new ArrayList<>();

        List<ChildSubscription> activeByChildList = childSubscriptionRepository.listActiveByChild(childId);
        activeByChildList.forEach(childSubscription -> result.add(convertChildView(childSubscription)));

        result.forEach(childSubscriptionChildViewDTO -> {
            if (OrganizationType.BUS_COMPANY.equals(childSubscriptionChildViewDTO.getOrganization().getOrganizationType())) {
                List<ItineraryStopPointDTO> content = itineraryStopPointService.listAllItineraryStopPoint(childSubscriptionChildViewDTO.getChildId(), childSubscriptionChildViewDTO.getOrganization().getId(), PageRequest.of(0, applicationProperties.getItineraryStopPointsByChild())).getContent();
                childSubscriptionChildViewDTO.setItineraryStopPoints(content);
            }

        });

        return result;
    }

    /**
     * Deactivate all subscriptions from an organization when this organization is disabled
     * @param organizationId
     */
    @Transactional
    public void deactivateAllByOrganizationId(Long organizationId) {
        List<StatusType> statusTypes = Arrays.asList(StatusType.ACTIVE, StatusType.PENDING);
        ChildSubscriptionUpdateDTO childSubscriptionUpdateDTO = new ChildSubscriptionUpdateDTO();
        childSubscriptionUpdateDTO.setStatusType(StatusType.INACTIVE);
        childSubscriptionRepository.findAllByOrganizationIdAndStatusTypeIn(organizationId, statusTypes).forEach(childSubscription ->
            updateStatus(childSubscription.getId(), childSubscriptionUpdateDTO)
        );
    }

    public List<Long> getOrganizationIdsWithActiveSubscriptionsByChildId(Long childId) {
        List<ChildSubscription> childSubscriptionList = childSubscriptionRepository.findAllByChildIdAndStatusType(childId, StatusType.ACTIVE);
        return childSubscriptionList.stream().map(childSubscription -> childSubscription.getOrganization().getId()).collect(Collectors.toList());
    }

    public List<Long> getOrganizationIdsWithSubscriptionsByChildId(Long childId) {
        List<ChildSubscription> childSubscriptionList = childSubscriptionRepository.findAllByChildId(childId);
        return childSubscriptionList.stream().map(childSubscription -> childSubscription.getOrganization().getId()).collect(Collectors.toList());
    }

    public Long getOrganizationIdByChildSubscriptionId(Long childSubscriptionId) {
        return childSubscriptionRepository.findById(childSubscriptionId).map(childSubscription -> childSubscription.getOrganization().getId()).orElse(-1L);
    }

    public List<ChildSubscription> getActiveChildSubscriptionByChildIdAndOrganizationId(Long childId, Long organizationId) {
        return childSubscriptionRepository.findAllByStatusTypeAndChildIdAndOrganizationId(StatusType.ACTIVE, childId, organizationId);
    }

    public List<Long> getUserIdsWithActiveSubscriptionsToOrganizationByOrganizationId(Long organizationId) {
        List<StatusType> statusTypes = Collections.singletonList(StatusType.ACTIVE);
        return childSubscriptionRepository.findAllByOrganizationIdAndStatusTypeIn(organizationId, statusTypes)
            .stream().map(childSubscription -> childSubscription.getUser().getId()).collect(Collectors.toList());
    }

    public InputStream download() throws IOException {
        Resource resource = new ClassPathResource("templates/import_template.xlsx");
        return resource.getInputStream();
    }

    public ImportDTO upload(InputStream inputStream) throws IOException {
        return processImportFile(inputStream);
    }

    private ImportDTO processImportFile(InputStream inputStream) throws IOException {
        ImportDTO importDTO = new ImportDTO();
        UserDTO userInfo = userService.getUserInfo();
        log.info("Starting children import routine for organization {}", userInfo.getOrganizationId());
        Workbook workbook = new XSSFWorkbook(inputStream);

        Sheet childrenSheet = workbook.getSheetAt(0);
        ChildrenImportDTO childrenImportDTO;
        int createdChildrenNumber = 0;
        int updatedChildrenNumber = 0;

        for (int i = FIRST_ROW_INDEX; isRowValid(childrenSheet, i, CHILD_INDEX); i++) {
            Row currentRow = childrenSheet.getRow(i);
            if (currentRow.getPhysicalNumberOfCells() >= IDENTIFICATION_CARD_NUMBER_INDEX + 1) {
                childrenImportDTO = parseChildRow(currentRow, userInfo);
                if (childrenImportDTO.getCreatedChildrenNumber() == 1) {
                    createdChildrenNumber++;
                } else if (childrenImportDTO.getUpdatedChildrenNumber() == 1) {
                    updatedChildrenNumber++;
                }
                if (childrenImportDTO.getSuccess() != null && !childrenImportDTO.getSuccess()) {
                    log.warn("Import failed for organization {}: {}", userInfo.getOrganizationId(), childrenImportDTO);
                    importDTO.setChildrenImportDTO(childrenImportDTO);
                    return importDTO;
                }
            }
        }
        childrenImportDTO = new ChildrenImportDTO(true, createdChildrenNumber, updatedChildrenNumber, null, null);
        log.info("Imported children successfully for organization {}: {}", userInfo.getOrganizationId(), childrenImportDTO);
        importDTO.setChildrenImportDTO(childrenImportDTO);

        return importDTO;

    }

    private boolean isRowValid(Sheet sheet, int i, int cellIndex) {
        return i <= sheet.getLastRowNum() && sheet.getRow(i) != null && sheet.getRow(i).getCell(cellIndex) != null;
    }

    private Boolean checkIfChildExists(Long organizationId, Row row) {
        Iterator<Cell> cellIterator = row.iterator();

        while (cellIterator.hasNext()) {
            Cell currentCell = cellIterator.next();
            if (currentCell.getColumnIndex() == CHILD_NIF_INDEX) {
                String childNif = getStringValue(currentCell);
                Optional<ChildSubscription> childOpt =
                    childSubscriptionRepository.findFirstByOrganizationIdAndChildNifNumberAndChildFamilityAndFamility(
                        organizationId, childNif, Boolean.FALSE, Boolean.FALSE);
                return childOpt.isPresent();
            }
        }
        return false;
    }

    private String getStringValue(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC: return String.valueOf(Double.valueOf(cell.getNumericCellValue()).longValue());
            default: return cell.getStringCellValue();
        }
    }

    private Long getLongValue(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC: return Double.valueOf(cell.getNumericCellValue()).longValue();
            default: return Long.valueOf(cell.getStringCellValue());
        }
    }

    private LocalDate getLocalDateValue(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC: return DateUtil.getLocalDateTime(cell.getNumericCellValue()).toLocalDate();
            default:
                String value = cell.getStringCellValue();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return LocalDate.parse(value, dtf);
        }
    }

    private ChildrenImportDTO parseChildRow(Row row, UserDTO userInfo) {
        Iterator<Cell> cellIterator = row.iterator();
        String childName = "";
        String childNif = "";
        LocalDate childDateOfBirth = null;
        String additionalInformation = "";
        String tutorFirstName = "";
        String tutorLastName = "";
        String tutorEmail = "";
        String tutorNif = "";
        String tutorPhone = "";
        long identificationCardTypeId = 0L;
        String identificationCardNumber = "";
        String cardNumber = "";
        String tutorCardNumber = "";
        boolean created;

        while (cellIterator.hasNext()) {
            Cell currentCell = cellIterator.next();
            try {
                switch (currentCell.getColumnIndex()) {
                    case CHILD_INDEX:
                        childName = getStringValue(currentCell);
                        if (childName.isEmpty()) {
                            throw new Exception("Child name is mandatory");
                        }
                        break;
                    case CHILD_NIF_INDEX:
                        childNif = getStringValue(currentCell);
                        if (childNif.isEmpty()) {
                            throw new Exception("Child nif number is mandatory");
                        }
                        break;
                    case CHILD_DATE_OF_BIRTH_INDEX:
                        childDateOfBirth = getLocalDateValue(currentCell);
                        break;
                    case ADDITIONAL_INFORMATION_INDEX:
                        additionalInformation = getStringValue(currentCell);
                        break;
                    case TUTOR_FIRST_NAME_INDEX:
                        tutorFirstName = getStringValue(currentCell);
                        if (tutorFirstName.isEmpty()) {
                            throw new Exception("Tutor first name is mandatory");
                        }
                        break;
                    case TUTOR_LAST_NAME_INDEX:
                        tutorLastName = getStringValue(currentCell);
                        if (tutorLastName.isEmpty()) {
                            throw new Exception("Tutor last name is mandatory");
                        }
                        break;
                    case TUTOR_EMAIL_INDEX:
                        tutorEmail = getStringValue(currentCell);
                        if (tutorEmail.isEmpty()) {
                            throw new Exception("Tutor email is mandatory");
                        }
                        break;
                    case TUTOR_NIF_INDEX:
                        tutorNif = getStringValue(currentCell);
                        if (tutorNif.isEmpty()) {
                            throw new Exception("Tutor nif number is mandatory");
                        }
                        break;
                    case TUTOR_PHONE_INDEX:
                        tutorPhone = getStringValue(currentCell);
                        if (tutorPhone.isEmpty()) {
                            throw new Exception("Tutor phone number is mandatory");
                        }
                        break;
                    case IDENTIFICATION_CARD_TYPE_ID_INDEX:
                        identificationCardTypeId = getLongValue(currentCell);
                        break;
                    case IDENTIFICATION_CARD_NUMBER_INDEX:
                        identificationCardNumber = getStringValue(currentCell);
                        if (identificationCardNumber.isEmpty()) {
                            throw new Exception("Tutor identification card number is mandatory");
                        }
                        break;
                    case CHILD_CARD_NUMBER_INDEX:
                        cardNumber = getStringValue(currentCell);
                        break;
                    case TUTOR_CARD_NUMBER_INDEX:
                        tutorCardNumber = getStringValue(currentCell);
                        break;
                }
            } catch (Exception e) {
                ChildrenImportDTO childrenImport = new ChildrenImportDTO(false, 0, 0, row.getRowNum() + 1, CellReference.convertNumToColString(currentCell.getColumnIndex()));
                log.warn(String.format("Error parsing child row. Organization: {} ChildrenImport: {}", userInfo.getOrganizationId(), childrenImport), e);
                return childrenImport;
            }
        }
        try {
            created = saveParsedChildRow(
                userInfo, childName, childNif, childDateOfBirth, additionalInformation, tutorFirstName, tutorLastName, tutorEmail, tutorNif, tutorPhone, identificationCardTypeId, identificationCardNumber, cardNumber, tutorCardNumber
            );
        } catch (Exception e) {
            ChildrenImportDTO childrenImport = new ChildrenImportDTO(false, 0, 0, row.getRowNum() + 1, null);
            log.warn(String.format("Error saving child row. Organization: {} ChildrenImport: {} Message: {}", userInfo.getOrganizationId()), childrenImport, e);
            return childrenImport;
        }
        return new ChildrenImportDTO(null, created ? 1 : 0, created ? 0 : 1, null, null);
    }

    private boolean saveParsedChildRow(
        UserDTO userInfo, String childName, String childNif, LocalDate childDateOfBirth, String additionalInformation,
        String tutorFirstName, String tutorLastName, String tutorEmail, String tutorNif, String tutorPhone, long identificationCardTypeId, String identificationCardNumber, String cardNumber, String tutorCardNumber) {

        ChildSubscription childSubscription = parseChildSubscription(userInfo, childNif, additionalInformation, cardNumber);
        Child child = parseChild(childSubscription, childName, childNif, childDateOfBirth);
        childSubscription.setChild(child);
        Tutor tutor = parseTutor(userInfo, childSubscription, tutorFirstName, tutorLastName, tutorEmail, tutorNif, tutorPhone, identificationCardTypeId, identificationCardNumber);
        childSubscription.setUser(tutor.getUser());

        boolean created = child.getId() == null;
        childSubscriptionRepository.save(childSubscription);

        return created;
    }

    private ChildSubscription parseChildSubscription(UserDTO userInfo, String childNif, String additionalInformation, String cardNumber) {
        Optional<ChildSubscription> childSubscriptionOpt = childSubscriptionRepository
            .findByOrganizationIdAndChildNifCountryAndChildNifNumberAndStatusType(userInfo.getOrganizationId(), "PT", childNif, StatusType.ACTIVE);
        Optional<Organization> organizationOpt = organizationRepository.findById(userInfo.getOrganizationId());

        if (!childSubscriptionOpt.isPresent()) {
            ChildSubscription newChildSubscription = new ChildSubscription();
            newChildSubscription.setStatusType(StatusType.ACTIVE);
            newChildSubscription.setFamility(Boolean.FALSE);
            newChildSubscription.setSubscriptionDate(Instant.now());
            newChildSubscription.setActivationDate(Instant.now());
            newChildSubscription.setOrganization(organizationOpt.get());
            childSubscriptionOpt = Optional.of(newChildSubscription);
        }

        //REVER - FMLTY-950 Se a criança já existir, do tipo Famility e existir uma adesão não famility, deve ser
        // desativada e criada uma associada à criança Famility

//        if (childSubscriptionOpt.get().getId() != null && !childSubscriptionOpt.get().isFamility()) {
//            childSubscriptionOpt.get().setStatusType(StatusType.INACTIVE);
//            childSubscriptionRepository.save(childSubscriptionOpt.get());
//            ChildSubscription newChildSubscription = new ChildSubscription();
//            newChildSubscription.setStatusType(StatusType.ACTIVE);
//            newChildSubscription.setFamility(Boolean.TRUE);
//            newChildSubscription.setSubscriptionDate(Instant.now());
//            newChildSubscription.setActivationDate(Instant.now());
//            newChildSubscription.setOrganization(organizationOpt.get());
//            childSubscriptionOpt = Optional.of(newChildSubscription);
//        }
        ChildSubscription childSubscription = childSubscriptionOpt.get();
        childSubscription.setAdditionalInformation(additionalInformation);
        childSubscription.setCardNumber(cardNumber.isEmpty() ? null : cardNumber);
        return childSubscription;
    }

    private Child parseChild(ChildSubscription childSubscription, String childName, String childNif, LocalDate childDateOfBirth) {
        Child child = childSubscription.getChild();
        if (child == null) {
            Optional<ChildSubscription> childSubscriptionOpt = childSubscriptionRepository
                .findFirstByOrganizationIdAndChildNifNumber(childSubscription.getOrganization().getId(), childNif);
            if (childSubscriptionOpt.isPresent()) {
                child = childSubscriptionOpt.get().getChild();
            } else {
                Child newChild = new Child();
                newChild.setNifCountry("PT");
                newChild.setFamility(Boolean.FALSE);
                newChild.setStatusType(StatusType.ACTIVE);
                child = newChild;
            }
        }
        if (!child.isFamility()) {
            child.setName(childName);
            child.setNifNumber(childNif);
            child.setDateOfBirth(childDateOfBirth);
        }
        return child;
    }

    private Tutor parseTutor(UserDTO userInfo, ChildSubscription childSubscription, String tutorFirstName, String tutorLastName, String tutorEmail, String tutorNif, String tutorPhone, Long identificationCardTypeId, String identificationCardNumber) {
        User user = childSubscription.getUser();
        Tutor tutor;
        IdentificationCardType identificationCardType = identificationCardTypeRepository.findById(identificationCardTypeId).get();

        if (user == null) {
            Optional<User> userOpt = userRepository.findOneByLogin(tutorEmail);
            if (userOpt.isPresent()) {
                tutor = tutorRepository.findByUserId(userOpt.get().getId()).get();
            } else {
                User newUser = new User();
                newUser.setFirstName(tutorFirstName);
                newUser.setLastName(tutorLastName);
                newUser.setLogin(tutorEmail);
                newUser.setEmail(tutorEmail);
                newUser.setActivated(Boolean.TRUE);
                newUser.setLangKey("en");
                newUser.setOrganizationId(userInfo.getOrganizationId());
                user = newUser;

                Tutor newTutor = new Tutor();
                newTutor.setNifCountry("PT");
                newTutor.setFamility(Boolean.FALSE);
                newTutor.setStatusType(StatusType.ACTIVE);
                newTutor.setUser(user);
                tutor = newTutor;
            }
        } else {
            tutor = tutorRepository.findByUserId(user.getId()).get();
        }
        if (!tutor.isFamility()) {
            tutor.setName(tutorFirstName + " " + tutorLastName);
            tutor.setNifNumber(tutorNif);
            tutor.setPhoneNumber(tutorPhone);
            tutor.setIdentificationCardType(identificationCardType);
            tutor.setIdentificationCardNumber(identificationCardNumber);
            tutor.addChild(childSubscription.getChild());
        }
        return tutor;
    }

    public ChildSubscriptionDTO getChildSubscriptionByCardNumber(Long organizationId, String cardNumber) {
        return childSubscriptionRepository.findFirstByOrganizationIdAndCardNumberAndStatusType(organizationId, cardNumber, StatusType.ACTIVE)
            .map(this::convert).orElse(null);
    }

    public void updateChildSubscriptionByTutor(Long childId) {
        childSubscriptionRepository.findAllByChildIdAndStatusTypeIn(childId,
                new ArrayList<>(Arrays.asList(StatusType.ACTIVE, StatusType.PENDING)))
            .forEach( cS -> {
                cS.setStatusType(StatusType.INACTIVE);
                cS.setDeactivationDate(Instant.now());

                ChildSubscription newSubscription = new ChildSubscription();

                newSubscription.setCardNumber(cS.getCardNumber());
                newSubscription.setStatusType(StatusType.PENDING);
                newSubscription.setSubscriptionDate(Instant.now());
                newSubscription.setAdditionalInformation(cS.getAdditionalInformation());
                newSubscription.setComments(cS.getComments());
                newSubscription.setChild(cS.getChild());
                newSubscription.setOrganization(cS.getOrganization());
                newSubscription.setFamility(cS.isFamility());
                newSubscription.setUser(cS.getUser());

                childSubscriptionRepository.save(cS);
                childSubscriptionRepository.save(newSubscription);
                });

    }

    public Page<ChildSubscriptionDTO> findAllByOrganizationIdAndStatusType(Long id, StatusType status, String childName, Boolean familityOnly, String additionalInformation, Pageable pageable, Boolean famility) {
        if (familityOnly != null) {
            famility = familityOnly ? familityOnly : null;
        }

        Page<ChildSubscription> page = findAllByOrganizationIdAndParams(id, status, childName, famility, additionalInformation, pageable);
        Page<ChildSubscription> pageNonFamility = findAllByOrganizationIdAndParams(id, StatusType.ACTIVE, childName, false, additionalInformation, pageable);
        List<ChildSubscriptionDTO> childSubscriptionDTOList = new ArrayList<>();
        page.getContent().forEach(childSubscription -> childSubscriptionDTOList.add(convert(childSubscription)));

        pageNonFamility.getContent().forEach(childSubscription -> childSubscriptionDTOList
            .stream()
            .filter((dto -> dto.getChildId().equals(childSubscription.getChild().getId())))
            .findFirst().ifPresent(result -> {
                result.setAdditionalInformation(childSubscription.getAdditionalInformation());
                result.setCardNumber(childSubscription.getCardNumber());
            }));

        Page<ChildSubscriptionDTO> pageResult = new PageImpl<>(childSubscriptionDTOList, pageable, page.getTotalElements());

        return pageResult;
    }
}

