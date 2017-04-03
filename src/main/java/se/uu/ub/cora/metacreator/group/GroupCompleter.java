package se.uu.ub.cora.metacreator.group;

import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class GroupCompleter implements ExtendedFunctionality {

    private SpiderDataGroup spiderDataGroup;

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
        completer.completeSpiderDataGroupWithTexts(spiderDataGroup);
    }
}
