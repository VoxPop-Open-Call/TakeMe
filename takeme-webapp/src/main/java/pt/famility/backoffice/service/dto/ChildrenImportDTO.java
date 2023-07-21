package pt.famility.backoffice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildrenImportDTO {
    private Boolean success;
    private Integer createdChildrenNumber;
    private Integer updatedChildrenNumber;
    private Integer lineNumber;
    private String columnLetter;
}
