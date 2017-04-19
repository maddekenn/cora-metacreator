package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class MetadataGroupCreator extends GroupCreator{

	private String nameInData;

	public MetadataGroupCreator(String id, String nameInData, String dataDivider) {
		super(id, dataDivider);
		this.nameInData = nameInData;
	}

	public static MetadataGroupCreator withIdAndNameInDataAndDataDivider(String id, String nameInData, String dataDivider) {
		return new MetadataGroupCreator(id, nameInData, dataDivider);
	}

	@Override
	public SpiderDataGroup createGroup(String refRecordInfoId) {

		super.createGroup(refRecordInfoId);
		addNameInData();
		addTextIds();

		return topLevelSpiderDataGroup;
	}

	private void addNameInData() {
		topLevelSpiderDataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("nameInData", nameInData));
	}

	@Override
	protected SpiderDataGroup createTopLevelSpiderDataGroup() {
		return SpiderDataGroup.withNameInData("metadata");
	}

	private void addTextIds() {
		SpiderDataGroup textIdGroup = SpiderDataGroup.withNameInData("textId");
		textIdGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", id+"Text"));
		textIdGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "text"));
		topLevelSpiderDataGroup.addChild(textIdGroup);
		SpiderDataGroup defTextIdGroup = SpiderDataGroup.withNameInData("defTextId");
		defTextIdGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", id+"DefText"));
		defTextIdGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "text"));
		topLevelSpiderDataGroup.addChild(defTextIdGroup);
	}

	@Override
	protected void addAttributeType() {
		topLevelSpiderDataGroup.addAttributeByIdWithValue("type", "group");
	}

}
