package se.uu.ub.cora.metacreator.search;

import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class SearchCompleter implements ExtendedFunctionality {

    private String textLinkedRecordType;

    public SearchCompleter(String textLinkedRecordType) {
        this.textLinkedRecordType = textLinkedRecordType;
    }

    public static SearchCompleter forTextLinkedRecordType(String textLinkedRecordType) {
        return new SearchCompleter(textLinkedRecordType);
    }

    @Override
    public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
        MetadataCompleter completer = new MetadataCompleter();
        completer.completeSpiderDataGroupWithLinkedTexts(spiderDataGroup, textLinkedRecordType);
    }
}
