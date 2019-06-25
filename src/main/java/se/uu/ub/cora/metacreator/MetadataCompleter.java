package se.uu.ub.cora.metacreator;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;

public class MetadataCompleter {

	private static final String DEF_TEXT_ID = "defTextId";
	private static final String TEXT_ID = "textId";
	private String id = "";

	public void completeDataGroupWithTexts(DataGroup metadataGroup) {
		id = extractIdFromMetadataGroup(metadataGroup);
		possiblyCompleteTextId(metadataGroup);
		possiblyCompleteDefTextId(metadataGroup);
	}

	private void possiblyCompleteDefTextId(DataGroup metadataGroup) {
		if (!metadataGroup.containsChildWithNameInData(DEF_TEXT_ID)) {
			metadataGroup.addChild(DataAtomic.withNameInDataAndValue(DEF_TEXT_ID, id + "DefText"));
		}
	}

	private void possiblyCompleteTextId(DataGroup metadataGroup) {
		if (!metadataGroup.containsChildWithNameInData(TEXT_ID)) {
			metadataGroup.addChild(DataAtomic.withNameInDataAndValue(TEXT_ID, id + "Text"));
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
		DataGroup textIdGroup = DataGroup.withNameInData(nameInData);
		textIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", textRecordType));
		textIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", textId));
		return textIdGroup;
	}

}
