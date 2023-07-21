package pt.famility.backoffice.service.mapper;

import org.mapstruct.*;
import pt.famility.backoffice.domain.Organization;
import pt.famility.backoffice.service.dto.OrganizationDTO;

/**
 * Mapper for the entity {@link Organization} and its DTO {@link OrganizationDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrganizationMapper extends EntityMapper<OrganizationDTO, Organization> {}
