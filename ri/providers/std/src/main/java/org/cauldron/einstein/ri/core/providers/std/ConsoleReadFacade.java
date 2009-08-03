package org.cauldron.einstein.ri.core.providers.std;

import jline.ConsoleReader;

import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.message.MessageListener;
import org.cauldron.einstein.api.message.MessageTuple;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.model.lifecycle.LifecycleContext;
import org.cauldron.einstein.api.model.lifecycle.exception.StartRuntimeException;
import org.cauldron.einstein.api.model.resource.ResourceRef;
import org.cauldron.einstein.api.provider.facade.PollFacade;
import org.cauldron.einstein.api.provider.facade.ReadFacade;
import org.cauldron.einstein.api.provider.facade.exception.ReadFailureRuntimeException;
import org.cauldron.einstein.api.provider.facade.context.ReadContext;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class ConsoleReadFacade extends AbstractFacade implements ReadFacade, PollFacade {

    private final ConsoleReader reader;
    private final String prompt;
    private final Character mask;
    private boolean listenerActive = false;

    public ConsoleReadFacade(EinsteinURI uri) {
        try {
            this.reader = new ConsoleReader();
        } catch (IOException e) {
            throw new ReadFailureRuntimeException(e);
        }
        this.prompt = uri.getDescriptor().asString();
        Properties properties = uri.getProviderMetadata().asProperties();
        if (properties.containsKey("mask")) {
            mask = properties.getProperty("mask").charAt(0);
        } else {
            mask = null;
        }
        reader.setDefaultPrompt(prompt);
    }


    public MessageTuple read( ReadContext readContext, boolean all, boolean payload, long l) throws ReadFailureRuntimeException {
        return readInternal(readContext, all);
    }

    private MessageTuple readInternal(ReadContext context, boolean all) {
        if (all) {
            List<MessageTuple> result = new ArrayList<MessageTuple>();
            try {
                while (true) {
                    String s = readLine();
                    if (s == null) {
                        break;
                    }
                    DataObject object = context.getActiveProfile().getDataModel().getDataObjectFactory().createDataObject(s);
                    result.add(context.getActiveProfile().getMessageModel().createMessage(context.getMessage().getExecutionCorrelation(), object));

                }
            } catch (IOException e) {
                throw new ReadFailureRuntimeException(e);
            }
            return context.getActiveProfile().getMessageModel().createTuple(context.getActiveProfile().getDataModel(), result);
        } else {

            try {
                String s = readLine();
                DataObject object = context.getActiveProfile().getDataModel().getDataObjectFactory().createDataObject(s);
                return context.getActiveProfile().getMessageModel().createMessage(context.getMessage().getExecutionCorrelation(), object);
            } catch (IOException e) {
                throw new ReadFailureRuntimeException(e);
            }
        }
    }

    private String readLine() throws IOException {
        String s;
        if (mask == null) {
            s = reader.readLine(prompt);
        } else {
            s = reader.readLine(prompt, mask);
        }
        return s;
    }


    public MessageTuple read( ReadContext readContext, boolean all, boolean payload) throws ReadFailureRuntimeException {
        return readInternal(readContext, all);
    }


    public MessageTuple readNoWait( ReadContext readContext, boolean all, boolean payload) throws ReadFailureRuntimeException {
        return readContext.getActiveProfile().getMessageModel().createEmptyMessage(readContext.getMessage().getExecutionCorrelation());
    }

    public void poll( ReadContext readContext, ResourceRef resourceRef, boolean message,  MessageListener messageListener) throws ReadFailureRuntimeException {

        try {
            while (listenerActive) {
                String s;
                s = readLine();
                if (s == null) {
                    break;
                }
                DataObject object = readContext.getActiveProfile().getDataModel().getDataObjectFactory().createDataObject(s);
                messageListener.handle(readContext.getActiveProfile().getMessageModel().createMessage(readContext.getMessage().getExecutionCorrelation(), object));

            }
        } catch (IOException e) {
            throw new ReadFailureRuntimeException(e);
        }
    }

    @Override
    public void stop(LifecycleContext ctx) throws StartRuntimeException {
        super.stop(ctx);
        listenerActive = false;

    }

    @Override
    public void start(LifecycleContext ctx) {
        super.start(ctx);
        listenerActive = true;
    }
}
