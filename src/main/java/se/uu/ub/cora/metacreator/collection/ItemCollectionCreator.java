package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;

public class ItemCollectionCreator implements ExtendedFunctionality {

    private String userId;
    private SpiderDataGroup spiderDataGroup;

    @Override
    public void useExtendedFunctionality(String userId, SpiderDataGroup spiderDataGroup) {
        this.userId = userId;
        this.spiderDataGroup = spiderDataGroup;

        SpiderDataGroup itemReferences = spiderDataGroup.extractGroup("collectionItemReferences");
        for (SpiderDataElement child : itemReferences.getChildren()) {
            createItem(spiderDataGroup, (SpiderDataGroup) child);
        }

    }

    private void createItem(SpiderDataGroup spiderDataGroup, SpiderDataGroup child) {
        String id = extractId(child);

        SpiderDataGroup item = SpiderDataGroup.withNameInData("metadata");

        String dataDivider = DataCreatorHelper.extractDataDividerFromDataGroup(spiderDataGroup);
        SpiderDataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider(id, dataDivider);
        item.addChild(recordInfo);

        addAtomicValues(id, item);
        createRecord("collectionItem", item);
    }

    private String extractId(SpiderDataGroup child) {
        SpiderDataGroup ref = child;
        return ref.extractAtomicValue("linkedRecordId");
    }

    private void addAtomicValues(String linkedRecordId, SpiderDataGroup item) {
        String nameInData = linkedRecordId.substring(0, linkedRecordId.indexOf("Item"));
        item.addChild(SpiderDataAtomic.withNameInDataAndValue("nameInData", nameInData));
        item.addChild(SpiderDataAtomic.withNameInDataAndValue("textId", linkedRecordId+"Text"));
        item.addChild(SpiderDataAtomic.withNameInDataAndValue("defTextId", linkedRecordId+"DefText"));
    }

    private void createRecord(String recordTypeToCreate, SpiderDataGroup spiderDataGroupToCreate) {
        SpiderRecordCreator spiderRecordCreatorOutput = SpiderInstanceProvider
                .getSpiderRecordCreator();
        spiderRecordCreatorOutput.createAndStoreRecord(userId, recordTypeToCreate,
                spiderDataGroupToCreate);
    }
}
