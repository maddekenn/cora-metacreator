package se.uu.ub.cora.metacreator.search;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.metacreator.RecordCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class SearchCreator implements ExtendedFunctionality {
    private String implementingTextType;
    private String authToken;
    private SpiderDataGroup spiderDataGroup;

    public SearchCreator(String implementingTextType) {
        this.implementingTextType = implementingTextType;
    }

    public static SearchCreator forImplementingTextType(String implementingTextType) {
        return new SearchCreator(implementingTextType);
    }

    @Override
    public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
        this.authToken = authToken;
        this.spiderDataGroup = spiderDataGroup;
        createTextsIfMissing();
    }

    private void createTextsIfMissing() {
        createTextWithTextIdToExtractIfMissing("textId");
        createTextWithTextIdToExtractIfMissing("defTextId");
    }

    private void createTextWithTextIdToExtractIfMissing(String textIdToExtract) {
        SpiderDataGroup textIdGroup = this.spiderDataGroup.extractGroup(textIdToExtract);
        String textId = textIdGroup.extractAtomicValue("linkedRecordId");
        if (textIsMissing(textId)) {
            createTextWithTextId(textId);
        }
    }

    private boolean textIsMissing(String textId) {
        try {
            SpiderRecordReader spiderRecordReader = SpiderInstanceProvider.getSpiderRecordReader();
            spiderRecordReader.readRecord(authToken, implementingTextType, textId);
        } catch (RecordNotFoundException e) {
            return true;
        }
        return false;
    }

    private void createTextWithTextId(String textId) {
        String dataDivider = DataCreatorHelper
                .extractDataDividerStringFromDataGroup(spiderDataGroup);
//        RecordCreatorHelper recordCreatorHelper = new RecordCreatorHelper(authToken);
//        recordCreatorHelper.createTextInStorageWithTextIdDataDividerAndTextType(textId, dataDivider,
//                implementingTextType);
    }

}
