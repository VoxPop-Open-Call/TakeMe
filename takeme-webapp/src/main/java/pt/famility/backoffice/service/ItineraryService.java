package pt.famility.backoffice.service;

import com.google.maps.model.LatLng;
import com.google.openlocationcode.OpenLocationCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.config.ApplicationProperties;
import pt.famility.backoffice.converter.ItineraryConverter;
import pt.famility.backoffice.converter.ItineraryStopPointConverter;
import pt.famility.backoffice.converter.StopPointConverter;
import pt.famility.backoffice.domain.Child;
import pt.famility.backoffice.domain.Itinerary;
import pt.famility.backoffice.domain.ItineraryStopPoint;
import pt.famility.backoffice.domain.Location;
import pt.famility.backoffice.domain.StopAuditEvent;
import pt.famility.backoffice.domain.StopPoint;
import pt.famility.backoffice.domain.Tutor;
import pt.famility.backoffice.domain.enumeration.ItineraryStatusType;
import pt.famility.backoffice.domain.enumeration.LocationType;
import pt.famility.backoffice.domain.enumeration.StopEventType;
import pt.famility.backoffice.domain.enumeration.StopPointType;
import pt.famility.backoffice.repository.ItineraryRepository;
import pt.famility.backoffice.repository.ItineraryStopPointRepository;
import pt.famility.backoffice.repository.LocationRepository;
import pt.famility.backoffice.repository.StopAuditEventRepository;
import pt.famility.backoffice.repository.StopPointRepository;
import pt.famility.backoffice.service.dto.ChildStopPointEventDTO;
import pt.famility.backoffice.service.dto.ItineraryDTO;
import pt.famility.backoffice.service.dto.ItineraryStatusTypePatchDTO;
import pt.famility.backoffice.service.dto.ItineraryStopPointDTO;
import pt.famility.backoffice.service.dto.LocationDTO;
import pt.famility.backoffice.service.dto.StopPointDTO;
import pt.famility.backoffice.service.dto.geolocation.ItineraryDirections;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.errors.NotFoundAlertException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Itinerary}.
 */
@Service
@Transactional
public class ItineraryService {

    private static final Logger log = LoggerFactory.getLogger(ItineraryService.class);

    private final ItineraryRepository itineraryRepository;

    private final ItineraryStopPointConverter itineraryStopPointConverter;

    private final ItineraryStopPointRepository itineraryStopPointRepository;

    private final ItineraryConverter itineraryConverter;

    private final ChildService childService;

    private final StopPointRepository stopPointRepository;

    private final StopAuditEventRepository stopAuditEventRepository;

    private final LocationService locationService;

    private final StopPointConverter stopPointConverter;

    private final DriverService driverService;

    private final VehicleService vehicleService;

    private final OrganizationService organizationService;

    private final NotificationSenderService notificationSenderService;

    private final GoogleMapsService googleMapsService;

    private final LocationRepository locationRepository;

    private final ApplicationProperties applicationProperties;

