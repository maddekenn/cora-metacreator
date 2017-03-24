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
		addLinkChildWithNameInDataLinkedTypeAndLinkedIdAndRepeatId("recordTypeToSearchIn", "recordType",
				recordType, "0");
		addLinkChildWithNameInDataLinkedTypeAndLinkedId("metadataId", "metadataGroup",
				"autocompleteSearchGroup");
		addLinkChildWithNameInDataLinkedTypeAndLinkedId("presentationId", "presentationGroup",
				"autocompleteSearchPGroup");
	}

	private void addLinkChildWithNameInDataLinkedTypeAndLinkedId(String nameInData,
			String linkedRecordType, String linkedRecordId) {
		SpiderDataGroup recordTypeToSearchIn = createLinkChildWithNameInDataAndLinkedTypeAndLinkedId(nameInData, linkedRecordType, linkedRecordId);
		topLevelSpiderDataGroup.addChild(recordTypeToSearchIn);
	}

	private void addLinkChildWithNameInDataLinkedTypeAndLinkedIdAndRepeatId(String nameInData, String linkedRecordType,
																			String linkedRecordId, String repeatId) {
		SpiderDataGroup recordTypeToSearchIn = createLinkChildWithNameInDataAndLinkedTypeAndLinkedId(nameInData, linkedRecordType, linkedRecordId);
		recordTypeToSearchIn.setRepeatId(repeatId);
		topLevelSpiderDataGroup.addChild(recordTypeToSearchIn);
	}

	private SpiderDataGroup createLinkChildWithNameInDataAndLinkedTypeAndLinkedId(String nameInData, String linkedRecordType, String linkedRecordId) {
		SpiderDataGroup recordTypeToSearchIn = SpiderDataGroup.withNameInData(nameInData);
		recordTypeToSearchIn.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
		recordTypeToSearchIn
				.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordId));
		return recordTypeToSearchIn;
	}

	@Override
	SpiderDataGroup createTopLevelSpiderDataGroup() {
		return SpiderDataGroup.withNameInData("search");
	}

	@Override
	void addAttributeType() {
		//not implemented for search
	}

	@Override
	protected void addChildReferencesWithChildId(String refRecordInfoId){
		//not implemented for search
	}
}
