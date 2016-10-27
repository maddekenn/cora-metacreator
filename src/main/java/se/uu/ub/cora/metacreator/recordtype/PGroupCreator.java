package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class PGroupCreator implements ExtendedFunctionality {


    private String userId;
    private SpiderDataGroup spiderDataGroup;

    @Override
    public void useExtendedFunctionality(String userId, SpiderDataGroup spiderDataGroup) {
        this.userId = userId;
        this.spiderDataGroup = spiderDataGroup;

        possiblyCreatePresentationGroups(spiderDataGroup);
    }

    private void possiblyCreatePresentationGroups(SpiderDataGroup spiderDataGroup) {
        String recordTypeToCreate = "presentationGroup";
        extractPresentationIdAndCreateIfNotExist(spiderDataGroup, "presentationViewId");
        extractPresentationIdAndCreateIfNotExist(spiderDataGroup, "presentationFormId");
        extractPresentationIdAndCreateIfNotExist(spiderDataGroup, "newPresentationFormId");
        extractPresentationIdAndCreateIfNotExist(spiderDataGroup, "menuPresentationViewId");
        extractPresentationIdAndCreateIfNotExist(spiderDataGroup, "listPresentationViewId");
    }

    private void extractPresentationIdAndCreateIfNotExist(SpiderDataGroup spiderDataGroup, String idToExtract) {
        String recordTypeToCreate = "presentationGroup";
        String presentationViewId = spiderDataGroup.extractAtomicValue(idToExtract);
        createPGroupIfNotExists(recordTypeToCreate, presentationViewId);
    }

    private void createPGroupIfNotExists(String recordTypeToCreate, String recordTypeId) {
        if(recordDoesNotExistInStorage(recordTypeToCreate, recordTypeId)){
            createAndStorePGroup(recordTypeToCreate, recordTypeId);
		}
    }

    private boolean recordDoesNotExistInStorage(String recordType, String presentationGroupId) {
        try {
            SpiderRecordReader spiderRecordReader = SpiderInstanceProvider.getSpiderRecordReader();
            spiderRecordReader.readRecord(userId, recordType, presentationGroupId);
        } catch (RecordNotFoundException e) {
            return true;
        }
        return false;
    }

    private void createAndStorePGroup(String recordTypeToCreate, String recordTypeId) {
        SpiderDataGroup presentationGroup = SpiderDataGroup.withNameInData("presentation");

        createAndAddRecordInfo(recordTypeId, presentationGroup);
        createAndAddPresentationOf(recordTypeId, presentationGroup);
        addAttributeType(presentationGroup);
        storePGroup(recordTypeToCreate, presentationGroup);
    }

    private void createAndAddRecordInfo(String recordTypeId, SpiderDataGroup presentationGroup) {
        SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
        recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", recordTypeId));

        SpiderDataGroup dataDivider = extractDataDividerFromMainSpiderDataGroup();

        recordInfo.addChild(dataDivider);
        presentationGroup.addChild(recordInfo);
    }

    private SpiderDataGroup extractDataDividerFromMainSpiderDataGroup() {
        SpiderDataGroup recordInfoGroup = spiderDataGroup.extractGroup("recordInfo");
        return recordInfoGroup.extractGroup("dataDivider");
    }

    private void createAndAddPresentationOf(String recordTypeId, SpiderDataGroup presentationGroup) {
        SpiderDataGroup presentationOf = SpiderDataGroup.withNameInData("presentationOf");
        presentationOf.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "metadataGroup"));
        presentationOf.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", recordTypeId));
        presentationGroup.addChild(presentationOf);
    }

    private void addAttributeType(SpiderDataGroup presentationGroup) {
        presentationGroup.addAttributeByIdWithValue("type", "pGroup");
    }

    private void storePGroup(String recordTypeToCreate, SpiderDataGroup presentationGroup) {
        SpiderRecordCreator spiderRecordCreatorOutput = SpiderInstanceProvider
                .getSpiderRecordCreator();
        spiderRecordCreatorOutput.createAndStoreRecord(userId, recordTypeToCreate,
                presentationGroup);
    }
}
