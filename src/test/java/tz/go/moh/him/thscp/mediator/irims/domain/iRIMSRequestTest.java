package tz.go.moh.him.thscp.mediator.irims.domain;
import com.google.gson.Gson;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class iRIMSRequestTest {

    @Test
    public void testThscpAck() throws Exception {
        String jsonThscpAckPayload = "{\"imported\":1,\"updated\":0,\"ignored\":7382,\"status\":\"Success\",\"iL_TransactionIDNumber\":\"5ff2b29416a3c934156395d3\",\"il_TransactionIDNumber\":\"5ff2b29416a3c934156395d3\"}";
        iRIMSRequest irimsRequest = new Gson().fromJson(jsonThscpAckPayload, iRIMSRequest.class);

        assertEquals("5ff2b29416a3c934156395d3", irimsRequest.getUuid());
        assertEquals("action1", irimsRequest.getActionRequired());
        assertEquals("rai", irimsRequest.getAffectedCommunity());
        assertEquals("02", irimsRequest.getBatchNumber());
        assertEquals("2020-11-27", irimsRequest.getClosureDate());
        assertEquals("this is action ", irimsRequest.getDescription());
        assertEquals(10, irimsRequest.getDistributedQuantity());
        assertEquals("issueone", irimsRequest.getIssue());
        assertEquals("2020-01-01", irimsRequest.getRecallDate());
        assertEquals(10, irimsRequest.getRecallFrequency());
        assertEquals(200, irimsRequest.getRecalledQuantity());
        assertEquals(200, irimsRequest.getReceivedQuantity());
        assertEquals("2020-01-02", irimsRequest.getStartDate());
        assertEquals("unit", irimsRequest.getUnit());
    }
}
