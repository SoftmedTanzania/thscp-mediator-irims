package tz.go.moh.him.thscp.mediator.irims.domain;

import com.google.gson.Gson;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class iRIMSResponseTest {

    @Test
    public void testIRIMSInboundAck() throws Exception {
        String jsonIRIMSInboundAckPayload = "{\"imported\":1,\"updated\":0,\"ignored\":7382,\"status\":\"Success\",\"iL_TransactionIDNumber\":\"5ff2b29416a3c934156395d3\",\"il_TransactionIDNumber\":\"5ff2b29416a3c934156395d3\"}";
        iRIMSResponse irimsResponse = new Gson().fromJson(jsonIRIMSInboundAckPayload, iRIMSResponse.class);

        assertEquals(200, irimsResponse.getStatus());
        assertEquals("Success", irimsResponse.getMessage());

    }
}
