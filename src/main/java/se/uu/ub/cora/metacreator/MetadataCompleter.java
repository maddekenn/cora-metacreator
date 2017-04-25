package se.uu.ub.cora.metacreator;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class MetadataCompleter {

	private static final String DEF_TEXT_ID = "defTextId";
	private static final String TEXT_ID = "textId";
	private String id = "";

	public void completeSpiderDataGroupWithTexts(SpiderDataGroup metadataGroup) {
		id = extractIdFromMetadataGroup(metadataGroup);
		possiblyCompleteTextId(metadataGroup);
		possiblyCompleteDefTextId(metadataGroup);
	}

	private void possiblyCompleteDefTextId(SpiderDataGroup metadataGroup) {
		if (!metadataGroup.containsChildWithNameInData(DEF_TEXT_ID)) {
			metadataGroup
					.addChild(SpiderDataAtomic.withNameInDataAndValue(DEF_TEXT_ID, id + "DefText"));
		}
	}

	private void possiblyCompleteTextId(SpiderDataGroup metadataGroup) {
		if (!metadataGroup.containsChildWithNameInData(TEXT_ID)) {
			metadataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue(TEXT_ID, id + "Text"));
		}
	}

	private String extractIdFromMetadataGroup(SpiderDataGroup metadataGroup) {
		SpiderDataGroup recordInfo = metadataGroup.extractGroup("recordInfo");
		return recordInfo.extractAtomicValue("id");
	}

	public void completeSpiderDataGroupWithLinkedTexts(SpiderDataGroup metadataGroup,
			String textRecordType) {
		id = extractIdFromMetadataGroup(metadataGroup);
		possiblyAddLinkedTextWithNameInDataTextIdAndTextRecordType(metadataGroup, TEXT_ID,
				id + "Text", textRecordType);
		possiblyAddLinkedTextWithNameInDataTextIdAndTextRecordType(metadataGroup, DEF_TEXT_ID,
				id + "DefText", textRecordType);
	}

	private void possiblyAddLinkedTextWithNameInDataTextIdAndTextRecordType(
			SpiderDataGroup metadataGroup, String textNameInData, String textId,
			String textRecordType) {
		if (!metadataGroup.containsChildWithNameInData(textNameInData)) {
			addLinkedTextWithNameInDataTextIdAndTextRecordType(metadataGroup, textNameInData, textId, textRecordType);
		}
	}

	private void addLinkedTextWithNameInDataTextIdAndTextRecordType(SpiderDataGroup metadataGroup, String textNameInData, String textId, String textRecordType) {
		SpiderDataGroup textIdGroup = createLinkedTextWithNameInDataLinkedIdAndLinkedType(
                textNameInData, textId, textRecordType);
		metadataGroup.addChild(textIdGroup);
	}

	private SpiderDataGroup createLinkedTextWithNameInDataLinkedIdAndLinkedType(String nameInData,
																				String textId, String textRecordType) {
		SpiderDataGroup textIdGroup = SpiderDataGroup.withNameInData(nameInData);
		textIdGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", textRecordType));
		textIdGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", textId));
		return textIdGroup;
	}

}