    public ItineraryService(
        ItineraryRepository itineraryRepository,
        ItineraryStopPointConverter itineraryStopPointConverter,
        ItineraryStopPointRepository itineraryStopPointRepository,
        ItineraryConverter itineraryConverter,
        ChildService childService,
        StopPointRepository stopPointRepository,
        StopAuditEventRepository stopAuditEventRepository,
        LocationService locationService,
        StopPointConverter stopPointConverter,
        DriverService driverService,
        VehicleService vehicleService,
        OrganizationService organizationService,
        NotificationSenderService notificationSenderService,
        GoogleMapsService googleMapsService,
        LocationRepository locationRepository,
        ApplicationProperties applicationProperties
    ) {
        this.itineraryRepository = itineraryRepository;
        this.itineraryStopPointConverter = itineraryStopPointConverter;
        this.itineraryStopPointRepository = itineraryStopPointRepository;
        this.itineraryConverter = itineraryConverter;
        this.childService = childService;
        this.stopPointRepository = stopPointRepository;
        this.stopAuditEventRepository = stopAuditEventRepository;
        this.locationService = locationService;
        this.stopPointConverter = stopPointConverter;
        this.driverService = driverService;
        this.vehicleService = vehicleService;
        this.organizationService = organizationService;
        this.notificationSenderService = notificationSenderService;
        this.googleMapsService = googleMapsService;
        this.locationRepository = locationRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Function that will update an itinerary in the system
     *
     * @param itineraryDTO itineray object with all the changes required
     * @return itinerary with full detail
     */
    public ItineraryDTO updateItinerary(ItineraryDTO itineraryDTO) {
        Itinerary itineraryInDb = itineraryRepository
                .findById(itineraryDTO.getId())
                .orElseThrow(() -> new NotFoundAlertException("Itinerary id does not exist", "itinerary", "id"));

        Itinerary itinerary = itineraryConverter.convert(itineraryDTO);

        itinerary.setItineraryStatusType(itineraryInDb.getItineraryStatusType());
        itinerary.setEstimatedKM(itineraryInDb.getEstimatedKM());

        itinerary = itineraryRepository.save(itinerary);

        List<ItineraryStopPointDTO> itineraryStopPointDTOList = itineraryDTO.getItineraryStopPointList();
        List<ItineraryStopPoint> itineraryStopPointList = new ArrayList<>(itineraryStopPointRepository.findAllByItinerary_Id(itinerary.getId()));

        for (ItineraryStopPoint itineraryStopPoint : itineraryStopPointList) {
            boolean exists = false;

            for (ItineraryStopPointDTO itineraryStopPointDTO : itineraryStopPointDTOList) {

                if (itineraryStopPoint.getId() == itineraryStopPointDTO.getId()) {
                    exists = true;
                }
            }

            if (!exists) {
                itineraryStopPointRepository.delete(itineraryStopPoint);
            }
        }

        saveItineraryStopPoints(itinerary, itineraryDTO.getItineraryStopPointList(), true);

        return toItineraryDTO(itinerary);
    }

    /**
     * Function that deletes a itinerary with all the relations from the system
     *
     * @param id itinerary id
     * @return the result of the operation
     */
    public void deleteItinerary(Long id) {

        Itinerary itinerary = findItineraryById(id);

        List<ItineraryStopPoint> itineraryStopPointList = new ArrayList<>(itineraryStopPointRepository.findAllByItinerary_Id(id));

        for (ItineraryStopPoint itineraryStopPoint : itineraryStopPointList) {
            itineraryStopPointRepository.delete(itineraryStopPoint);
        }

        itineraryRepository.delete(itinerary);
    }

    /**
     * Function that will finish the current itineraryStopPoint and start the next, if there is not one end the itinerary,
     * and insert the actions made to each child
     *
     * @param id                         itinerary id
     * @param childStopPointEventDTOList List of ids and actions for each child
     * @return itineraryDTO updated with the changes
     */
    public ItineraryDTO finishItineraryStopPoint(Long id, List<ChildStopPointEventDTO> childStopPointEventDTOList) {

        Itinerary itinerary = findItineraryById(id);
        Instant timeNow = Instant.now();

        ItineraryStopPoint itineraryStopPoint =
                itineraryStopPointRepository.findFirstByItinerary_IdAndItineraryStatusType(
                        itinerary.getId(),
                        ItineraryStatusType.IN_PROGRESS
                )
                        .orElseThrow(() -> new NotFoundAlertException("Itinerary stop point in progress not found", "itineraryStopPoint", "itineraryStatus"));


        StopPoint stopPoint = itineraryStopPoint.getStopPoint();
        Set<StopAuditEvent> setStopAuditEvent = new HashSet<>();

        for (ChildStopPointEventDTO childStopPointEventDTO : childStopPointEventDTOList) {
            StopAuditEvent stopAuditEvent = new StopAuditEvent();

            stopAuditEvent.setItinerary(itinerary);
            stopAuditEvent.setChild(childService.findById(childStopPointEventDTO.getChildId()));
            stopAuditEvent.setEventTime(timeNow);
            stopAuditEvent.setStopPoint(stopPoint);
            stopAuditEvent.setEventType(childStopPointEventDTO.getEvent());

            setStopAuditEvent.add(stopAuditEventRepository.save(stopAuditEvent));
        }

        stopPoint.setStopAuditEvents(setStopAuditEvent);
        stopPoint.setEffectiveArriveTime(Instant.now());
        stopPointRepository.save(stopPoint);



        itineraryStopPoint.setItineraryStatusType(ItineraryStatusType.FINISHED);
        itineraryStopPointRepository.save(itineraryStopPoint);

        // this next section does the following: if a stopPoint is of type Collection, and if one (or more) child
        // does not check_in in the vehicle, the corresponding pair stopPoint check_out will be removed; if a stopPoint
        // is of type Deliver, nothing is removed
        if (stopPoint.getStopPointType().equals(StopPointType.COLLECTION)) {
            childStopPointEventDTOList
                    .stream()
                    .filter(child -> child.getEvent().equals(StopEventType.CANCELED))
                    .forEach(childStopPointEventDTO -> {
                        List<ItineraryStopPoint> itineraryStopPointList = itineraryStopPointRepository.findAllByItinerary_IdAndOrderGreaterThanOrderByOrder(
                                itinerary.getId(),
                                itineraryStopPoint.getOrder()
                        );
                        for (ItineraryStopPoint itineraryStopPoint1 : itineraryStopPointList) {
                            StopPoint stopPoint1 = itineraryStopPoint1.getStopPoint();
                            List<Child> childList = stopPoint1.getChildList();
                            Child child = childService.findById(childStopPointEventDTO.getChildId());
                            if (childList.contains(child) && stopPoint1.getStopPointType().equals(StopPointType.DELIVER)) {
                                childList.remove(child);
                                if (childList.isEmpty()) {
                                    itineraryStopPointRepository.delete(itineraryStopPoint1);
                                } else {
                                    itineraryStopPointRepository.save(itineraryStopPoint1);
                                }
                                break;
                            }
                        }
                    });
        }

        notifyTutors(setStopAuditEvent);

        Optional<ItineraryStopPoint> nextItineraryStopPoint =
                itineraryStopPointRepository.findFirstByItinerary_IdAndItineraryStatusTypeOrderByOrder(
                        itinerary.getId(),
                        ItineraryStatusType.READY_TO_START
                );

        if (nextItineraryStopPoint.isPresent()) {
            ItineraryStopPoint iti = nextItineraryStopPoint.get();
            iti.setItineraryStatusType(ItineraryStatusType.IN_PROGRESS);
            itineraryStopPointRepository.save(iti);
            finishStopPointUpdates(itinerary);
        } else {
            ItineraryStatusTypePatchDTO itineraryStatusTypePatchDTO = new ItineraryStatusTypePatchDTO();
            itineraryStatusTypePatchDTO.setStatusType(ItineraryStatusType.FINISHED);
            patchItineraryStatus(itinerary.getId(), itineraryStatusTypePatchDTO);
        }

        return toItineraryDTO(findItineraryById(id));
    }

    private void notifyTutors(Set<StopAuditEvent> setStopAuditEvent) {
        setStopAuditEvent.forEach(stopAuditEvent -> {
            if (stopAuditEvent.getChild().isFamility()) {
                String childName = stopAuditEvent.getChild().getName();
                Set<Tutor> tutors = stopAuditEvent.getChild().getTutors();
                tutors.forEach(tutor -> {
                    StopEventType stopEventType = stopAuditEvent.getEventType();
                    StopPointType stopPointType = stopAuditEvent.getStopPoint().getStopPointType();
                    LocationType locationType = stopAuditEvent.getStopPoint().getLocation().getType();
                    if (stopEventType.equals(StopEventType.CHECK_IN) && stopPointType.equals(StopPointType.COLLECTION) && locationType.equals(LocationType.PRIVATE)) {
                        notificationSenderService.sendCheckInNotificationForPrivateLocation(tutor.getId(), childName);
                    } else if (stopEventType.equals(StopEventType.CHECK_OUT) && stopPointType.equals(StopPointType.DELIVER) && !locationType.equals(LocationType.PRIVATE)) {
                        notificationSenderService.sendCheckOutNotificationForSchoolLocation(tutor.getId(), childName);
                    } else if (stopEventType.equals(StopEventType.CHECK_IN) && stopPointType.equals(StopPointType.COLLECTION) && !locationType.equals(LocationType.PRIVATE)) {
                        notificationSenderService.sendCheckInNotificationForSchoolLocation(tutor.getId(), childName);
                    } else if (stopEventType.equals(StopEventType.CHECK_OUT) && stopPointType.equals(StopPointType.DELIVER) && locationType.equals(LocationType.PRIVATE)) {
                        notificationSenderService.sendCheckOutNotificationForPrivateLocation(tutor.getId(), childName);
                    } else if (stopEventType.equals(StopEventType.CANCELED) && stopPointType.equals(StopPointType.COLLECTION) && locationType.equals(LocationType.PRIVATE)) {
                        notificationSenderService.sendCheckInCancelNotificationForPrivateLocation(tutor.getId(), childName);
                    } else if (stopEventType.equals(StopEventType.CANCELED) && stopPointType.equals(StopPointType.DELIVER) && !locationType.equals(LocationType.PRIVATE)) {
                        notificationSenderService.sendCheckOutCancelNotificationForSchoolLocation(tutor.getId(), childName);
                    } else if (stopEventType.equals(StopEventType.CANCELED) && stopPointType.equals(StopPointType.COLLECTION) && !locationType.equals(LocationType.PRIVATE)) {
                        notificationSenderService.sendCheckInCancelNotificationForSchoolLocation(tutor.getId(), childName);
                    } else if (stopEventType.equals(StopEventType.CANCELED) && stopPointType.equals(StopPointType.DELIVER) && locationType.equals(LocationType.PRIVATE)) {
                        notificationSenderService.sendCheckOutCancelNotificationForPrivateLocation(tutor.getId(), childName);
                    } else {
                    }
                });
            }
        });
    }

    /**
     * Function that will return the first itinerary that is in progress for a driver give min and max date
     *
     * @param driverId driver id
     * @param minToday min date time
     * @param maxToday max date time
     * @return itinerary with full detail
     */
    public ItineraryDTO findFirstByDriverIdAndItineraryStatusTypeAndScheduledTimeBetween(Long driverId, Instant minToday, Instant maxToday) {
        Optional<Itinerary> itinerary = itineraryRepository.findFirstByDriverIdAndItineraryStatusTypeAndScheduledTimeBetween(driverId, ItineraryStatusType.IN_PROGRESS, minToday, maxToday);
        return itinerary.map(this::toItineraryDTO).orElse(null);
    }

    /**
     * Function that returns the itinerayDTO with all details
     *
     * @param id Itinerary Id
     * @return itinerary with full detail
     * @throws NotFoundAlertException if id does not exist
     */
    public ItineraryDTO findById(Long id) {
        return toItineraryDTO(findItineraryById(id));
    }

    /**
     * Function that update the status of a given itinerary
     *
     * @param itineraryId Itinerary id
     * @param itineraryStatusTypePatchDTO dto of the patch to do
     */
    public void patchItineraryStatus(Long itineraryId, ItineraryStatusTypePatchDTO itineraryStatusTypePatchDTO) {
        ItineraryStatusType statusType = itineraryStatusTypePatchDTO.getStatusType();

        Itinerary itinerary = itineraryRepository
                .findById(itineraryId)
                .orElseThrow(() -> new NotFoundAlertException("Itinerary id does not exist", "itinerary", "id"));

        switch (statusType) {
            case IN_PROGRESS:
                itinerary.setEffectiveStartTime(Instant.now());

                if (Optional.ofNullable(itineraryStatusTypePatchDTO.getLongitude()).isPresent()
                    && Optional.ofNullable(itineraryStatusTypePatchDTO.getLatitude()).isPresent()) {

                    Location startLocation = new Location();

                    startLocation.setDesignation("start location of itinerary " + itineraryId);
                    startLocation.setStreet("start location of itinerary " + itineraryId);
                    startLocation.setPortNumber("start location of itinerary " + itineraryId);
                    startLocation.setPostalCode("start location of itinerary " + itineraryId);
                    startLocation.setCity("start location of itinerary " + itineraryId);
                    startLocation.setCountry("start location of itinerary " + itineraryId);
                    startLocation.setType(LocationType.PRIVATE);

                    startLocation.setLongitude(String.valueOf(itineraryStatusTypePatchDTO.getLongitude()));
                    startLocation.setLatitude(String.valueOf(itineraryStatusTypePatchDTO.getLatitude()));

                    OpenLocationCode openLocationCode = new OpenLocationCode(
                        itineraryStatusTypePatchDTO.getLatitude(),
                        itineraryStatusTypePatchDTO.getLongitude()
                    );

                    startLocation.setPlusCode(openLocationCode.getCode());

                    startLocation = locationRepository.save(startLocation);

                    itinerary.setStartLocation(startLocation);

                }

                itineraryStopPointRepository.findFirstByItinerary_IdAndItineraryStatusTypeOrderByOrder(
                        itinerary.getId(),
                        ItineraryStatusType.READY_TO_START
                ).ifPresent(itineraryStopPoint -> {
                            itineraryStopPoint.setItineraryStatusType(ItineraryStatusType.IN_PROGRESS);
                            itineraryStopPointRepository.save(itineraryStopPoint);
                        }
                );

                createOrStartItineraryUpdates(itinerary);

                sendStartItineraryNotificationWithPrivateAsOrigin(itineraryId);

                break;
            case CANCELED:
                itinerary.setEffectiveEndTime(Instant.now());

                itineraryStopPointRepository.findFirstByItinerary_IdAndItineraryStatusType(
                        itinerary.getId(),
                        ItineraryStatusType.IN_PROGRESS
                ).ifPresent(itineraryStopPoint -> {
                    itineraryStopPoint.setItineraryStatusType(ItineraryStatusType.CANCELED);
                    itineraryStopPointRepository.save(itineraryStopPoint);
                });

                itineraryStopPointRepository.findAllByItinerary_IdAndItineraryStatusType(
                    itinerary.getId(),
                    ItineraryStatusType.READY_TO_START
                ).forEach(
                    itineraryStopPoint -> {
                        itineraryStopPoint.setItineraryStatusType(ItineraryStatusType.CANCELED);
                        itineraryStopPointRepository.save(itineraryStopPoint);
                    }
                );

                // TODO: o que fazemos em relação às notificações quando o percurso é cancelado? e vai depender do estado em que se encontra.

                break;
            case FINISHED:
                itinerary.setEffectiveEndTime(Instant.now());

                itineraryStopPointRepository.findFirstByItinerary_IdAndItineraryStatusType(
                        itinerary.getId(),
                        ItineraryStatusType.IN_PROGRESS
                ).ifPresent(itineraryStopPoint -> {
                    itineraryStopPoint.setItineraryStatusType(ItineraryStatusType.FINISHED);
                    itineraryStopPointRepository.save(itineraryStopPoint);
                });

                break;
            default:
        }

        itinerary.setItineraryStatusType(statusType);

        itineraryRepository.save(itinerary);
    }

    /**
     * Function that returns a list of itineraries with pagination of a organization with filter by name and status
     *
     * @param organizationId id of organization
     * @param name           name to filter the itineraries
     * @param statusTypes    status of itinerary
     * @param pageable       number of page, size of page and the way is sort
     * @return List with the itineraries
     */
    public Page<Itinerary> getItinerariesWithFilters(Long organizationId, String name, List<ItineraryStatusType> statusTypes, Pageable pageable) {
        if (Optional.ofNullable(name).isEmpty()) {
            name = "";
        }

        if (Optional.ofNullable(statusTypes).isEmpty()) {
            statusTypes = Arrays.asList(ItineraryStatusType.values());
        }

        if (Optional.ofNullable(organizationId).isEmpty()) {
            return itineraryRepository.findAllByItineraryStatusTypeInAndNameIsContainingIgnoreCase(statusTypes, name, pageable);
        }

        return itineraryRepository.findAllByItineraryStatusTypeInAndOrganization_IdAndNameIsContainingIgnoreCase(statusTypes, organizationId, name, pageable);
    }

    /**
     * Function that returns all itineraries for a given drive giving a start and end date
     *
     * @param driverId driver id to search
     * @param minDate  minimum date to search
     * @param maxDate  maximum date to search
     * @return list with itineraryDTO (no full detail)
     */
    public List<ItineraryDTO> findAllByDriverIdAndScheduledTimeBetween(Long driverId, Instant minDate, Instant maxDate) {
        List<Itinerary> allByDriverIdAndScheduledTimeAfter = itineraryRepository.findAllByDriverIdAndScheduledTimeBetween(driverId, minDate, maxDate);
        return allByDriverIdAndScheduledTimeAfter.stream().map(this::toItineraryDTO).collect(Collectors.toList());
    }

    /**
     * Function to find an itinerary
     *
     * @param id itinerary id to search
     * @return itinerary
     * @throws NotFoundAlertException if does not find the itinerary with the given id
     */
    public Itinerary findItineraryById(Long id) {
        Itinerary itinerary = itineraryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundAlertException("Itinerary id does not exist", "itinerary", "id"));

        return itinerary;
    }

