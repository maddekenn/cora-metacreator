package se.uu.ub.cora.metacreator.group;

import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class GroupCompleter implements ExtendedFunctionality {

    private SpiderDataGroup spiderDataGroup;
    private String linkedRecordType;

    public GroupCompleter(String linkedRecordType) {
        this.linkedRecordType = linkedRecordType;
    }


    public static GroupCompleter forTextLinkedRecordType(String linkedRecordType) {
        return new GroupCompleter(linkedRecordType);
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
