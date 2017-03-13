package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class RecordTypeMetaCompleter implements ExtendedFunctionality {

	private String userId;
	private SpiderDataGroup spiderDataGroup;
	private String id;

	@Override
	public void useExtendedFunctionality(String userId, SpiderDataGroup spiderDataGroup) {
		this.userId = userId;
		this.spiderDataGroup = spiderDataGroup;

		addValuesToDataGroup();
	}

	private void addValuesToDataGroup() {
		SpiderDataGroup recordInfoGroup = spiderDataGroup.extractGroup("recordInfo");
		id = recordInfoGroup.extractAtomicValue("id");
		addMetadataIds();
		addPresentationIds();
		addTexts();
//		SpiderDataGroup search = SpiderDataGroup.withNameInData("search");
//		search.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "search"));
//		search.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", id + "Search"));
//		spiderDataGroup.addChild(search);
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("search", "search", id + "Search");
	}

	private void addMetadataIds() {
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("metadataId", "metadataGroup", id + "Group");
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("newMetadataId", "metadataGroup", id + "NewGroup");

	}

	private void createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting(String nameInData, String linkedRecordType, String linkedRecordId) {
		if (!spiderDataGroup.containsChildWithNameInData(nameInData)) {
			SpiderDataGroup link = SpiderDataGroup.withNameInData(nameInData);
			link.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
			link.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordId));
			spiderDataGroup.addChild(link);
		}
	}

	private void addAtomicValueWithNameInDataAndValueIfNotExisting(String nameInData,
			String value) {
		if (!spiderDataGroup.containsChildWithNameInData(nameInData)) {
			spiderDataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue(nameInData, value));
		}
	}

	private void addPresentationIds() {

		String linkedRecordType = "presentationGroup";
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("presentationViewId", linkedRecordType, id + "ViewPGroup");
//		addAtomicValueWithNameInDataAndValueIfNotExisting("presentationViewId", id + "ViewPGroup");

		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("presentationFormId", linkedRecordType, id + "FormPGroup");
//		addAtomicValueWithNameInDataAndValueIfNotExisting("presentationFormId", id + "FormPGroup");
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("newPresentationFormId", linkedRecordType, id + "FormNewPGroup");
//		addAtomicValueWithNameInDataAndValueIfNotExisting("newPresentationFormId",
//				id + "FormNewPGroup");
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("menuPresentationViewId", linkedRecordType, id + "MenuPGroup");
//		addAtomicValueWithNameInDataAndValueIfNotExisting("menuPresentationViewId",
//				id + "MenuPGroup");
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("listPresentationViewId", linkedRecordType, id + "ListPGroup");
//		addAtomicValueWithNameInDataAndValueIfNotExisting("listPresentationViewId",
//				id + "ListPGroup");
		addAtomicValueWithNameInDataAndValueIfNotExisting("selfPresentationViewId",
				id + "ViewSelfPGroup");
//		 addAtomicValueWithNameInDataAndValueIfNotExisting("searchPresentationFormId",
//		 id+"FormSearchPGroup");
	}

	private void addTexts() {
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("textId", "text", id + "Text");
//		addAtomicValueWithNameInDataAndValueIfNotExisting("textId", id + "Text");
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("defTextId", "text", id + "DefText");
//		addAtomicValueWithNameInDataAndValueIfNotExisting("defTextId", id + "DefText");
	}

}