    /**
     * Function that creates a new itinerary and save it in the system
     *
     * @param newItineraryDTO the new itinerary
     * @return itineraryDTO with all the details
     */
    public ItineraryDTO createItinerary(ItineraryDTO newItineraryDTO) {

        Itinerary itinerary = itineraryConverter.convert(newItineraryDTO);
        itinerary.setItineraryStatusType(ItineraryStatusType.READY_TO_START);

        itinerary.setDriver(driverService.findById(newItineraryDTO.getDriver().getId()));
        itinerary.setVehicle(vehicleService.findById(newItineraryDTO.getVehicle().getId()));
        itinerary.setOrganization(organizationService.findById(newItineraryDTO.getOrganization().getId()));
        itinerary.estimatedStartLocation(locationService.findById(newItineraryDTO.getEstimatedStartLocation().getId()));

        // the estimated length of the itinerary is set to 0.0 Kms here, since the attribute is not nullable; later in this execution it will be filled with the real estimated value
        itinerary.setEstimatedKM(0.0);

        // the calculation is done when creating the itinerary stop points
        itinerary.setOcupation(0.0);

        Itinerary itinerarySaved = itineraryRepository.save(itinerary);

        saveItineraryStopPoints(itinerary, newItineraryDTO.getItineraryStopPointList(), false);

        return toItineraryDTO(itinerarySaved);
    }

