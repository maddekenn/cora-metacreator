package se.uu.ub.cora.metacreator;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class MetadataCompleter {

	private String id = "";

	public void completeSpiderDataGroupWithTexts(SpiderDataGroup metadataGroup) {
		id = extractIdFromMetadataGroup(metadataGroup);
		possiblyCompleteTextId(metadataGroup);
		possiblyCompleteDefTextId(metadataGroup);
	}

	private void possiblyCompleteDefTextId(SpiderDataGroup metadataGroup) {
		if (!metadataGroup.containsChildWithNameInData("defTextId")) {
			metadataGroup
					.addChild(SpiderDataAtomic.withNameInDataAndValue("defTextId", id + "DefText"));
		}
	}

	private void possiblyCompleteTextId(SpiderDataGroup metadataGroup) {
		if (!metadataGroup.containsChildWithNameInData("textId")) {
			metadataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("textId", id + "Text"));
		}
	}

	private String extractIdFromMetadataGroup(SpiderDataGroup metadataGroup) {
		SpiderDataGroup recordInfo = metadataGroup.extractGroup("recordInfo");
		return recordInfo.extractAtomicValue("id");
	}

}
