package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public abstract class GroupCreator {
	protected String id;
	protected String dataDivider;
	protected SpiderDataGroup topLevelSpiderDataGroup;

	public GroupCreator(String id, String dataDivider) {
		this.id = id;
		this.dataDivider = dataDivider;
	}

	public SpiderDataGroup createGroup(String refRecordInfoId) {
		topLevelSpiderDataGroup = createTopLevelSpiderDataGroup();

		createAndAddRecordInfoToSpiderDataGroup();
		//TODO: maybe nameInData should be provided

		addChildReferences(refRecordInfoId);
		addAttributeType();
		return topLevelSpiderDataGroup;
	}

	abstract SpiderDataGroup createTopLevelSpiderDataGroup();
	abstract void addAttributeType();

	protected void createAndAddRecordInfoToSpiderDataGroup() {
		SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
		recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", id));

		SpiderDataGroup dataDividerGroup = SpiderDataGroup.withNameInData("dataDivider");
		dataDividerGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDividerGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", dataDivider));

		recordInfo.addChild(dataDividerGroup);
		topLevelSpiderDataGroup.addChild(recordInfo);
	}

	protected void addChildReferences(String refRecordInfoId) {
		SpiderDataGroup childReferences = SpiderDataGroup.withNameInData("childReferences");
		SpiderDataGroup childReference = SpiderDataGroup.withNameInData("childReference");
		childReference.addChild(SpiderDataAtomic.withNameInDataAndValue("ref", refRecordInfoId));
		childReference.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMin", "1"));
		childReference.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMax", "1"));
		childReference.setRepeatId("0");
		childReferences.addChild(childReference);
		topLevelSpiderDataGroup.addChild(childReferences);
	}
}
