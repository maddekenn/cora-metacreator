package se.uu.ub.cora.metacreator.search;

import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class SearchCompleter implements ExtendedFunctionality {

    private String implementingTextType;

    public SearchCompleter(String implementingTextType) {

        this.implementingTextType = implementingTextType;
    }

    public static SearchCompleter forTextLinkedRecordType(String linkedRecordType) {
        return new SearchCompleter(linkedRecordType);
    }

    @Override
    public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
        MetadataCompleter completer = new MetadataCompleter();
        completer.completeSpiderDataGroupWithLinkedTexts(spiderDataGroup, implementingTextType);
    }
}
