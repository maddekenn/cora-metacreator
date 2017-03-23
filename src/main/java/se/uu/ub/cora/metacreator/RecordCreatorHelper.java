package se.uu.ub.cora.metacreator;

import se.uu.ub.cora.metacreator.text.TextCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;

public class RecordCreatorHelper {


    private final String authToken;

    public RecordCreatorHelper(String authToken) {
        this.authToken = authToken;
    }

    public void createTextInStorageWithTextIdDataDividerAndTextType(String textId, String dataDivider, String implementingTextType) {
        TextCreator textCreator = TextCreator.withTextIdAndDataDivider(textId, dataDivider);
        SpiderDataGroup textGroup = textCreator.createText();

        SpiderRecordCreator spiderRecordCreator = SpiderInstanceProvider.getSpiderRecordCreator();
        spiderRecordCreator.createAndStoreRecord(authToken, implementingTextType, textGroup);
    }

    public static RecordCreatorHelper withAuthToken(String authToken) {
        return new RecordCreatorHelper(authToken);
    }
}
