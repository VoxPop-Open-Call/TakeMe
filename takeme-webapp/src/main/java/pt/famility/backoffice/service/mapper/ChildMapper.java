package pt.famility.backoffice.service.mapper;

import org.mapstruct.*;
import pt.famility.backoffice.domain.Child;
import pt.famility.backoffice.service.dto.ChildDTO;

/**
 * Mapper for the entity {@link Child} and its DTO {@link ChildDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChildMapper extends EntityMapper<ChildDTO, Child> {}
