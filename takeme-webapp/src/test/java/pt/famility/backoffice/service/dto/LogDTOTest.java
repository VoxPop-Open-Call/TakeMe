package pt.famility.backoffice.service.dto;

import org.junit.Test;

import static org.junit.Assert.*;

public class LogDTOTest {

    @Test
    public void testToString() {
        // GIVEN
        LogDTO.Stackframe stackFrame = new LogDTO.Stackframe();
        stackFrame.setColumnNumber(1);
        stackFrame.setFunctionName("goBack");

        LogDTO.Stackframe[] frames = {stackFrame};

        LogDTO.ErrorDetail detail = new LogDTO.ErrorDetail();
        detail.setStack(frames);
        detail.setAppId("app 1");
        detail.setName("detail name");

        LogDTO.ErrorDetail[] details = {detail};

        LogDTO logEntry = new LogDTO();
        logEntry.setAdditional(details);
        logEntry.setMessage("Msg");
        logEntry.setLevel(2);



        // WHEN calling toString
        System.out.println(logEntry);

        // THEN check output :).
    }
}
