package tmg.flashback.rss.network.apis.model;

import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "item", strict = false)
public class RssXMLModelItem {
    @Element
    public String title;

    @Element
    @Nullable
    public String description;
    
    @Element
    public String link;

    @Element
    public String pubDate;
}
