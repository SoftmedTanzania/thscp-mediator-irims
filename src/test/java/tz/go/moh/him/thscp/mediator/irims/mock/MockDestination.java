package tz.go.moh.him.thscp.mediator.irims.mock;
import tz.go.moh.him.mediator.core.serialization.JsonSerializer;
import org.junit.Assert;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.testing.MockHTTPConnector;
import tz.go.moh.him.thscp.mediator.irims.domain.IRIMSRequest;
import tz.go.moh.him.thscp.mediator.irims.orchestrator.ProductRecallOrchestratorTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;

/**
 * Represents a mock destination.
 */
public class MockDestination extends MockHTTPConnector {

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

        JsonSerializer serializer = new JsonSerializer();

        List<IRIMSRequest> expected;

        try {
            expected = Arrays.asList(serializer.deserialize(IOUtils.toByteArray(stream), IRIMSRequest[].class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<IRIMSRequest> actual = Arrays.asList(serializer.deserialize(msg.getBody(), IRIMSRequest[].class));

        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);

        Assert.assertEquals(1, actual.size());
        Assert.assertEquals(expected.size(), actual.size());

        Assert.assertEquals(expected.get(0).getUuid(), actual.get(0).getUuid());
        Assert.assertEquals(expected.get(0).getActionRequired(), actual.get(0).getActionRequired());
        Assert.assertEquals(expected.get(0).getAffectedCommunity(), actual.get(0).getAffectedCommunity());
        Assert.assertEquals(expected.get(0).getBatchNumber(), actual.get(0).getBatchNumber());
        Assert.assertEquals(expected.get(0).getClosureDate(), actual.get(0).getClosureDate());
        Assert.assertEquals(expected.get(0).getDescription(), actual.get(0).getDescription());
        Assert.assertEquals(expected.get(0).getDistributedQuantity(), actual.get(0).getDistributedQuantity());
        Assert.assertEquals(expected.get(0).getIssue(), actual.get(0).getIssue());
        Assert.assertEquals(expected.get(0).getRecallDate(), actual.get(0).getRecallDate());
        Assert.assertEquals(expected.get(0).getRecallFrequency(), actual.get(0).getRecallFrequency());
        Assert.assertEquals(expected.get(0).getRecalledQuantity(), actual.get(0).getRecalledQuantity());
        Assert.assertEquals(expected.get(0).getReceivedQuantity(), actual.get(0).getReceivedQuantity());
        Assert.assertEquals(expected.get(0).getStartDate(), actual.get(0).getStartDate());
        Assert.assertEquals(expected.get(0).getUnit(), actual.get(0).getUnit());



    }
}