package se.uu.ub.cora.metacreator.recordtype;

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
        	
        extractDataDivider();
        possiblyCreateMetadataGroups(spiderDataGroup);
        possiblyCreatePresentationGroups();
    }

    private void possiblyCreateMetadataGroups(SpiderDataGroup spiderDataGroup) {
        possiblyCreateMetadataGroup("metadataId");
        possiblyCreateMetadataGroup("newMetadataId");
    }

    private void possiblyCreateMetadataGroup(String metadataIdToExtract) {
        String metadataId = spiderDataGroup.extractAtomicValue(metadataIdToExtract);
        if(recordDoesNotExistInStorage("metadataGroup", metadataId)) {
            MetadataGroupCreator groupCreator = MetadataGroupCreator.withIdAndNameInData(metadataId, "metadata");
            SpiderDataGroup metadataGroup = groupCreator.createGroup("recordInfoGroup");
            storeRecord("metadataGroup", metadataGroup);
        }
    }

    private void possiblyCreatePresentationGroups() {
    	String presentationOf = spiderDataGroup.extractAtomicValue("metadataId");

        extractPresentationIdAndSendToCreate(presentationOf, "presentationViewId", "recordInfoPGroup");
        extractPresentationIdAndSendToCreate(presentationOf, "presentationFormId", "recordInfoPGroup");
        extractPresentationIdAndSendToCreate(presentationOf, "newPresentationFormId", "recordInfoNewPGroup");
        extractPresentationIdAndSendToCreate(presentationOf, "menuPresentationViewId", "recordInfoPGroup");
        extractPresentationIdAndSendToCreate(presentationOf, "listPresentationViewId", "recordInfoPGroup");
    }

	private void extractDataDivider() {
		SpiderDataGroup dataDividerGroup = extractDataDividerFromMainSpiderDataGroup();
    	dataDivider = dataDividerGroup.extractAtomicValue("linkedRecordId");
	}

    private void extractPresentationIdAndSendToCreate(String presentationOf, String presentationNameInData, String refRecordInfoId) {
        String presentationId = spiderDataGroup.extractAtomicValue(presentationNameInData);
        possiblyCreatePresentationGroupWithPresentationOfAndNameInData(presentationOf, presentationId, refRecordInfoId);
    }

    private void possiblyCreatePresentationGroupWithPresentationOfAndNameInData(String presentationOf, String presentationId, String refRecordInfoId) {
        if(recordDoesNotExistInStorage("presentationGroup", presentationId)){
            createPresentationGroup(presentationOf, presentationId, refRecordInfoId);
        }
    }

    private void createPresentationGroup(String presentationOf, String presentationId, String refRecordInfoId) {
        PresentationGroupCreator presentationGroup = PresentationGroupCreator.withIdDataDividerAndPresentationOf(presentationId, dataDivider, presentationOf);
        SpiderDataGroup pGroup = presentationGroup.createGroup(refRecordInfoId);
        storeRecord("presentationGroup", pGroup);
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

    private void storeRecord(String recordTypeToCreate, SpiderDataGroup presentationGroup) {
        SpiderRecordCreator spiderRecordCreatorOutput = SpiderInstanceProvider
                .getSpiderRecordCreator();
        spiderRecordCreatorOutput.createAndStoreRecord(userId, recordTypeToCreate,
                presentationGroup);
    }
}
