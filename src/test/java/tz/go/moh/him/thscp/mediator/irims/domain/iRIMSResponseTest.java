package tz.go.moh.him.thscp.mediator.irims.domain;

import com.google.gson.Gson;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class iRIMSResponseTest {

    @Test
    public void testIRIMSInboundAck() throws Exception {
        String jsonIRIMSInboundAckPayload = "{\"Status\":200,\"Message\":\"Success\"}";
        iRIMSResponse irimsResponse = new Gson().fromJson(jsonIRIMSInboundAckPayload, iRIMSResponse.class);

        assertEquals(200, irimsResponse.getStatus());
        assertEquals("Success", irimsResponse.getMessage());

    }
}
