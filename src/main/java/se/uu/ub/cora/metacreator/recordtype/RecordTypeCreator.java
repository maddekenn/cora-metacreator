package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.metacreator.text.TextCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class RecordTypeCreator implements ExtendedFunctionality {

	private static final String RECORD_INFO = "recordInfo";
	private static final String LINKED_RECORD_ID = "linkedRecordId";
	private static final String METADATA_ID = "metadataId";
	private String userId;
	private SpiderDataGroup spiderDataGroup;
	private String dataDivider;
	private String implementingTextType;

	public RecordTypeCreator(String implementingTextType) {
		this.implementingTextType = implementingTextType;
	}

	public static RecordTypeCreator forImplementingTextType(String implementingTextType) {
		return new RecordTypeCreator(implementingTextType);
	}

	@Override
	public void useExtendedFunctionality(String userId, SpiderDataGroup spiderDataGroup) {
		this.userId = userId;
		this.spiderDataGroup = spiderDataGroup;

		extractDataDivider();
		possiblyCreateText("textId");
		possiblyCreateText("defTextId");
		possiblyCreateMetadataGroups();
		possiblyCreatePresentationGroups();
		// possiblyCreateSearch();
	}

	private void possiblyCreateText(String textIdToExtract) {
		SpiderDataGroup textGroup = spiderDataGroup.extractGroup(textIdToExtract);

		String textId = textGroup.extractAtomicValue(LINKED_RECORD_ID);
		if (recordDoesNotExistInStorage(implementingTextType, textId)) {
			TextCreator textCreator = TextCreator.withTextIdAndDataDivider(textId, dataDivider);
			SpiderDataGroup text = textCreator.createText();
			storeRecord(implementingTextType, text);
		}
	}

	private void possiblyCreateMetadataGroups() {
		possiblyCreateMetadataGroup(METADATA_ID, "recordInfoGroup");
		possiblyCreateMetadataGroup("newMetadataId", "recordInfoNewGroup");
	}

	// private void possiblyCreateSearch() {
	// SpiderDataGroup searchIdGroup = spiderDataGroup.extractGroup("search");
	// String searchId = searchIdGroup.extractAtomicValue(LINKED_RECORD_ID);
	// if (recordDoesNotExistInStorage("search", searchId)) {
	// }
	// }

	private void possiblyCreateMetadataGroup(String metadataIdToExtract, String childReference) {
		SpiderDataGroup metadataIdGroup = spiderDataGroup.extractGroup(metadataIdToExtract);
		String metadataId = metadataIdGroup.extractAtomicValue(LINKED_RECORD_ID);
		if (recordDoesNotExistInStorage("metadataGroup", metadataId)) {
			MetadataGroupCreator groupCreator = MetadataGroupCreator
					.withIdAndNameInDataAndDataDivider(metadataId, metadataId, dataDivider);
			SpiderDataGroup metadataGroup = groupCreator.createGroup(childReference);
			storeRecord("metadataGroup", metadataGroup);
		}
	}

	private void possiblyCreatePresentationGroups() {
		SpiderDataGroup metadataIdGroup = spiderDataGroup.extractGroup(METADATA_ID);
		String presentationOf = metadataIdGroup.extractAtomicValue(LINKED_RECORD_ID);
		// String presentationOf =
		// spiderDataGroup.extractAtomicValue(METADATA_ID);

		createFormPresentation(presentationOf);

		createOutputPresentations(presentationOf);

		createNewFormPresentation();
	}

	private void createFormPresentation(String presentationOf) {
		String refRecordInfoId = "recordInfoPGroup";
		extractPresentationIdAndSendToCreate(presentationOf, "presentationFormId", refRecordInfoId);
	}

	private void createOutputPresentations(String presentationOf) {
		String refRecordInfoId = "recordInfoOutputPGroup";
		extractPresentationIdAndSendToCreate(presentationOf, "presentationViewId", refRecordInfoId);
		extractPresentationIdAndSendToCreate(presentationOf, "menuPresentationViewId",
				refRecordInfoId);
		extractPresentationIdAndSendToCreate(presentationOf, "listPresentationViewId",
				refRecordInfoId);
	}

	private void createNewFormPresentation() {
		SpiderDataGroup metadataIdGroup = spiderDataGroup.extractGroup("newMetadataId");
		String presentationOf = metadataIdGroup.extractAtomicValue(LINKED_RECORD_ID);
		// String presentationOf =
		// spiderDataGroup.extractAtomicValue("newMetadataId");
		String refRecordInfoId = "recordInfoNewPGroup";
		extractPresentationIdAndSendToCreate(presentationOf, "newPresentationFormId",
				refRecordInfoId);
	}

	private void extractDataDivider() {
		SpiderDataGroup dataDividerGroup = extractDataDividerFromMainSpiderDataGroup();
		dataDivider = dataDividerGroup.extractAtomicValue(LINKED_RECORD_ID);
	}

	private SpiderDataGroup extractDataDividerFromMainSpiderDataGroup() {
		SpiderDataGroup recordInfoGroup = spiderDataGroup.extractGroup(RECORD_INFO);
		return recordInfoGroup.extractGroup("dataDivider");
	}

	private void extractPresentationIdAndSendToCreate(String presentationOf,
			String presentationNameInData, String refRecordInfoId) {
		SpiderDataGroup presentationIdGroup = spiderDataGroup.extractGroup(presentationNameInData);
		String presentationId = presentationIdGroup.extractAtomicValue(LINKED_RECORD_ID);
		possiblyCreatePresentationGroupWithPresentationOfAndNameInData(presentationOf,
				presentationId, refRecordInfoId);
	}

	private void possiblyCreatePresentationGroupWithPresentationOfAndNameInData(
			String presentationOf, String presentationId, String refRecordInfoId) {
		if (recordDoesNotExistInStorage("presentationGroup", presentationId)) {
			createPresentationGroup(presentationOf, presentationId, refRecordInfoId);
		}
	}

	private void createPresentationGroup(String presentationOf, String presentationId,
			String refRecordInfoId) {
		PresentationGroupCreator presentationGroup = PresentationGroupCreator
				.withIdDataDividerAndPresentationOf(presentationId, dataDivider, presentationOf);
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

	private void storeRecord(String recordTypeToCreate, SpiderDataGroup spiderDataGroupToStore) {
		SpiderRecordCreator spiderRecordCreatorOutput = SpiderInstanceProvider
				.getSpiderRecordCreator();
		spiderRecordCreatorOutput.createAndStoreRecord(userId, recordTypeToCreate,
				spiderDataGroupToStore);
	}
}
