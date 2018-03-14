package se.uu.ub.cora.metacreator.recordlink;

import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class RecordLinkCompleter implements ExtendedFunctionality {
    private String implementingTextType;
    private SpiderDataGroup spiderDataGroup;

    public RecordLinkCompleter(String implementingTextType) {
        this.implementingTextType = implementingTextType;
    }

    public static RecordLinkCompleter forTextLinkedRecordType(String implementingTextType) {
        return new RecordLinkCompleter(implementingTextType);
    }

    @Override
    public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
        this.spiderDataGroup = spiderDataGroup;
        addValuesToDataGroup();
    }

    private void addValuesToDataGroup() {
        addTexts();
    }

    private void addTexts() {
        MetadataCompleter completer = new MetadataCompleter();
        completer.completeSpiderDataGroupWithLinkedTexts(spiderDataGroup, implementingTextType);
    }

    public String getImplementingTextType() {
        return implementingTextType;
    }
}
