package tz.go.moh.him.thscp.mediator.irims.orchestrator;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import tz.go.moh.him.mediator.core.domain.ErrorMessage;
import tz.go.moh.him.mediator.core.serialization.JsonSerializer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class BaseOrchestrator extends UntypedActor{

    /**
     * The serializer
     */
    public static final JsonSerializer serializer = new JsonSerializer();
    /**
     * The logger instance.
     */
    public final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    /**
     * The mediator configuration.
     */
    protected final MediatorConfig config;

    /**
     * Represents a mediator request.
     */
    protected MediatorHTTPRequest workingRequest;

    protected JSONObject errorMessageResource;

    protected SimpleDateFormat thscpDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * Represents a list of error messages, if any,that have been caught during payload data validation to be returned to the source system as response.
     */
    protected List<ErrorMessage> errorMessages = new ArrayList<>();

    /**
     * Handles the received message.
     *
     * @param msg The received message.
     */

    /**
     * Initializes a new instance of the {@link BaseOrchestrator} class.
     *
     * @param config The mediator configuration.
     */
    public BaseOrchestrator(MediatorConfig config) {
        this.config = config;
        InputStream stream = getClass().getClassLoader().getResourceAsStream("error-messages.json");
        try {
            if (stream != null) {
                errorMessageResource = new JSONObject(IOUtils.toString(stream));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String checkDateFormatStrings(String dateString) {
        List<String> formatStrings = Arrays.asList("yyyy-MM-dd HH:mm:ss:ms", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyyMMdd");
        for (String formatString : formatStrings) {
            try {
                new SimpleDateFormat(formatString).parse(dateString);
                return formatString;
            } catch (ParseException e) {
                //  e.printStackTrace();
            }
        }

        return "";
    }


    /**
     * Handle sending of data to thscp
     *
     * @param msg to be sent
     */
    public void sendDataToThscp(String msg) {
        if (!errorMessages.isEmpty()) {
            FinishRequest finishRequest = new FinishRequest(new Gson().toJson(errorMessages), "text/json", HttpStatus.SC_BAD_REQUEST);
            (workingRequest).getRequestHandler().tell(finishRequest, getSelf());
        } else {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");

            String scheme;
            String host;
            String path;
            int portNumber;

            if (config.getDynamicConfig().isEmpty()) {
                if (config.getProperty("destination.secure").equals("true")) {
                    scheme = "https";
                } else {
                    scheme = "http";
                }

                host = config.getProperty("destination.host");
                portNumber = Integer.parseInt(config.getProperty("destination.api.port"));
                path = config.getProperty("destination.api.path");
            } else {
                JSONObject connectionProperties = new JSONObject(config.getDynamicConfig()).getJSONObject("destinationConnectionProperties");

                if (!connectionProperties.getString("destinationUsername").isEmpty() && !connectionProperties.getString("destinationPassword").isEmpty()) {
                    String auth = connectionProperties.getString("destinationUsername") + ":" + connectionProperties.getString("destinationPassword");
                    byte[] encodedAuth = Base64.encodeBase64(
                            auth.getBytes(StandardCharsets.ISO_8859_1));
                    String authHeader = "Basic " + new String(encodedAuth);
                    headers.put(HttpHeaders.AUTHORIZATION, authHeader);
                }

                host = connectionProperties.getString("destinationHost");
                portNumber = connectionProperties.getInt("destinationPort");
                path = connectionProperties.getString("destinationPath");
                scheme = connectionProperties.getString("destinationScheme");
            }

            List<Pair<String, String>> params = new ArrayList<>();

            MediatorHTTPRequest forwardToThscpRequest = new MediatorHTTPRequest(
                    (workingRequest).getRequestHandler(), getSelf(), "Sending recall data to thscp", "POST", scheme,
                    host, portNumber, path, msg, headers, params
            );

            ActorSelection httpConnector = getContext().actorSelection(config.userPathFor("http-connector"));
            httpConnector.tell(forwardToThscpRequest, getSelf());
        }
    }

    
}
