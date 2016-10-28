package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class RecordTypeCreator implements ExtendedFunctionality {


    private String userId;
    private SpiderDataGroup spiderDataGroup;
    private String dataDivider;

    @Override
    public void useExtendedFunctionality(String userId, SpiderDataGroup spiderDataGroup) {
        this.userId = userId;
        this.spiderDataGroup = spiderDataGroup;

        possiblyCreatePresentationGroups();
    }

    private void possiblyCreatePresentationGroups() {
    	SpiderDataGroup dataDividerGroup = extractDataDividerFromMainSpiderDataGroup();
    	dataDivider = dataDividerGroup.extractAtomicValue("linkedRecordId");
    	String presentationOf = spiderDataGroup.extractAtomicValue("metadataId");

        extractPresentationIdAndSendToCreate(presentationOf, "presentationViewId");
        extractPresentationIdAndSendToCreate(presentationOf, "presentationFormId");
        extractPresentationIdAndSendToCreate(presentationOf, "newPresentationFormId");
        extractPresentationIdAndSendToCreate(presentationOf, "menuPresentationViewId");
        extractPresentationIdAndSendToCreate(presentationOf, "listPresentationViewId");
    }

    private void extractPresentationIdAndSendToCreate(String presentationOf, String presentationNameInData) {
        String presentationId = spiderDataGroup.extractAtomicValue(presentationNameInData);
        possiblyCreatePresentationGroupWithPresentationOfAndNameInData(presentationOf, presentationId);
    }

    private void possiblyCreatePresentationGroupWithPresentationOfAndNameInData(String presentationOf, String presentationId) {
        if(recordDoesNotExistInStorage("presentationGroup", presentationId)){
            createPresentationGroup(presentationOf, presentationId);
        }
    }

    private void createPresentationGroup(String presentationOf, String presentationId) {
        PGroupCreator presentationGroup = PGroupCreator.withIdDataDividerAndPresentationOf(presentationId, dataDivider, presentationOf);
        SpiderDataGroup pGroup = presentationGroup.createPresentationGroup();
        storePGroup("presentationGroup", pGroup);
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

    private SpiderDataGroup extractDataDividerFromMainSpiderDataGroup() {
        SpiderDataGroup recordInfoGroup = spiderDataGroup.extractGroup("recordInfo");
        return recordInfoGroup.extractGroup("dataDivider");
    }

    private void storePGroup(String recordTypeToCreate, SpiderDataGroup presentationGroup) {
        SpiderRecordCreator spiderRecordCreatorOutput = SpiderInstanceProvider
                .getSpiderRecordCreator();
        spiderRecordCreatorOutput.createAndStoreRecord(userId, recordTypeToCreate,
                presentationGroup);
    }
}
