package tz.go.moh.him.thscp.mediator.irims.orchestrator;

import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.gson.Gson;
import org.apache.http.HttpStatus;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import tz.go.moh.him.thscp.mediator.irims.domain.iRIMSResponse;


public class DefaultOrchestrator extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

//    private final MediatorConfig config;
    final MediatorConfig config;


    public DefaultOrchestrator(MediatorConfig config) {
        this.config = config;
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof MediatorHTTPRequest) {
            FinishRequest finishRequest = null;
            try {

                iRIMSResponse irimsResponse = new iRIMSResponse(HttpStatus.SC_OK, "Success");
                log.info("irims response is " + irimsResponse);

                finishRequest = new FinishRequest(new Gson().toJson(irimsResponse), "text/json", HttpStatus.SC_OK);
            } catch (Exception e) {
                iRIMSResponse irimsResponse = new iRIMSResponse(HttpStatus.SC_BAD_REQUEST, "Failed");
                finishRequest = new FinishRequest(new Gson().toJson(irimsResponse), "text/json", HttpStatus.SC_BAD_REQUEST);
            } finally {
                ((MediatorHTTPRequest) msg).getRequestHandler().tell(finishRequest, getSelf());
            }
        } else {
            unhandled(msg);
        }
    }
}
