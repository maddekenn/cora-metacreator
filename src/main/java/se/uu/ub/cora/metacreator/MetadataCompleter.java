package se.uu.ub.cora.metacreator;

import se.uu.ub.cora.data.DataAtomicProvider;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataGroupProvider;

public class MetadataCompleter {

	private static final String DEF_TEXT_ID = "defTextId";
	private static final String TEXT_ID = "textId";
	private String id = "";

	public void completeSpiderDataGroupWithTexts(DataGroup metadataGroup) {
		id = extractIdFromMetadataGroup(metadataGroup);
		possiblyCompleteTextId(metadataGroup);
		possiblyCompleteDefTextId(metadataGroup);
	}

	private void possiblyCompleteDefTextId(DataGroup metadataGroup) {
		if (!metadataGroup.containsChildWithNameInData(DEF_TEXT_ID)) {
			metadataGroup.addChild(DataAtomicProvider
					.getDataAtomicUsingNameInDataAndValue(DEF_TEXT_ID, id + "DefText"));
		}
	}

	private void possiblyCompleteTextId(DataGroup metadataGroup) {
		if (!metadataGroup.containsChildWithNameInData(TEXT_ID)) {
			metadataGroup.addChild(
					DataAtomicProvider.getDataAtomicUsingNameInDataAndValue(TEXT_ID, id + "Text"));
		}
	}

	private String extractIdFromMetadataGroup(DataGroup metadataGroup) {
		DataGroup recordInfo = metadataGroup.getFirstGroupWithNameInData("recordInfo");
		return recordInfo.getFirstAtomicValueWithNameInData("id");
	}

	public void completeDataGroupWithLinkedTexts(DataGroup metadataGroup,
			String textRecordType) {
		id = extractIdFromMetadataGroup(metadataGroup);
		possiblyAddLinkedTextWithNameInDataTextIdAndTextRecordType(metadataGroup, TEXT_ID,
				id + "Text", textRecordType);
		possiblyAddLinkedTextWithNameInDataTextIdAndTextRecordType(metadataGroup, DEF_TEXT_ID,
				id + "DefText", textRecordType);
	}

	private void possiblyAddLinkedTextWithNameInDataTextIdAndTextRecordType(DataGroup metadataGroup,
			String textNameInData, String textId, String textRecordType) {
		if (!metadataGroup.containsChildWithNameInData(textNameInData)) {
			addLinkedTextWithNameInDataTextIdAndTextRecordType(metadataGroup, textNameInData,
					textId, textRecordType);
		}
	}

	private void addLinkedTextWithNameInDataTextIdAndTextRecordType(DataGroup metadataGroup,
			String textNameInData, String textId, String textRecordType) {
		DataGroup textIdGroup = createLinkedTextWithNameInDataLinkedIdAndLinkedType(textNameInData,
				textId, textRecordType);
		metadataGroup.addChild(textIdGroup);
	}

	private DataGroup createLinkedTextWithNameInDataLinkedIdAndLinkedType(String nameInData,
			String textId, String textRecordType) {
		DataGroup textIdGroup = DataGroupProvider.getDataGroupUsingNameInData(nameInData);
		textIdGroup.addChild(DataAtomicProvider
				.getDataAtomicUsingNameInDataAndValue("linkedRecordType", textRecordType));
		textIdGroup.addChild(
				DataAtomicProvider.getDataAtomicUsingNameInDataAndValue("linkedRecordId", textId));
		return textIdGroup;
	}

}
