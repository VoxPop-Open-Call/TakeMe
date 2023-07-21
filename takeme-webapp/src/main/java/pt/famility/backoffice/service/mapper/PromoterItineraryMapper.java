package pt.famility.backoffice.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pt.famility.backoffice.domain.Organization;
import pt.famility.backoffice.domain.PromoterItinerary;
import pt.famility.backoffice.domain.PromoterService;
import pt.famility.backoffice.service.dto.OrganizationDTO;
import pt.famility.backoffice.service.dto.PromoterItineraryDTO;
import pt.famility.backoffice.service.dto.PromoterServiceDTO;

/**
 * Mapper for the entity {@link PromoterItinerary} and its DTO {@link PromoterItineraryDTO}.
 */
@Mapper(componentModel = "spring")
public interface PromoterItineraryMapper extends EntityMapper<PromoterItineraryDTO, PromoterItinerary> {
    @Mapping(target = "service", source = "service", qualifiedByName = "promoterServiceName")
    @Mapping(target = "organization", source = "organization", qualifiedByName = "organizationName")
    PromoterItineraryDTO toDto(PromoterItinerary s);

    @Named("promoterServiceName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "needsETA", source = "needsETA")
    @Mapping(target = "transportType", source = "transportType")
    PromoterServiceDTO toDtoPromoterServiceName(PromoterService promoterService);

    @Named("organizationName")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    OrganizationDTO toDtoOrganizationName(Organization organization);
}
