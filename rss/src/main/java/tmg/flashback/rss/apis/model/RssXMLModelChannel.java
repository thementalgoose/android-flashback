package tmg.flashback.rss.apis.model;

import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;

@Root(name = "channel", strict = false)
public class RssXMLModelChannel {
    @Element
    public String title;

    @Path(value = "link")
    @Text(required = false)
    @Nullable
    public String link;

    @ElementList(name = "item", type = RssXMLModelItem.class, inline = true, required = false)
    public List<RssXMLModelItem> item;
}