    /**
     * Function that creates itinerary stop points with all the details into the system and connects with itinerary
     *
     * @param itinerary                 itinerary the points have to be connected
     * @param itineraryStopPointListDTO itineraries stop points list to create
     */
    private void saveItineraryStopPoints(Itinerary itinerary, List<ItineraryStopPointDTO> itineraryStopPointListDTO, boolean update) {

        List<Integer> listChildMove = new ArrayList<>();

        itineraryStopPointListDTO.forEach(itineraryStopPointDTO -> {

            StopPointDTO stopPointDTO = itineraryStopPointDTO.getStopPoint();

            if (!update) {
                if (itineraryStopPointDTO.getId() != null) {
                    throw new BadRequestAlertException("A new itineraryStopPoint cannot already have an ID", "itineraryStopPoint", "idexists");
                }

                if (stopPointDTO.getId() != null) {
                    throw new BadRequestAlertException("A new stopPoint cannot already have an ID", "stopPoint", "idexists");
                }
            }

            ItineraryStopPoint itineraryStopPoint = itineraryStopPointConverter.convert(itineraryStopPointDTO);

            StopPoint stopPoint = stopPointConverter.convert(stopPointDTO);

            stopPoint.setLocation(locationService.findById(stopPointDTO.getLocation().getId()));

            List<Child> childList = new ArrayList<>();

            stopPointDTO.getChildList().forEach(childDTO -> {
                Child child = childService.findById(childDTO.getId());
                childList.add(child);
            });

            stopPoint.setChildList(childList);
            stopPoint.setStopAuditEvents(new HashSet<>());

            stopPoint = stopPointRepository.save(stopPoint);

            itineraryStopPoint.setItineraryStatusType(ItineraryStatusType.READY_TO_START);
            itineraryStopPoint.setStopPoint(stopPoint);
            itineraryStopPoint.setItinerary(itinerary);

            itineraryStopPointRepository.save(itineraryStopPoint);

            if (stopPoint.getStopPointType().equals(StopPointType.COLLECTION)){
                listChildMove.add(stopPoint.getChildList().size());
            } else {
                listChildMove.add(-1 * stopPoint.getChildList().size());
            }
        });


        List<Double> ocupationStopPoints = new ArrayList<>();
        Double totalOcupationMean = 0.0;
        int totalChildren = 0;
        int busCapacity = itinerary.getVehicle().getCapacity();

        for (Integer numberChild : listChildMove) {
            totalChildren += numberChild;

            Double calculation = (double) totalChildren / busCapacity;
            totalOcupationMean += calculation;
            ocupationStopPoints.add(calculation);

            if (calculation > 1) {
                throw new BadRequestAlertException("Exceed the limit of children in vehicle", "vehicle", "vehicleCapacity");
            }
        }

        totalOcupationMean = totalOcupationMean / ocupationStopPoints.size();

        itinerary.setOcupation(totalOcupationMean);
        itineraryRepository.save(itinerary);

        createOrStartItineraryUpdates(itinerary);
    }

