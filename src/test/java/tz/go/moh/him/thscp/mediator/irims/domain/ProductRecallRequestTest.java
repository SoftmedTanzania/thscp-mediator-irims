package tz.go.moh.him.thscp.mediator.irims.domain;
import com.google.gson.Gson;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProductRecallRequestTest {

    @Test
    public void testThscpAck() throws Exception {
        String jsonThscpAckPayload = "{\"uuid\":\"5ff2b29416a3c934156395d3\",\"actionRequired\":\"action1\"," +
                "\"affectedCommunity\":\"rai\",\"batchNumber\":\"02\",\"closureDate\":\"2020-11-27\"," +
                "\"description\":\"this is action\", \"distributedQuantity\":10,\"issue\":\"issuedone\" ," +
                "\"recallDate\":\"2020-01-01\", \"recallFrequency\":10, \"recalledQuantity\":200," +
                " \"receivedQuantity\":200, \"startDate\":\"2020-01-02\", \"unit\":\"unit\"}";
        ProductRecallRequest irimsRequest = new Gson().fromJson(jsonThscpAckPayload, ProductRecallRequest.class);

        assertEquals("5ff2b29416a3c934156395d3", irimsRequest.getUuid());
        assertEquals("action1", irimsRequest.getActionRequired());
        assertEquals("rai", irimsRequest.getAffectedCommunity());
        assertEquals("02", irimsRequest.getBatchNumber());
        assertEquals("2020-11-27", irimsRequest.getClosureDate());
        assertEquals("this is action", irimsRequest.getDescription());
        assertEquals(10, irimsRequest.getDistributedQuantity());
        assertEquals("issuedone", irimsRequest.getIssue());
        assertEquals("2020-01-01", irimsRequest.getRecallDate());
        assertEquals(10, irimsRequest.getRecallFrequency());
        assertEquals(200, irimsRequest.getRecalledQuantity());
        assertEquals(200, irimsRequest.getReceivedQuantity());
        assertEquals("2020-01-02", irimsRequest.getStartDate());
        assertEquals("unit", irimsRequest.getUnit());
    }
}
