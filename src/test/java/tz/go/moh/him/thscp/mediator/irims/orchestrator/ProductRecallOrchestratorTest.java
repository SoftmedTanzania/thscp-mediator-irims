package tz.go.moh.him.thscp.mediator.irims.orchestrator;

import akka.testkit.JavaTestKit;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.testing.MockLauncher;
import org.openhim.mediator.engine.testing.TestingUtils;
import tz.go.moh.him.thscp.mediator.irims.mock.MockDestination;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProductRecallOrchestratorTest extends BaseTest{

    /**
     * Represents an Error Messages Definition Resource Object defined in <a href="file:../resources/error-messages.json">/resources/error-messages.json</a>.
     */
    protected JSONObject thscpErrorMessageResource;

    /**
     * Runs initialization before each class execution.
     */
    @Override
    public void before() throws Exception {
        super.before();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("error-messages.json");
        try {
            if (stream != null) {
                thscpErrorMessageResource = new JSONObject(IOUtils.toString(stream));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<MockLauncher.ActorToLaunch> toLaunch = new LinkedList<>();
        toLaunch.add(new MockLauncher.ActorToLaunch("http-connector", MockDestination.class));
        TestingUtils.launchActors(system, testConfig.getName(), toLaunch);
    }

    /**
     * Runs cleanup after class execution.
     */
    @After
    public void after() {
        TestingUtils.clearRootContext(system, testConfig.getName());
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    /**
     * Tests the mediator.
     *
     * @throws Exception if an exception occurs
     */
    @Test
    public void testMediatorHTTPRequest() throws Exception {
        assertNotNull(testConfig);
        new JavaTestKit(system) {{
            InputStream stream = ProductRecallOrchestratorTest.class.getClassLoader().getResourceAsStream("product-recall-request.json");

            assertNotNull(stream);


            createActorAndSendRequest(system, testConfig, getRef(), IOUtils.toString(stream), ProductRecallOrchestrator.class, "/thscp-product-recall");

            final Object[] out =
                    new ReceiveWhile<Object>(Object.class, duration("1 second")) {
                        @Override
                        protected Object match(Object msg) throws Exception {
                            if (msg instanceof FinishRequest) {
                                return msg;
                            }
                            throw noMatch();
                        }
                    }.get();

            boolean foundResponse = false;

            for (Object o : out) {
                if (o instanceof FinishRequest) {
                    foundResponse = true;
                    break;
                }
            }
            assertNotNull(foundResponse);
        }};
    }

    /**
     * Tests the mediator with payload with invalid payload
     *
     * @throws Exception if an exception occurs
     */
    @Test
    public void testInValidPayload() throws Exception {
        assertNotNull(testConfig);

        new JavaTestKit(system) {{
            InputStream stream = ProductRecallOrchestratorTest.class.getClassLoader().getResourceAsStream("invalid-product-recall-request.json");

            assertNotNull(stream);

            createActorAndSendRequest(system, testConfig, getRef(), IOUtils.toString(stream), ProductRecallOrchestrator.class, "/thscp-product-recall");

            final Object[] out =
                    new ReceiveWhile<Object>(Object.class, duration("1 second")) {
                        @Override
                        protected Object match(Object msg) throws Exception {
                            if (msg instanceof FinishRequest) {
                                return msg;
                            }
                            throw noMatch();
                        }
                    }.get();

            int responseStatus = 0;
            String responseMessage = "";

            for (Object o : out) {
                if (o instanceof FinishRequest) {
                    responseStatus = ((FinishRequest) o).getResponseStatus();
                    responseMessage = ((FinishRequest) o).getResponse();
                    break;
                }
            }

            assertEquals(400, responseStatus);
            assertTrue(responseMessage.contains(String.format(thscpErrorMessageResource.getString("GENERIC_ERR"),"uuid")));
            assertTrue(responseMessage.contains(String.format(thscpErrorMessageResource.getString("GENERIC_ERR"), "actionRequired")));
        }};
    }

    @Test
    public void testInvalidDates() throws Exception {

        assertNotNull(testConfig);

        new JavaTestKit(system) {{
            InputStream stream = ProductRecallOrchestratorTest.class.getClassLoader().getResourceAsStream("invalid-dates-product-recall-request.json");

            assertNotNull(stream);

            createActorAndSendRequest(system, testConfig, getRef(), IOUtils.toString(stream), ProductRecallOrchestrator.class, "/thscp-product-recall");

            final Object[] out =
                    new ReceiveWhile<Object>(Object.class, duration("1 second")) {
                        @Override
                        protected Object match(Object msg) throws Exception {
                            if (msg instanceof FinishRequest) {
                                return msg;
                            }
                            throw noMatch();
                        }
                    }.get();

            int responseStatus = 0;
            String responseMessage = "";

            for (Object o : out) {
                if (o instanceof FinishRequest) {
                    responseStatus = ((FinishRequest) o).getResponseStatus();
                    responseMessage = ((FinishRequest) o).getResponse();
                    break;
                }
            }

            assertEquals(400, responseStatus);
            assertTrue(responseMessage.contains(String.format(String.format(thscpErrorMessageResource.getString("ERROR_DATE_IS_NOT_VALID_PAST_DATE"),"recallDate"), "2022-05-05")));
        }};

    }
}
