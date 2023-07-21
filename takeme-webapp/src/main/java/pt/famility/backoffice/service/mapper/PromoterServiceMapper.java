package pt.famility.backoffice.service.mapper;

import org.mapstruct.Mapper;
import pt.famility.backoffice.domain.PromoterService;
import pt.famility.backoffice.service.dto.PromoterServiceDTO;

/**
 * Mapper for the entity {@link PromoterService} and its DTO {@link PromoterServiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface PromoterServiceMapper extends EntityMapper<PromoterServiceDTO, PromoterService> {}
