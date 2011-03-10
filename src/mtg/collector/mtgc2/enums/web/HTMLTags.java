package mtg.collector.mtgc2.enums.web;

/**
 *
 * @author Eric Kisner
 */
public enum HTMLTags {

    start_table( "<table" ),
    start_tr( "<tr" ),
    start_td( "<td" ),
    start_div( "<div" ),
    start_span( "<span" ),
    start_p( "<p>" ),
    start_img( "<img" ),
    start_title( "<title" ),
    end_table( "</table>" ),
    end_tr( "</tr>" ),
    end_td( "</td>" ),
    end_div( "</div>" ),
    end_span( "</span>" ),
    end_p( "</p>" ),
    end_title( "</title>" );
    private String htmlTag;

    private HTMLTags( String htmlTag ) {
        this.htmlTag = htmlTag;
    }

    public String getHtmlTag() {
        return htmlTag;
    }
}
