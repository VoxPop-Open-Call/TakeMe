package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class DocumentFileDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long   id;
	private String fileName;
	private String contentType;
	private String fileContent;
}
