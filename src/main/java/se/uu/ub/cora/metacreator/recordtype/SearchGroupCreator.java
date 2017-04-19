package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class SearchGroupCreator extends GroupCreator {

	private String recordType;

	public SearchGroupCreator(String id, String dataDivider, String recordType) {
		super(id, dataDivider);
		this.recordType = recordType;
	}

	public static SearchGroupCreator withIdIdAndDataDividerAndRecordType(String id,
			String dataDivider, String recordType) {
		return new SearchGroupCreator(id, dataDivider, recordType);
	}

	@Override
	public SpiderDataGroup createGroup(String childReferenceId) {
		super.createGroup(childReferenceId);
		addChildren();
		return topLevelSpiderDataGroup;
	}

	private void addChildren() {
		addLinkChildWithNameInDataLinkedTypeAndLinkedIdAndRepeatId("recordTypeToSearchIn",
				"recordType", recordType, "0");
		addLinkChildWithNameInDataLinkedTypeAndLinkedId("metadataId", "metadataGroup",
				"autocompleteSearchGroup");
		addLinkChildWithNameInDataLinkedTypeAndLinkedId("presentationId", "presentationGroup",
				"autocompleteSearchPGroup");

		addTexts();

		topLevelSpiderDataGroup
				.addChild(SpiderDataAtomic.withNameInDataAndValue("searchGroup", "autocomplete"));
	}

	private void addLinkChildWithNameInDataLinkedTypeAndLinkedId(String nameInData,
			String linkedRecordType, String linkedRecordId) {
		SpiderDataGroup recordTypeToSearchIn = createLinkChildWithNameInDataAndLinkedTypeAndLinkedId(
				nameInData, linkedRecordType, linkedRecordId);
		topLevelSpiderDataGroup.addChild(recordTypeToSearchIn);
	}

	private void addLinkChildWithNameInDataLinkedTypeAndLinkedIdAndRepeatId(String nameInData,
			String linkedRecordType, String linkedRecordId, String repeatId) {
		SpiderDataGroup linkChild = createLinkChildWithNameInDataAndLinkedTypeAndLinkedId(
				nameInData, linkedRecordType, linkedRecordId);
		linkChild.setRepeatId(repeatId);
		topLevelSpiderDataGroup.addChild(linkChild);
	}

	private SpiderDataGroup createLinkChildWithNameInDataAndLinkedTypeAndLinkedId(String nameInData,
			String linkedRecordType, String linkedRecordId) {
		SpiderDataGroup linkChild = SpiderDataGroup.withNameInData(nameInData);
		linkChild.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
		linkChild.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordId));
		return linkChild;
	}

	private void addTexts() {
		addLinkChildWithNameInDataLinkedTypeAndLinkedId("textId", "text", id + "Text");
		addLinkChildWithNameInDataLinkedTypeAndLinkedId("defTextId", "text", id + "DefText");
	}

	@Override
	SpiderDataGroup createTopLevelSpiderDataGroup() {
		return SpiderDataGroup.withNameInData("search");
	}

	@Override
	void addAttributeType() {
		// not implemented for search
	}

	@Override
	protected void addChildReferencesWithChildId(String refRecordInfoId) {
		// not implemented for search
	}
}
