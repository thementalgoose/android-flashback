package tmg.flashback.rss.network.model;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Keep
@Root(name = "item", strict = false)
public class RssXMLModelItem {
    @Element
    public String title;

    @Element(required = false)
    @Nullable
    public String description;
    
    @Element
    public String link;

    @Element
    public String pubDate;
}
