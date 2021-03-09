package tz.go.moh.him.thscp.mediator.irims.orchestrator;


import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.codehaus.plexus.util.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpHeaders;
import org.json.JSONObject;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;
import tz.go.moh.him.mediator.core.adapter.CsvAdapterUtils;
import tz.go.moh.him.mediator.core.domain.ErrorMessage;
import tz.go.moh.him.mediator.core.domain.ResultDetail;
import tz.go.moh.him.thscp.mediator.irims.domain.iRIMSRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.lang.reflect.Type;

public class ProductRecallOrchestrator extends UntypedActor{
    /**
     * The logger instance.
     */
    protected final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    /**
     * The mediator configuration.
     */
    protected final MediatorConfig config;

    /**
     * Represents a mediator request.
     */
    protected MediatorHTTPRequest workingRequest;

    protected JSONObject errorMessageResource;

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
     * Initializes a new instance of the {@link ProductRecallOrchestrator} class.
     *
     * @param config The mediator configuration.
     */
    public ProductRecallOrchestrator(MediatorConfig config) {
        this.config = config;
        InputStream stream = getClass().getClassLoader().getResourceAsStream("error-messages.json");
        try {
            if (stream != null) {
                errorMessageResource = new JSONObject(IOUtils.toString(stream)).getJSONObject("PRODUCT_RECALL_ERROR_MESSAGES");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles data validations
     *
     * @param receivedList The object to be validated
     */
    protected List<iRIMSRequest> validateData(List<iRIMSRequest> receivedList) {
        List<iRIMSRequest> validReceivedList = new ArrayList<>();

        for (iRIMSRequest irimsRequest : receivedList) {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setSource(new Gson().toJson(irimsRequest));

            List<ResultDetail> resultDetailsList = new ArrayList<>();

            if (irimsRequest == null) {
                resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, errorMessageResource.getString("ERROR_INVALID_PAYLOAD"), null));
            } else {
                resultDetailsList.addAll(validateRequiredFields(irimsRequest));
            }

            //TODO implement additional data validations checks
            if (resultDetailsList.size() == 0) {
                //No errors were found during data validation
                //adding the service received to the valid payload to be sent to HDR
                validReceivedList.add(irimsRequest);
            } else {
                //Adding the validation results to the Error message object
                errorMessage.setResultsDetails(resultDetailsList);
                errorMessages.add(errorMessage);
            }
        }
        return validReceivedList;
    }

    /**
     * Validate iRIMS Request Required Fields
     *
     * @param irimsRequest to be validated
     * @return array list of validation results details for failed validations
     */
    public List<ResultDetail> validateRequiredFields(iRIMSRequest irimsRequest) {
        List<ResultDetail> resultDetailsList = new ArrayList<>();

        if (StringUtils.isBlank(irimsRequest.getUuid()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, errorMessageResource.getString("UUID_IS_BLANK"), null));

        if (StringUtils.isBlank(irimsRequest.getActionRequired()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, errorMessageResource.getString("ACTION_REQUIRED_IS_BLANK"), null));

        if (StringUtils.isBlank(irimsRequest.getAffectedCommunity()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, errorMessageResource.getString("AFFECTED_COMMUNITY_IS_BLANK"), null));

        if (StringUtils.isBlank(irimsRequest.getBatchNumber()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, errorMessageResource.getString("BATCH_NUMBER_IS_BLANK"), null));

        if (StringUtils.isBlank(irimsRequest.getClosureDate()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, errorMessageResource.getString("CLOSURE_DATE_IS_BLANK"), null));

        if (StringUtils.isBlank(irimsRequest.getDescription()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, errorMessageResource.getString("DESCRIPTION_IS_BLANK"), null));

        if (StringUtils.isBlank(String.valueOf(irimsRequest.getDistributedQuantity())))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, errorMessageResource.getString("DISTRIBUTED_QUANTITY_IS_BLANK"), null));

        if (StringUtils.isBlank(irimsRequest.getIssue()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, errorMessageResource.getString("ISSUE_IS_BLANK"), null));

        if (StringUtils.isBlank(irimsRequest.getRecallDate()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, errorMessageResource.getString("RECALL_DATE_IS_BLANK"), null));

