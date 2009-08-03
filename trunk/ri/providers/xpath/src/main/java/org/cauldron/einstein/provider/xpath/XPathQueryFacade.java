package org.cauldron.einstein.provider.xpath;

import org.apache.commons.jxpath.JXPathContext;
import org.cauldron.einstein.api.common.euri.EinsteinURI;
import org.cauldron.einstein.api.message.data.model.DataList;
import org.cauldron.einstein.api.message.data.model.DataObject;
import org.cauldron.einstein.api.provider.facade.BooleanQueryFacade;
import org.cauldron.einstein.api.provider.facade.QueryFacade;
import org.cauldron.einstein.ri.core.log.Logger;
import org.cauldron.einstein.ri.core.providers.AbstractFacade;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class XPathQueryFacade extends AbstractFacade implements BooleanQueryFacade, QueryFacade {
    private static final Logger log = Logger.getLogger(XPathQueryFacade.class);
    private final EinsteinURI uri;

    public XPathQueryFacade(EinsteinURI uri) {
        this.uri = uri;
    }

    public DataObject selectSingle(DataObject o) {
        log.debug("Splitting {0} with {1}.", o, uri);
        if (o.getQueryObject().supportsQuery(uri)) {
            return o.getQueryObject().selectSingle(uri);
        } else {
            JXPathContext context = JXPathContext.newContext(o.getValue());
            return o.getDataModel().getDataObjectFactory().createDataObject(context.selectSingleNode(uri.getDescriptor().asString()));
        }
    }

    public DataList selectMultiple(DataObject o) {
        log.debug("Splitting {0} with {1}.", o, uri);
        if (o.getQueryObject().supportsQuery(uri)) {
            return o.getQueryObject().selectMultiple(uri);
        } else {
            JXPathContext context = JXPathContext.newContext(o.getValue());
            return o.getDataModel().getDataObjectFactory().createDataObject(context.selectSingleNode(uri.getDescriptor().asString())).asList();
        }
    }

    public boolean match(DataObject o) {
        log.debug("Matching {0} against {1}.", o, uri);
        if (o == null) {
            return false;
        }
        if (o.getQueryObject().supportsQuery(uri)) {
            return o.getQueryObject().matches(uri);
        } else {
            JXPathContext context = JXPathContext.newContext(o.getValue());
            return context.selectSingleNode(uri.getDescriptor().asString()) != null;
        }
    }


}
