package tz.go.moh.him.thscp.mediator.irims.mock;
import tz.go.moh.him.mediator.core.serialization.JsonSerializer;
import org.junit.Assert;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.testing.MockHTTPConnector;
import tz.go.moh.him.thscp.mediator.irims.domain.ProductRecallRequest;

import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.util.Arrays;


/**
 * Represents a mock destination.
 */
public class MockDestination extends MockHTTPConnector {
    /**
     * serializer initialization
     */
    public JsonSerializer serializer = new JsonSerializer();

    /**
     * The expected message type
     */
    private final String expectedMessageType;


    /**
     * Gets the response.
     *
     * @return Returns the response.
     */
    @Override
    public String getResponse() {
        return null;
    }

    /**
     * Gets the status code.
     *
     * @return Returns the status code.
     */
    @Override
    public Integer getStatus() {
        return 200;
    }

    /**
     * Gets the HTTP headers.
     *
     * @return Returns the HTTP headers.
     */
    @Override
    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }


    public MockDestination(String expectedMessageType) {
        this.expectedMessageType = expectedMessageType;
    }
    /**
     * Handles the message.
     *
     * @param msg The message.
     */
    @Override
    public void executeOnReceive(MediatorHTTPRequest msg){
        System.out.println("Received body : " + msg.getBody());

        switch(expectedMessageType) {
            case "productRecall":
                List<ProductRecallRequest> productRecall = Arrays.asList(serializer.deserialize(msg.getBody(), ProductRecallRequest[].class));
                Assert.assertNotNull(productRecall);
                Assert.assertEquals(1, productRecall.size());
                Assert.assertEquals("453b906b-5dc4-4ba2-a37b-527f2987b0d8", productRecall.get(0).getUuid());
                Assert.assertEquals("action1", productRecall.get(0).getActionRequired());
                Assert.assertEquals("rai", productRecall.get(0).getAffectedCommunity());
                Assert.assertEquals("02", productRecall.get(0).getBatchNumber());
                Assert.assertEquals("2020-11-27", productRecall.get(0).getClosureDate());
                Assert.assertEquals("this is action ", productRecall.get(0).getDescription());
                Assert.assertEquals(0, productRecall.get(0).getDistributedQuantity());
                Assert.assertEquals("issueone", productRecall.get(0).getIssue());
                Assert.assertEquals("2020-11-27", productRecall.get(0).getRecallDate());
                Assert.assertEquals(10, productRecall.get(0).getRecallFrequency());
                Assert.assertEquals(200, productRecall.get(0).getRecalledQuantity());
                Assert.assertEquals(2000, productRecall.get(0).getReceivedQuantity());
                Assert.assertEquals("2020-11-27", productRecall.get(0).getStartDate());
                Assert.assertEquals("unit", productRecall.get(0).getUnit());
                break;
            default:
                break;

        }
    }
}