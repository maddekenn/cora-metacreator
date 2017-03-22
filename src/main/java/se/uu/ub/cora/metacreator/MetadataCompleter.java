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

	public void completeSpiderDataGroupWithLinkedTexts(SpiderDataGroup metadataGroup) {
		id = extractIdFromMetadataGroup(metadataGroup);
		possiblyCompleteLinkedTextIdUsingForTextIdTypeUsingTextId(metadataGroup, TEXT_ID,
				id + "Text");
		possiblyCompleteLinkedTextIdUsingForTextIdTypeUsingTextId(metadataGroup, DEF_TEXT_ID,
				id + "DefText");
	}

	private void possiblyCompleteLinkedTextIdUsingForTextIdTypeUsingTextId(
			SpiderDataGroup metadataGroup, String textIdType, String textId) {
		if (!metadataGroup.containsChildWithNameInData(textIdType)) {
			SpiderDataGroup textIdGroup = createLinkedText(textIdType, textId);
			metadataGroup.addChild(textIdGroup);
		}
	}

	private SpiderDataGroup createLinkedText(String textIdType, String textId) {
		SpiderDataGroup textIdGroup = SpiderDataGroup.withNameInData(textIdType);
		textIdGroup
				.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "text"));
		textIdGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", textId));
		return textIdGroup;
	}

}
