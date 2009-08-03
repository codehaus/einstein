package org.cauldron.einstein.ri.examples;


import org.w3c.dom.Node;
import org.cauldron.einstein.ri.core.log.Logger;

import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.URLDescriptor;
import javax.media.jai.remote.SerializableRenderedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class ImageMaker {
    private static final Logger log = Logger.getLogger(ImageMaker.class);

    public RenderedImage convert(Node node) throws MalformedURLException {
        log.debug("Received {0}.", node);

        if (node == null || node.getNodeValue() == null) {
            return null;
        }
        
        String image = node.getNodeValue();
        log.debug("Processing image: {0}.", image);
        RenderedOp op = URLDescriptor.create(new URL(image), null, null);
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(op);
        pb.add((float) Constants.WIDTH / op.getWidth());
        pb.add((float) Constants.HEIGHT / op.getHeight());
        pb.add(0.0F);
        pb.add(0.0F);
        pb.add(new InterpolationNearest());
        RenderedOp src = JAI.create("scale", pb);
        SerializableRenderedImage result = new SerializableRenderedImage(src.getRendering());
        int numBands = src.getNumBands();
        if (numBands == 3) {
            return result;
        } else {
            return null;
        }

    }


}