package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class MetadataGroupCreator extends GroupCreator{

	private MetadataGroupCreator(String id, String dataDivider) {
		super(id, dataDivider);
	}

	public static MetadataGroupCreator withIdAndNameInData(String id, String dataDivider) {
		return new MetadataGroupCreator(id, dataDivider);
	}

	@Override
	public SpiderDataGroup createGroup(String refRecordInfoId) {

		super.createGroup(refRecordInfoId);
		addNameInData();
		addTextIds();

		return topLevelSpiderDataGroup;
	}

	private void addNameInData() {
		//TODO: maybe nameInData should be provided
		topLevelSpiderDataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("nameInData", id));
	}

	@Override
	protected SpiderDataGroup createTopLevelSpiderDataGroup() {
		return SpiderDataGroup.withNameInData("metadata");
	}

	private void addTextIds() {
		topLevelSpiderDataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("textId", id+"Text"));
		topLevelSpiderDataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("defTextId", id+"DefText"));
	}

	@Override
	protected void addAttributeType() {
		topLevelSpiderDataGroup.addAttributeByIdWithValue("type", "group");
	}
	
}
