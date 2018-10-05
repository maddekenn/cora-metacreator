package se.uu.ub.cora.metacreator.group;

import se.uu.ub.cora.metacreator.RecordCompleter;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class GroupCompleter extends RecordCompleter implements ExtendedFunctionality {


    public GroupCompleter(String implementingTextType) {
        super(implementingTextType);
    }


    public static GroupCompleter forTextLinkedRecordType(String linkedRecordType) {
        return new GroupCompleter(linkedRecordType);
    }

    @Override
    public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
        this.spiderDataGroup = spiderDataGroup;
        addValuesToDataGroup();
    }
}
