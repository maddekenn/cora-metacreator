package se.uu.ub.cora.metacreator;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class MetadataCompleterImp implements MetadataCompleter {

	private String id = "";

	@Override
	public void completeSpiderDataGroupWithTexts(SpiderDataGroup metadataGroup) {
		if (!metadataGroup.containsChildWithNameInData("textId")) {
			id = extractIdFromMetadataGroup(metadataGroup);
			metadataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("textId", id + "Text"));
		}
		if (!metadataGroup.containsChildWithNameInData("defTextId")) {
			if ("".equals(id)) {
				id = extractIdFromMetadataGroup(metadataGroup);
			}
			metadataGroup
					.addChild(SpiderDataAtomic.withNameInDataAndValue("defTextId", id + "DefText"));
		}
	}

	private String extractIdFromMetadataGroup(SpiderDataGroup metadataGroup) {
		SpiderDataGroup recordInfo = metadataGroup.extractGroup("recordInfo");
		return recordInfo.extractAtomicValue("id");
	}

}