    /**
     * Convert an itinerary to itineraryDTO with full detail
     *
     * @param itinerary object to convert
     * @return itineraryDTO with full detail
     */
    private ItineraryDTO toItineraryDTO(Itinerary itinerary) {
        ItineraryDTO itineraryDTO = itineraryConverter.convert(itinerary);
        itineraryDTO.setItineraryStopPointList(itineraryStopPointRepository.findAllByItinerary_Id(itinerary.getId())
                .stream()
                .map(itineraryStopPointConverter::convert)
                .sorted(Comparator.comparing(ItineraryStopPointDTO::getOrder))
                .collect(Collectors.toList()));
        return itineraryDTO;
    }

    private void sendStartItineraryNotificationWithPrivateAsOrigin(Long itineraryId) {
        itineraryStopPointRepository.findAllByItinerary_Id(itineraryId).forEach(itineraryStopPoint -> {
            StopPoint stopPoint = itineraryStopPoint.getStopPoint();
            if (stopPoint.getStopPointType().equals(StopPointType.COLLECTION) && stopPoint.getLocation().getType().equals(LocationType.PRIVATE)) {
                stopPoint.getChildList().forEach(child -> {
                    if (child.isFamility()) {
                        Set<Tutor> tutors = child.getTutors();
                        tutors.forEach(tutor -> {
                            Instant estimatedTimeOfArrival;
                            if (stopPoint.getEstimatedArriveTime() != null) {
                                estimatedTimeOfArrival = stopPoint.getEstimatedArriveTime();
                            } else {
                                estimatedTimeOfArrival = stopPoint.getScheduledTime();
                            }
                            notificationSenderService.sendStartItineraryNotificationWithPrivateAsOrigin(tutor.getId(), child.getName(), estimatedTimeOfArrival);
                        });
                    }
                });
            }
        });
    }

