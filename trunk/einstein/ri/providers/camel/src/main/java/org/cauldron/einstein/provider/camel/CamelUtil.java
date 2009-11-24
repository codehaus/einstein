package org.cauldron.einstein.provider.camel;

import org.apache.camel.Exchange;
import org.cauldron.einstein.api.message.Message;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.model.profile.Profile;
import org.cauldron.einstein.ri.core.log.Logger;

import java.util.Map;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class CamelUtil {
    private static final Logger log = Logger.getLogger(CamelUtil.class);
    public static Message extractMessage(Exchange exchange, MessageTuple messageTuple, Profile profile) {
        Object payload = exchange.getIn().getBody();
        if(payload == null) {
            return profile.getMessageModel().createVoidMessage(messageTuple.getExecutionCorrelation());
        }
        log.debug("Extracting camel message {0}.",payload.toString());
        DataObject dataObject = profile.getDataModel().getDataObjectFactory().createDataObject(payload);
        Map<String, Object> stringObjectMap = exchange.getIn().getHeaders();

        return profile.getMessageModel().createMessage(messageTuple.getExecutionCorrelation(), profile.getMessageModel().createProperties(stringObjectMap), dataObject);
    }

    public static Message extractReturnMessage(Exchange exchange, MessageTuple messageTuple, Profile profile) {
        if(exchange.getOut().getBody() == null) {
            return profile.getMessageModel().createVoidMessage(messageTuple.getExecutionCorrelation());
        }
        Object payload = exchange.getOut().getBody();
        DataObject dataObject = profile.getDataModel().getDataObjectFactory().createDataObject(payload);
        Map<String, Object> stringObjectMap = exchange.getIn().getHeaders();

        return profile.getMessageModel().createMessage(messageTuple.getExecutionCorrelation(), profile.getMessageModel().createProperties(stringObjectMap), dataObject);
    }


}
