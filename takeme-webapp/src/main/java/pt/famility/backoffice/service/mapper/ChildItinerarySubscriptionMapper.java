package pt.famility.backoffice.service.mapper;

import org.mapstruct.*;
import pt.famility.backoffice.domain.Child;
import pt.famility.backoffice.domain.ChildItinerarySubscription;
import pt.famility.backoffice.domain.PromoterItinerary;
import pt.famility.backoffice.domain.User;
import pt.famility.backoffice.service.dto.ChildDTO;
import pt.famility.backoffice.service.dto.ChildItinerarySubscriptionDTO;
import pt.famility.backoffice.service.dto.PromoterItineraryDTO;
import pt.famility.backoffice.service.dto.UserDTO;

/**
 * Mapper for the entity {@link ChildItinerarySubscription} and its DTO {@link ChildItinerarySubscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChildItinerarySubscriptionMapper extends EntityMapper<ChildItinerarySubscriptionDTO, ChildItinerarySubscription> {
    @Mapping(target = "child", source = "child", qualifiedByName = "childName")
    @Mapping(target = "promoterItinerary", source = "promoterItinerary", qualifiedByName = "promoterItineraryName")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    ChildItinerarySubscriptionDTO toDto(ChildItinerarySubscription s);

    @Named("childName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "photoId", source = "photoFile.id")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "nifNumber", source = "nifNumber")
    ChildDTO toDtoChildName(Child child);

    @Named("promoterItineraryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "service", source = "service")
    @Mapping(target = "organization", source = "organization")
    PromoterItineraryDTO toDtoPromoterItineraryName(PromoterItinerary promoterItinerary);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Mapping(target = "user.authorities", ignore = true)
    ChildItinerarySubscription toEntity(ChildItinerarySubscriptionDTO dto);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user.authorities", ignore = true)
    void partialUpdate(@MappingTarget ChildItinerarySubscription entity, ChildItinerarySubscriptionDTO dto);
}