    public void createOrStartItineraryUpdates(Itinerary itinerary) {
        if (itinerary.getPromoterItinerary().getService().getNeedsETA()) {
            Optional<ItineraryDirections> itineraryDirections = setupForItineraryETACalculationOnCreateOrStartItinerary(itinerary.getId());
            itineraryDirections.ifPresent(directions -> {
                updateItinerary(itinerary, directions);
                updateStopPointsOnStartItinerary(itinerary, directions);
            });
        } else {
            itinerary.setEstimatedKM(0.0);
            itinerary.setEstimatedTotalTime(0);
            itineraryRepository.save(itinerary);

            List<ItineraryStopPoint> itineraryStopPointList = itineraryStopPointRepository.findAllByItinerary_Id(itinerary.getId());
            itineraryStopPointList.forEach(itineraryStopPoint -> itineraryStopPoint.getStopPoint().setEstimatedArriveTime(null));
        }
    }

    private Optional<ItineraryDirections> setupForItineraryETACalculationOnCreateOrStartItinerary(Long itineraryId) {
        ItineraryDTO itineraryDTO = findById(itineraryId);

        LatLng originCoordinates;
        if (Optional.ofNullable(itineraryDTO.getStartLocation()).isPresent()) {
            originCoordinates = getCoordinatesOfLocationDTO(itineraryDTO.getStartLocation());
        } else {
            originCoordinates = getCoordinatesOfLocationDTO(itineraryDTO.getEstimatedStartLocation());
        }

        if (itineraryDTO.getItineraryStopPointList().isEmpty()) {
            return Optional.empty();
        }

        LocationDTO lastLocationDTO = itineraryDTO.getItineraryStopPointList().get(itineraryDTO.getItineraryStopPointList().size() - 1).getStopPoint().getLocation();
        LatLng destinyCoordinates = getCoordinatesOfLocationDTO(lastLocationDTO);
        List<ItineraryStopPointDTO> itineraryStopPointDTOList = itineraryDTO.getItineraryStopPointList();
        itineraryStopPointDTOList.remove(itineraryDTO.getItineraryStopPointList().size() - 1);
        List<LatLng> waypoints = new ArrayList<>();
        itineraryStopPointDTOList.forEach(itineraryStopPointDTO -> {
            LatLng coordinates = getCoordinatesOfLocationDTO(itineraryStopPointDTO.getStopPoint().getLocation());
            waypoints.add(coordinates);
        });
        Instant startTime = itineraryDTO.getEffectiveStartTime();
        if (startTime == null) {
            startTime = itineraryDTO.getScheduledTime();
        }

        return googleMapsService.calculateItineraryETA(startTime, Duration.ofMinutes(applicationProperties.getItinerary().getWaypointTime()), originCoordinates, destinyCoordinates, waypoints.toArray(new LatLng[0]));
    }

