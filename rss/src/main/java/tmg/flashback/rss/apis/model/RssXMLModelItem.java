package tmg.flashback.rss.apis.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

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
