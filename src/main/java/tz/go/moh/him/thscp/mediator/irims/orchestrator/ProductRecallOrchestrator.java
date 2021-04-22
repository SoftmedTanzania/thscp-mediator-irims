package tz.go.moh.him.thscp.mediator.irims.orchestrator;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.codehaus.plexus.util.StringUtils;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;
import tz.go.moh.him.mediator.core.domain.ErrorMessage;
import tz.go.moh.him.mediator.core.domain.ResultDetail;
import tz.go.moh.him.mediator.core.validator.DateValidatorUtils;
import tz.go.moh.him.thscp.mediator.irims.domain.ProductRecallRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductRecallOrchestrator extends BaseOrchestrator{

    /**
     * Initializes a new instance of the {@link ProductRecallOrchestrator} class.
     *
     * @param config The mediator configuration.
     */
    public ProductRecallOrchestrator(MediatorConfig config) {
        super(config);
    }

    /**
     * Handles data validations
     *
     * @param receivedList The object to be validated
     */
    protected List<ProductRecallRequest> validateData(List<ProductRecallRequest> receivedList) {
        List<ProductRecallRequest> validReceivedList = new ArrayList<>();

        for (ProductRecallRequest irimsRequest : receivedList) {
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
    public List<ResultDetail> validateRequiredFields(ProductRecallRequest irimsRequest) {
        List<ResultDetail> resultDetailsList = new ArrayList<>();

        if (StringUtils.isBlank(irimsRequest.getUuid()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("GENERIC_ERR"),"uuid"), null));

        if (StringUtils.isBlank(irimsRequest.getActionRequired()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("GENERIC_ERR"),"actionRequired"), null));

        if (StringUtils.isBlank(irimsRequest.getAffectedCommunity()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("GENERIC_ERR"),"affectedCommunity"), null));

        if (StringUtils.isBlank(irimsRequest.getBatchNumber()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("GENERIC_ERR"),"batchNumber"), null));

        if (StringUtils.isBlank(irimsRequest.getClosureDate()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("GENERIC_ERR"),"closureDate"), null));

        if (StringUtils.isBlank(irimsRequest.getDescription()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("GENERIC_ERR"),"description"), null));

        if (StringUtils.isBlank(String.valueOf(irimsRequest.getDistributedQuantity())))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("GENERIC_ERR"),"distributedQuantity"), null));

        if (StringUtils.isBlank(irimsRequest.getIssue()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("GENERIC_ERR"),"issue"), null));

        if (StringUtils.isBlank(irimsRequest.getRecallDate()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("GENERIC_ERR"),"recalldate"), null));

        if (StringUtils.isBlank(String.valueOf(irimsRequest.getRecallFrequency())))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("GENERIC_ERR"),"recallFrequency"), null));

        if (StringUtils.isBlank(String.valueOf(irimsRequest.getRecalledQuantity())))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("GENERIC_ERR"),"recalledQuantity"), null));

        if (StringUtils.isBlank(String.valueOf(irimsRequest.getStartDate())))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("GENERIC_ERR"),"startDate"), null));

        if (StringUtils.isBlank(irimsRequest.getUnit()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("GENERIC_ERR"),"unit"), null));

        if (StringUtils.isBlank(irimsRequest.getProductCode()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("GENERIC_ERR"),"productCode"), null));

        if (StringUtils.isBlank(irimsRequest.getRecallOrganization()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("GENERIC_ERR"),"getRecallOrganization"), null));

        if (StringUtils.isBlank(irimsRequest.getRegion()))
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("GENERIC_ERR"),"region"), null));

        try {
            if (!DateValidatorUtils.isValidPastDate(irimsRequest.getRecallDate(), checkDateFormatStrings(irimsRequest.getRecallDate()))) {
                resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("ERROR_DATE_IS_NOT_VALID_PAST_DATE"),"recallDate"), null));
            }
            else{
                SimpleDateFormat irimsDateFormat = new SimpleDateFormat(checkDateFormatStrings(irimsRequest.getRecallDate()));
                irimsRequest.setRecallDate(thscpDateFormat.format(irimsDateFormat.parse(irimsRequest.getRecallDate())));

            }
        } catch (ParseException e) {
            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("ERROR_INVALID_DATE_FORMAT"),"recallDate"),null));
        }

//        try {
//            if (!DateValidatorUtils.isValidPastDate(irimsRequest.getStartDate(), checkDateFormatStrings(irimsRequest.getStartDate()))) {
//                resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("ERROR_DATE_IS_NOT_VALID_PAST_DATE"),"startDate"), null));
//            }
//            else{
//                SimpleDateFormat irimsDateFormat = new SimpleDateFormat(checkDateFormatStrings(irimsRequest.getStartDate()));
//                irimsRequest.setStartDate(thscpDateFormat.format(irimsDateFormat.parse(irimsRequest.getStartDate())));
//            }
//        } catch (ParseException e) {
//            resultDetailsList.add(new ResultDetail(ResultDetail.ResultsDetailsType.ERROR, String.format(errorMessageResource.getString("ERROR_INVALID_DATE_FORMAT"),"startDate"),null));
//        }

        log.info("results are" +resultDetailsList);
        return resultDetailsList;
    }

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof MediatorHTTPRequest)
        {
            workingRequest = (MediatorHTTPRequest) msg;

            log.info("Received request: " + workingRequest.getHost() + " " + workingRequest.getMethod() + " " + workingRequest.getPath());

            //Converting the received request body to POJO List
            List<ProductRecallRequest> irimsRequestList = new ArrayList<>();
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

            List<ProductRecallRequest> validatedObjects;
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
    protected List<ProductRecallRequest> convertMessageBodyToPojoList(String msg) throws JsonSyntaxException {
        List<ProductRecallRequest> productRecallRequestList = Arrays.asList(serializer.deserialize(msg, ProductRecallRequest[].class));
        return productRecallRequestList;
    }


}