    public void updateItinerary(Itinerary itinerary, ItineraryDirections itineraryDirections) {
        itinerary.setEstimatedKM((double) itineraryDirections.getTotalDistance().getDistanceInMeters() / 1000);
        itinerary.setEstimatedTotalTime((int) itineraryDirections.getTotalDuration().getSeconds());
        itineraryRepository.save(itinerary);
    }

    public void updateStopPointsOnStartItinerary(Itinerary itinerary, ItineraryDirections itineraryDirections) {
        List<ItineraryStopPoint> itineraryStopPointList = itineraryStopPointRepository.findAllByItinerary_Id(itinerary.getId());
        itineraryStopPointList.sort(Comparator.comparing(ItineraryStopPoint::getOrder));
        Instant startTime = itinerary.getEffectiveStartTime();
        if (startTime == null) {
            startTime = itinerary.getScheduledTime();
        }
        Duration cumulativeDuration = Duration.ZERO;
        for (int i = 0; i < itineraryStopPointList.size(); i++) {
            Duration legDuration = itineraryDirections.getItineraryLegs().get(i).getDuration();
            cumulativeDuration = cumulativeDuration.plus(legDuration);
            Instant newStopPointEstimatedArriveTime = startTime.plus(cumulativeDuration);
            itineraryStopPointList.get(i).getStopPoint().setEstimatedArriveTime(newStopPointEstimatedArriveTime);
        }
    }

