package pt.famility.backoffice.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * Logger entry, according to ngx-logger contract
 */
@Data
public class LogDTO {


    private int level;
    @NotNull
    private String message;
    private String fileName;
    private String lineNumber;
    private ErrorDetail[] additional;

    @Data
    public static class ErrorDetail {
        private String appId;
        private String user;
        private String name;
        private String message;
        private String url;
        private Integer status;
        private Stackframe[] stack;
    }

    @Data
    public static class Stackframe {
        public Integer columnNumber;
        public String fileName;
        public String functionName;
        public Integer lineNumber;
        public String source;
    }
}
