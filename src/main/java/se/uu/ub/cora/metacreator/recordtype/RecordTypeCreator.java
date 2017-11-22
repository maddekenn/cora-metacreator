package se.uu.ub.cora.metacreator.recordtype;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.metacreator.TextConstructor;
import se.uu.ub.cora.metacreator.group.PGroupConstructor;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.data.SpiderDataRecord;
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
	private SpiderDataGroup topLevelDataGroup;
	private String dataDivider;
	private String implementingTextType;
	private String recordTypeId;
	private SpiderRecordReader spiderRecordReader;

	public RecordTypeCreator(String implementingTextType) {
		this.implementingTextType = implementingTextType;
	}

	public static RecordTypeCreator forImplementingTextType(String implementingTextType) {
		return new RecordTypeCreator(implementingTextType);
	}

	@Override
	public void useExtendedFunctionality(String userId, SpiderDataGroup spiderDataGroup) {
		this.userId = userId;
		this.topLevelDataGroup = spiderDataGroup;
		spiderRecordReader = SpiderInstanceProvider.getSpiderRecordReader();
		SpiderDataGroup recordInfo = spiderDataGroup.extractGroup("recordInfo");
		recordTypeId = recordInfo.extractAtomicValue("id");

		possiblyCreateNecessaryTextsMetadataAndPresentations();
	}

	private void possiblyCreateNecessaryTextsMetadataAndPresentations() {
		extractDataDivider();
		possiblyCreateText("textId");
		possiblyCreateText("defTextId");
		possiblyCreateMetadataGroups();
		possiblyCreatePresentationGroups();
	}

	private void possiblyCreateText(String textIdToExtract) {
		String textId = getTextId(textIdToExtract);
		if (recordDoesNotExistInStorage(implementingTextType, textId)) {
			createText(textId);
		}
	}

	private String getTextId(String idToExtract) {
		return getLinkedRecordIdFromGroupByNameInData(idToExtract);
	}

	private String getLinkedRecordIdFromGroupByNameInData(String textIdToExtract) {
		SpiderDataGroup textGroup = topLevelDataGroup.extractGroup(textIdToExtract);
		return textGroup.extractAtomicValue(LINKED_RECORD_ID);
	}

	private boolean recordDoesNotExistInStorage(String recordType, String presentationGroupId) {
		try {
			spiderRecordReader.readRecord(userId, recordType, presentationGroupId);
		} catch (RecordNotFoundException e) {
			return true;
		}
		return false;
	}

	private void createText(String textId) {
		TextConstructor textConstructor = TextConstructor.withTextIdAndDataDivider(textId,
				dataDivider);
		SpiderDataGroup text = textConstructor.createText();
		storeRecord(implementingTextType, text);
	}

	private void possiblyCreateMetadataGroups() {
		possiblyCreateMetadataGroup(METADATA_ID, "recordInfoGroup");
		possiblyCreateMetadataGroup("newMetadataId", "recordInfoNewGroup");
	}

	private void possiblyCreateMetadataGroup(String metadataIdToExtract, String childReference) {
		String metadataId = getMetadataId(metadataIdToExtract);
		if (recordDoesNotExistInStorage("metadataGroup", metadataId)) {
			createMetadataGroup(childReference, metadataId);
		}
	}

	private String getMetadataId(String metadataIdToExtract) {
		return getLinkedRecordIdFromGroupByNameInData(metadataIdToExtract);
	}

	private void createMetadataGroup(String childReference, String metadataId) {
		MetadataGroupCreator groupCreator = MetadataGroupCreator
				.withIdAndNameInDataAndDataDivider(metadataId, recordTypeId, dataDivider);
		SpiderDataGroup metadataGroup = groupCreator.createGroup(childReference);
		metadataGroup
				.addChild(SpiderDataAtomic.withNameInDataAndValue("excludePGroupCreation", "true"));
		storeRecord("metadataGroup", metadataGroup);
	}

	private void possiblyCreatePresentationGroups() {
		String presentationOf = getPresentationOf(METADATA_ID);

		createFormPresentation(presentationOf);
		createNewFormPresentation();

		createOutputPresentations(presentationOf);
		createAutocompletePresentation(presentationOf);
	}

	private String getPresentationOf(String metadataId) {
		return getLinkedRecordIdFromGroupByNameInData(metadataId);
	}

	private void createFormPresentation(String presentationOf) {
		String presentationId = extractPresentationIdUsingNameInData("presentationFormId");

		List<SpiderDataElement> metadataChildReferences = getMetadataChildReferencesFromMetadataGroup(presentationOf);
		possiblyCreatePGroupWithPresentationOfIdMetadataChildRefsAndMode(presentationOf, presentationId, "input", metadataChildReferences);

	}

	private void possiblyCreatePGroupWithPresentationOfIdMetadataChildRefsAndMode(String presentationOf, String presentationId, String mode, List<SpiderDataElement> metadataChildReferences) {
		if (recordDoesNotExistInStorage("presentationGroup", presentationId)) {
			createPresentationGroup(presentationOf, presentationId, metadataChildReferences, mode);
		}
	}

	private List<SpiderDataElement> getMetadataChildReferencesFromMetadataGroup(String presentationOf) {
		SpiderDataRecord spiderDataRecord = spiderRecordReader.readRecord(userId, "metadataGroup",
				presentationOf);
		return spiderDataRecord.getSpiderDataGroup()
				.extractGroup("childReferences").getChildren();
	}

	private String extractPresentationIdUsingNameInData(String presentationNameInData) {
		SpiderDataGroup presentationIdGroup = topLevelDataGroup.extractGroup(presentationNameInData);
		return presentationIdGroup.extractAtomicValue(LINKED_RECORD_ID);
	}

	private void createOutputPresentations(String presentationOf) {

		sendToCreate(presentationOf, extractPresentationIdFromNameInData("presentationViewId"),
				"output");
		sendToCreate(presentationOf, extractPresentationIdFromNameInData("menuPresentationViewId"),
				"output");
		sendToCreate(presentationOf, extractPresentationIdFromNameInData("listPresentationViewId"),
				"output");
	}

	private String extractPresentationIdFromNameInData(String presentationNameInData) {
		SpiderDataGroup presentationIdGroup = topLevelDataGroup.extractGroup(presentationNameInData);
		return presentationIdGroup.extractAtomicValue(LINKED_RECORD_ID);
	}

	private void createNewFormPresentation() {
		SpiderDataGroup metadataIdGroup = topLevelDataGroup.extractGroup("newMetadataId");
		String presentationOf = metadataIdGroup.extractAtomicValue(LINKED_RECORD_ID);
		extractPresentationIdAndSendToCreate(presentationOf, "newPresentationFormId", "input");
	}

	private void extractDataDivider() {
		SpiderDataGroup dataDividerGroup = extractDataDividerFromMainSpiderDataGroup();
		dataDivider = dataDividerGroup.extractAtomicValue(LINKED_RECORD_ID);
	}

	private SpiderDataGroup extractDataDividerFromMainSpiderDataGroup() {
		SpiderDataGroup recordInfoGroup = topLevelDataGroup.extractGroup(RECORD_INFO);
		return recordInfoGroup.extractGroup("dataDivider");
	}

	private void sendToCreate(String presentationOf, String presentationId, String mode) {
		SpiderDataRecord spiderDataRecord = spiderRecordReader.readRecord(userId, "metadataGroup",
				presentationOf);
		List<SpiderDataElement> metadataChildReferences = new ArrayList<>();
		SpiderDataGroup spiderDataGroup2 = spiderDataRecord.getSpiderDataGroup();
		SpiderDataGroup childReferences = spiderDataGroup2.extractGroup("childReferences");
		for (SpiderDataElement spiderDataElement : childReferences.getChildren()) {
			SpiderDataGroup childReference = (SpiderDataGroup) spiderDataElement;
			SpiderDataGroup ref = childReference.extractGroup("ref");
			String linkedRecordId = ref.extractAtomicValue("linkedRecordId");
			if (linkedRecordId.startsWith("recordInfo")) {
				metadataChildReferences.add(spiderDataElement);
			}
		}
		possiblyCreatePresentationGroupWithPresentationOfAndNameInData(presentationOf,
				presentationId, mode, metadataChildReferences);
	}

	private void extractPresentationIdAndSendToCreate(String presentationOf,
			String presentationNameInData, String mode) {
		SpiderDataGroup presentationIdGroup = topLevelDataGroup.extractGroup(presentationNameInData);
		String presentationId = presentationIdGroup.extractAtomicValue(LINKED_RECORD_ID);
			SpiderDataRecord spiderDataRecord = spiderRecordReader.readRecord(userId, "metadataGroup",
					presentationOf);
		if (recordDoesNotExistInStorage("presentationGroup", presentationId)) {
			List<SpiderDataElement> metadataChildReferences = spiderDataRecord.getSpiderDataGroup()
					.extractGroup("childReferences").getChildren();
			createPresentationGroup(presentationOf, presentationId, metadataChildReferences, mode);
		}
	}

	private void possiblyCreatePresentationGroupWithPresentationOfAndNameInData(
			String presentationOf, String presentationId, String mode,
			List<SpiderDataElement> metadataChildReferences) {
		if (recordDoesNotExistInStorage("presentationGroup", presentationId)) {
			createPresentationGroup(presentationOf, presentationId, metadataChildReferences, mode);
		}
	}

	private void createPresentationGroup(String presentationOf, String presentationId,
			List<SpiderDataElement> metadataChildReferences, String mode) {

		PGroupConstructor constructor = new PGroupConstructor(userId);
		SpiderDataGroup pGroup = constructor
				.constructPGroupWithIdDataDividerPresentationOfChildrenAndMode(presentationId,
						dataDivider, presentationOf, metadataChildReferences, mode);

		storeRecord("presentationGroup", pGroup);
	}

	private void storeRecord(String recordTypeToCreate, SpiderDataGroup spiderDataGroupToStore) {
		SpiderRecordCreator spiderRecordCreatorOutput = SpiderInstanceProvider
				.getSpiderRecordCreator();
		spiderRecordCreatorOutput.createAndStoreRecord(userId, recordTypeToCreate,
				spiderDataGroupToStore);
	}

	private void createAutocompletePresentation(String presentationOf) {
		sendToCreate(presentationOf,
				extractPresentationIdFromNameInData("autocompletePresentationView"), "input");
	}

}
