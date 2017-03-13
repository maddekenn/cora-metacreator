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
	public SpiderDataGroup createGroup() {
		super.createGroup();
		createRecordTypeToSearchInPartOfSearch();
		return topLevelSpiderDataGroup;
	}

	private void createRecordTypeToSearchInPartOfSearch() {
		SpiderDataGroup recordTypeToSearchIn = SpiderDataGroup
				.withNameInData("recordTypeToSearchIn");
		recordTypeToSearchIn.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		recordTypeToSearchIn
				.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", recordType));
		topLevelSpiderDataGroup.addChild(recordTypeToSearchIn);
	}

	@Override
	SpiderDataGroup createTopLevelSpiderDataGroup() {
		return SpiderDataGroup.withNameInData("search");
	}

	@Override
	void addAttributeType() {
		topLevelSpiderDataGroup.addAttributeByIdWithValue("type", "search");

	}

	@Override
	void addValuesForChildReference(SpiderDataGroup childReference) {
		// TODO Auto-generated method stub

	}

}
