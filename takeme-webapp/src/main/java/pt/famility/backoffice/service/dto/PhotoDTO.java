package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class PhotoDTO {

    @NotBlank
    private String photo;

    public PhotoDTO(String photo) {
        this.photo = photo;
    }
}
