package se.uu.ub.cora.metacreator.recordType;

import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class RecordTypeMetaCompleter implements ExtendedFunctionality {

    private String userId;
    private SpiderDataGroup spiderDataGroup;

    @Override
    public void useExtendedFunctionality(String userId, SpiderDataGroup spiderDataGroup) {
        this.userId = userId;
        this.spiderDataGroup = spiderDataGroup;
    }
}
