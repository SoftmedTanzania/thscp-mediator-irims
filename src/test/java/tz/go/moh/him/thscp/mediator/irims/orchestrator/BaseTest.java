package tz.go.moh.him.thscp.mediator.irims.orchestrator;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.RegistrationConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.testing.TestingUtils;
import tz.go.moh.him.thscp.mediator.irims.MediatorMain;
import tz.go.moh.him.thscp.mediator.irims.mock.CustomMockLauncher;
import tz.go.moh.him.thscp.mediator.irims.mock.MockDestination;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class BaseTest {
    /**
     * Represents the system actor.
     */
    protected static ActorSystem system;

    /**
     * Represents the configuration.
     */
    protected MediatorConfig testConfig;

    /**
     * Represents an Error Messages Definition Resource Object defined in <a href="file:../resources/error-messages.json">/resources/error-messages.json</a>.
     */
    protected JSONObject errorMessageResource;

    /**
     * Runs initialization before each class execution.
     */
    @Before
    public void before() throws Exception {
        system = ActorSystem.create();

        testConfig = new MediatorConfig();
        testConfig.setName("thscp-mediator-irims-tests");
        testConfig.setProperties("mediator-unit-test.properties");

        InputStream regInfo = MediatorMain.class.getClassLoader().getResourceAsStream("mediator-registration-info.json");
        RegistrationConfig regConfig = null;
        if (regInfo != null) {
            regConfig = new RegistrationConfig(regInfo);
        }

        testConfig.setRegistrationConfig(regConfig);

        InputStream stream = getClass().getClassLoader().getResourceAsStream("error-messages.json");
        if (stream != null) {
            errorMessageResource = new JSONObject(IOUtils.toString(stream));
        }
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
     * Method for initiating actors, creating requests and sending request to the actor.
     *
     * @param system     the actor system used to initialize the destination actor
     * @param testConfig the configuration used
     * @param sender     the sending actor
     * @param payload    the payload
     * @param type       class type of the destination orchestrator
     * @param path       to send the request
     */
    public void createActorAndSendRequest(ActorSystem system, MediatorConfig testConfig, ActorRef sender, String payload, Class<?> type, String path) {
        final ActorRef orchestratorActor = system.actorOf(Props.create(type, testConfig));
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/json");
        MediatorHTTPRequest POST_Request = new MediatorHTTPRequest(
                sender,
                sender,
                "unit-test",
                "POST",
                "http",
                null,
                null,
                path,
                payload,
                headers,
                Collections.<Pair<String, String>>emptyList()
        );

        orchestratorActor.tell(POST_Request, sender);
    }

    /**
     * Sets up the hdr mock actor with the expected message type
     *
     * @param expectedMessageType to be verified by the hdr mock in the received request body
     */
    protected void setupDestinationMock(String expectedMessageType) {
        system.actorOf(Props.create(CustomMockLauncher.class, MockDestination.class, expectedMessageType, "http-connector"), testConfig.getName());
    }
}
