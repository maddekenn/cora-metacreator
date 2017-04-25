package se.uu.ub.cora.metacreator.recordlink;

import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class RecordLinkCompleter implements ExtendedFunctionality {
    private String linkedRecordType;
    private SpiderDataGroup spiderDataGroup;

    public RecordLinkCompleter(String linkedRecordType) {
        this.linkedRecordType = linkedRecordType;
    }

    public static RecordLinkCompleter forTextLinkedRecordType(String linkedRecordType) {
        return new RecordLinkCompleter(linkedRecordType);
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
        completer.completeSpiderDataGroupWithLinkedTexts(spiderDataGroup, linkedRecordType);
    }
}
