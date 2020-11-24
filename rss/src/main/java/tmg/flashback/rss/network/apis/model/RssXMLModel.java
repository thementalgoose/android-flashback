package tmg.flashback.rss.network.apis.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Needs to be declared in Java because kotlin weirdness with classes and
 * exposing the correct formats for the reflection to work properly
 *
 * Look into converting this to a kotlin object at some point maybe
 */
@Root(name = "rss", strict = false)
public class RssXMLModel {
    @Element
    public RssXMLModelChannel channel;
}