    public void finishStopPointUpdates(Itinerary itinerary) {
        if (itinerary.getPromoterItinerary().getService().getNeedsETA()) {
            Optional<ItineraryDirections> itineraryDirections = setupForItineraryETACalculationOnFinishStopPoint(itinerary.getId());
            itineraryDirections.ifPresent(directions -> updateRemainingStopPoints(itinerary, directions));
        } else {
            List<ItineraryStopPoint> itineraryStopPointList = itineraryStopPointRepository.findAllByItinerary_Id(itinerary.getId())
                    .stream()
                    .filter(itineraryStopPoint -> itineraryStopPoint.getItineraryStatusType().equals(ItineraryStatusType.IN_PROGRESS) || itineraryStopPoint.getItineraryStatusType().equals(ItineraryStatusType.READY_TO_START))
                    .sorted(Comparator.comparing(ItineraryStopPoint::getOrder))
                    .collect(Collectors.toList());
            itineraryStopPointList.forEach(itineraryStopPoint -> itineraryStopPoint.getStopPoint().setEstimatedArriveTime(null));
        }
    }

    private Optional<ItineraryDirections> setupForItineraryETACalculationOnFinishStopPoint(Long itineraryId) {
        ItineraryDTO itineraryDTO = findById(itineraryId);
        List<ItineraryStopPointDTO> remainingItineraryStopPointDTOList = itineraryDTO.getItineraryStopPointList()
                .stream()
                .filter(itineraryStopPointDTO -> itineraryStopPointDTO.getItineraryStatusType().equals(ItineraryStatusType.IN_PROGRESS) || itineraryStopPointDTO.getItineraryStatusType().equals(ItineraryStatusType.READY_TO_START))
                .collect(Collectors.toList());
        Location originLocation = itineraryStopPointRepository.findFirstByItinerary_IdAndItineraryStatusTypeOrderByOrderDesc(itineraryId, ItineraryStatusType.FINISHED).get().getStopPoint().getLocation();
        LatLng originCoordinates = getCoordinatesOfLocation(originLocation);
        LocationDTO lastLocationDTO = remainingItineraryStopPointDTOList.get(remainingItineraryStopPointDTOList.size() - 1).getStopPoint().getLocation();
        LatLng destinyCoordinates = getCoordinatesOfLocationDTO(lastLocationDTO);
        remainingItineraryStopPointDTOList.remove(remainingItineraryStopPointDTOList.size() - 1);
        List<LatLng> waypoints = new ArrayList<>();
        remainingItineraryStopPointDTOList.forEach(itineraryStopPointDTO -> {
            LatLng coordinates = getCoordinatesOfLocationDTO(itineraryStopPointDTO.getStopPoint().getLocation());
            waypoints.add(coordinates);
        });

        return googleMapsService.calculateItineraryETA(Instant.now(), Duration.ofMinutes(applicationProperties.getItinerary().getWaypointTime()), originCoordinates, destinyCoordinates, waypoints.toArray(new LatLng[0]));
    }

    private void updateRemainingStopPoints(Itinerary itinerary, ItineraryDirections itineraryDirections) {
        List<ItineraryStopPoint> itineraryStopPointList = itineraryStopPointRepository.findAllByItinerary_Id(itinerary.getId())
                .stream()
                .filter(itineraryStopPoint ->
                        itineraryStopPoint.getItineraryStatusType().equals(ItineraryStatusType.IN_PROGRESS) ||
                                itineraryStopPoint.getItineraryStatusType().equals(ItineraryStatusType.READY_TO_START)
                )
                .sorted(Comparator.comparing(ItineraryStopPoint::getOrder))
                .collect(Collectors.toList());
        Instant now = Instant.now();
        Duration cumulativeDuration = Duration.ZERO;
        for (int i = 0; i < itineraryStopPointList.size(); i++) {
            Duration legDuration = itineraryDirections.getItineraryLegs().get(i).getDuration();
            cumulativeDuration = cumulativeDuration.plus(legDuration);
            Instant newStopPointEstimatedArriveTime = now.plus(cumulativeDuration);
            itineraryStopPointList.get(i).getStopPoint().setEstimatedArriveTime(newStopPointEstimatedArriveTime);
        }
    }

    private LatLng getCoordinatesOfLocation(Location location) {
        return new LatLng(Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()));
    }

    private LatLng getCoordinatesOfLocationDTO(LocationDTO locationDTO) {
        return new LatLng(Double.parseDouble(locationDTO.getLatitude()), Double.parseDouble(locationDTO.getLongitude()));
    }
}
