package pt.famility.backoffice.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pt.famility.backoffice.domain.Location;
import pt.famility.backoffice.domain.PromoterItinerary;
import pt.famility.backoffice.domain.PromoterStopPoint;
import pt.famility.backoffice.service.dto.LocationDTO;
import pt.famility.backoffice.service.dto.PromoterItineraryDTO;
import pt.famility.backoffice.service.dto.PromoterStopPointDTO;

/**
 * Mapper for the entity {@link PromoterStopPoint} and its DTO {@link PromoterStopPointDTO}.
 */
@Mapper(componentModel = "spring")
public interface PromoterStopPointMapper extends EntityMapper<PromoterStopPointDTO, PromoterStopPoint> {
    @Mapping(target = "promoterItinerary", source = "promoterItinerary", qualifiedByName = "promoterItineraryName")
    @Mapping(target = "location", source = "location", qualifiedByName = "locationStreet")
    PromoterStopPointDTO toDto(PromoterStopPoint s);

    @Named("promoterItineraryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    PromoterItineraryDTO toDtoPromoterItineraryName(PromoterItinerary promoterItinerary);

    @Named("locationStreet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "street", source = "street")
    LocationDTO toDtoLocationStreet(Location location);
}