        if (StringUtils.isBlank(String.valueOf(irimsRequest.getRecallFrequency())))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, errorMessageResource.getString("RECALL_FREQUENCY_IS_BLANK"), null));

        if (StringUtils.isBlank(String.valueOf(irimsRequest.getRecalledQuantity())))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, errorMessageResource.getString("RECALLED_QUANTITY_IS_BLANK"), null));

        if (StringUtils.isBlank(String.valueOf(irimsRequest.getStartDate())))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, errorMessageResource.getString("START_DATE_IS_BLANK"), null));

        if (StringUtils.isBlank(irimsRequest.getUnit()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, errorMessageResource.getString("UNIT_IS_BLANK"), null));

        return resultDetailsList;
    }

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof MediatorHTTPRequest)
        {
            workingRequest = (MediatorHTTPRequest) msg;

            log.info("Received request: " + workingRequest.getHost() + " " + workingRequest.getMethod() + " " + workingRequest.getPath());

            //Converting the received request body to POJO List
            List<iRIMSRequest> irimsRequestList = new ArrayList<>();
            try {
                irimsRequestList = convertMessageBodyToPojoList(((MediatorHTTPRequest) msg).getBody());
            } catch (Exception e) {
                //In-case of an exception creating an error message with the stack trace
                ErrorMessage errorMessage = new ErrorMessage(
                        workingRequest.getBody(),
                        Arrays.asList(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, e.getMessage(), tz.go.moh.him.mediator.core.utils.StringUtils.writeStackTraceToString(e)))
                );
                errorMessages.add(errorMessage);
            }

            log.info("Received payload in JSON = " + new Gson().toJson(irimsRequestList));

            List<iRIMSRequest> validatedObjects;
            if (irimsRequestList.isEmpty()) {
                ErrorMessage errorMessage = new ErrorMessage(
                        workingRequest.getBody(),
                        Arrays.asList(
                                new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, errorMessageResource.getString("ERROR_INVALID_PAYLOAD"), null)
                        )
                );
                errorMessages.add(errorMessage);
                validatedObjects = new ArrayList<>();
            } else {
                validatedObjects = validateData(irimsRequestList);
            }

            log.info("validated object is" +new Gson().toJson(validatedObjects));

            sendDataToThscp(new Gson().toJson(validatedObjects));
        } else if (msg instanceof MediatorHTTPResponse) { //respond
            log.info("Received response from thscp");
            (workingRequest).getRequestHandler().tell(((MediatorHTTPResponse) msg).toFinishRequest(), getSelf());
        } else {
            unhandled(msg);
        }
    }

    /**
     * Handle sending of data to thscp
     *
     * @param msg to be sent
     */
    private void sendDataToThscp(String msg) {
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
                if (config.getProperty("thscp.secure").equals("true")) {
                    scheme = "https";
                } else {
                    scheme = "http";
                }

                host = config.getProperty("thscp.host");
                portNumber = Integer.parseInt(config.getProperty("thscp.api.port"));
                path = config.getProperty("thscp.api.path");
            } else {
                JSONObject connectionProperties = new JSONObject(config.getDynamicConfig()).getJSONObject("thscpConnectionProperties");

                if (!connectionProperties.getString("thscpUsername").isEmpty() && !connectionProperties.getString("thscpPassword").isEmpty()) {
                    String auth = connectionProperties.getString("thscpUsername") + ":" + connectionProperties.getString("thscpPassword");
                    byte[] encodedAuth = Base64.encodeBase64(
                            auth.getBytes(StandardCharsets.ISO_8859_1));
                    String authHeader = "Basic " + new String(encodedAuth);
                    headers.put(HttpHeaders.AUTHORIZATION, authHeader);
                }

                host = connectionProperties.getString("thscpHost");
                portNumber = connectionProperties.getInt("thscpPort");
                path = connectionProperties.getString("thscpPath");
                scheme = connectionProperties.getString("thscpScheme");
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

    protected List<iRIMSRequest> convertMessageBodyToPojoList(String msg) throws JsonSyntaxException {
        List<iRIMSRequest> irimsRequestList;

        Type listType = new TypeToken<List<iRIMSRequest>>() {
        }.getType();
        irimsRequestList = new Gson().fromJson((workingRequest).getBody(), listType);

        return irimsRequestList;
    }


}
