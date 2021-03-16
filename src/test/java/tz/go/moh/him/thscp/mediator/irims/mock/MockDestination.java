package tz.go.moh.him.thscp.mediator.irims.mock;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.testing.MockHTTPConnector;
import tz.go.moh.him.thscp.mediator.irims.domain.IRIMSRequest;
import tz.go.moh.him.thscp.mediator.irims.orchestrator.ProductRecallOrchestratorTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

/**
 * Represents a mock destination.
 */
public class MockDestination extends MockHTTPConnector {

    /**
     * Initializes a new instance of the {@link MockDestination} class.
     */
    public MockDestination() {
    }

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

    /**
     * Handles the message.
     *
     * @param msg The message.
     */
    @Override
    public void executeOnReceive(MediatorHTTPRequest msg) {
        System.out.println("Received body : " + msg.getBody());
        InputStream stream = ProductRecallOrchestratorTest.class.getClassLoader().getResourceAsStream("request.json");

        Assert.assertNotNull(stream);

        ObjectMapper mapper = new ObjectMapper();

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        IRIMSRequest expected;

        try {
            expected = mapper.readValue(IOUtils.toString(stream), IRIMSRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        IRIMSRequest actual = null;
        try {
            actual = mapper.readValue(msg.getBody(), IRIMSRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);

        Assert.assertEquals(expected.getUuid(), actual.getUuid());
        Assert.assertEquals(expected.getActionRequired(), actual.getActionRequired());
        Assert.assertEquals(expected.getAffectedCommunity(), actual.getAffectedCommunity());
        Assert.assertEquals(expected.getBatchNumber(), actual.getBatchNumber());
        Assert.assertEquals(expected.getClosureDate(), actual.getClosureDate());
        Assert.assertEquals(expected.getDescription(), actual.getDescription());
        Assert.assertEquals(expected.getDistributedQuantity(), actual.getDistributedQuantity());
        Assert.assertEquals(expected.getIssue(), actual.getIssue());
        Assert.assertEquals(expected.getRecallDate(), actual.getRecallDate());
        Assert.assertEquals(expected.getRecallFrequency(), actual.getRecallFrequency());
        Assert.assertEquals(expected.getRecalledQuantity(), actual.getRecalledQuantity());
        Assert.assertEquals(expected.getReceivedQuantity(), actual.getReceivedQuantity());
        Assert.assertEquals(expected.getStartDate(), actual.getStartDate());
        Assert.assertEquals(expected.getUnit(), actual.getUnit());
    }
}