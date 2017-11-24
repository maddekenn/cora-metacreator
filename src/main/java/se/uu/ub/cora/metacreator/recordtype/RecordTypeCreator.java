package se.uu.ub.cora.metacreator.recordtype;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.metacreator.TextConstructor;
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
	private static final String METADATA_GROUP = "metadataGroup";
	private static final String INPUT_MODE = "input";
	private String authToken;
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
		this.authToken = userId;
		this.topLevelDataGroup = spiderDataGroup;
		spiderRecordReader = SpiderInstanceProvider.getSpiderRecordReader();
		SpiderDataGroup recordInfo = spiderDataGroup.extractGroup(RECORD_INFO);
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
			spiderRecordReader.readRecord(authToken, recordType, presentationGroupId);
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
		if (recordDoesNotExistInStorage(METADATA_GROUP, metadataId)) {
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
		storeRecord(METADATA_GROUP, metadataGroup);
	}

	private void possiblyCreatePresentationGroups() {
		String presentationOf = getPresentationOf(METADATA_ID);

		createFormPresentation(presentationOf);
		createNewFormPresentation(getPresentationOf("newMetadataId"));

		createOutputPresentations(presentationOf);
		createAutocompletePresentation(presentationOf);
	}

	private String getPresentationOf(String metadataId) {
		return getLinkedRecordIdFromGroupByNameInData(metadataId);
	}

	private void createFormPresentation(String presentationOf) {
		String presentationId = extractPresentationIdUsingNameInData("presentationFormId");
		List<SpiderDataElement> metadataChildReferences = getMetadataChildReferencesFromMetadataGroup(presentationOf);
		usePGroupCreatorWithPresentationOfIdChildRefsAndMode(presentationOf, presentationId, metadataChildReferences, INPUT_MODE);
	}

	private String extractPresentationIdUsingNameInData(String presentationNameInData) {
		SpiderDataGroup presentationIdGroup = topLevelDataGroup.extractGroup(presentationNameInData);
		return presentationIdGroup.extractAtomicValue(LINKED_RECORD_ID);
	}

	private List<SpiderDataElement> getMetadataChildReferencesFromMetadataGroup(String presentationOf) {
		SpiderDataRecord spiderDataRecord = spiderRecordReader.readRecord(authToken, METADATA_GROUP,
				presentationOf);
		return spiderDataRecord.getSpiderDataGroup()
				.extractGroup("childReferences").getChildren();
	}

	private void usePGroupCreatorWithPresentationOfIdChildRefsAndMode(String presentationOf, String presentationId, List<SpiderDataElement> metadataChildReferences, String mode) {
		PresentationGroupCreator presentationGroupCreator = PresentationGroupCreator.withAuthTokenPresentationIdAndDataDivider(authToken, presentationId, dataDivider);
		presentationGroupCreator.setPresentationOfAndMode(presentationOf, mode);
		presentationGroupCreator.setMetadataChildReferences(metadataChildReferences);
		presentationGroupCreator.createPGroupIfNotAlreadyExist();
	}

	private void createOutputPresentations(String presentationOf) {
		String mode = "output";
		createViewPresentation(presentationOf, mode);
		createPresentationWithPresentationOfIdAndModeOnlyRecordInfoAsChild(presentationOf, "menuPresentationViewId", mode);
		createPresentationWithPresentationOfIdAndModeOnlyRecordInfoAsChild(presentationOf, "listPresentationViewId", mode);
	}

	private void createViewPresentation(String presentationOf, String mode) {
		List<SpiderDataElement> metadataChildReferences = getMetadataChildReferencesFromMetadataGroup(presentationOf);
		String presentationId = extractPresentationIdUsingNameInData("presentationViewId");
		usePGroupCreatorWithPresentationOfIdChildRefsAndMode(presentationOf, presentationId, metadataChildReferences, mode);
	}

	private void createPresentationWithPresentationOfIdAndModeOnlyRecordInfoAsChild(String presentationOf, String presentationIdToExtract, String mode) {
		String presentationId = extractPresentationIdFromNameInData(presentationIdToExtract);
		SpiderDataRecord spiderDataRecord = spiderRecordReader.readRecord(authToken, METADATA_GROUP,
				presentationOf);
		List<SpiderDataElement> metadataChildReferences = getRecordInfoAsMetadataChildReference(spiderDataRecord);
		usePGroupCreatorWithPresentationOfIdChildRefsAndMode(presentationOf, presentationId, metadataChildReferences, mode);
	}

	private String extractPresentationIdFromNameInData(String presentationNameInData) {
		SpiderDataGroup presentationIdGroup = topLevelDataGroup.extractGroup(presentationNameInData);
		return presentationIdGroup.extractAtomicValue(LINKED_RECORD_ID);
	}

	private void createNewFormPresentation(String presentationOf) {
		String presentationId = extractPresentationIdUsingNameInData("newPresentationFormId");
		List<SpiderDataElement> metadataChildReferences = getMetadataChildReferencesFromMetadataGroup(presentationOf);
		usePGroupCreatorWithPresentationOfIdChildRefsAndMode(presentationOf, presentationId, metadataChildReferences, INPUT_MODE);
	}

	private void extractDataDivider() {
		SpiderDataGroup dataDividerGroup = extractDataDividerFromMainSpiderDataGroup();
		dataDivider = dataDividerGroup.extractAtomicValue(LINKED_RECORD_ID);
	}

	private SpiderDataGroup extractDataDividerFromMainSpiderDataGroup() {
		SpiderDataGroup recordInfoGroup = topLevelDataGroup.extractGroup(RECORD_INFO);
		return recordInfoGroup.extractGroup("dataDivider");
	}

	private List<SpiderDataElement> getRecordInfoAsMetadataChildReference(SpiderDataRecord spiderDataRecord) {
		List<SpiderDataElement> metadataChildReferences = new ArrayList<>();
		SpiderDataGroup childReferences = getChildReferences(spiderDataRecord);
		for (SpiderDataElement spiderDataElement : childReferences.getChildren()) {
			addChildIfRecordInfo(metadataChildReferences, spiderDataElement);
		}
		return metadataChildReferences;
	}

	private SpiderDataGroup getChildReferences(SpiderDataRecord spiderDataRecord) {
		SpiderDataGroup spiderDataGroup = spiderDataRecord.getSpiderDataGroup();
		return spiderDataGroup.extractGroup("childReferences");
	}

	private void addChildIfRecordInfo(List<SpiderDataElement> metadataChildReferences, SpiderDataElement spiderDataElement) {
		String linkedRecordId = getRefLinkedRecordId((SpiderDataGroup) spiderDataElement);
		if (refIsRecordInfo(linkedRecordId)) {
            metadataChildReferences.add(spiderDataElement);
        }
	}

	private String getRefLinkedRecordId(SpiderDataGroup spiderDataElement) {
		SpiderDataGroup childReference = spiderDataElement;
		SpiderDataGroup ref = childReference.extractGroup("ref");
		return ref.extractAtomicValue("linkedRecordId");
	}

	private boolean refIsRecordInfo(String linkedRecordId) {
		return linkedRecordId.startsWith(RECORD_INFO);
	}

	private void storeRecord(String recordTypeToCreate, SpiderDataGroup spiderDataGroupToStore) {
		SpiderRecordCreator spiderRecordCreatorOutput = SpiderInstanceProvider
				.getSpiderRecordCreator();
		spiderRecordCreatorOutput.createAndStoreRecord(authToken, recordTypeToCreate,
				spiderDataGroupToStore);
	}

	private void createAutocompletePresentation(String presentationOf) {
		createPresentationWithPresentationOfIdAndModeOnlyRecordInfoAsChild(presentationOf, "autocompletePresentationView", INPUT_MODE);
	}

}
