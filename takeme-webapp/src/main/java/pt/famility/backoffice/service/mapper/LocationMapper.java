package pt.famility.backoffice.service.mapper;

import org.mapstruct.Mapper;
import pt.famility.backoffice.domain.Location;
import pt.famility.backoffice.service.dto.LocationDTO;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {}
