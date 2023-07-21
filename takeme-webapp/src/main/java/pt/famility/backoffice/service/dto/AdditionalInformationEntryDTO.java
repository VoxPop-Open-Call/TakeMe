package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdditionalInformationEntryDTO {

    private String additionalInformationEntry;

    public AdditionalInformationEntryDTO(String additionalInformationEntry) {
        this.additionalInformationEntry = additionalInformationEntry;
    }
}
