package tmg.flashback.rss.network.apis.model;

import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

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
