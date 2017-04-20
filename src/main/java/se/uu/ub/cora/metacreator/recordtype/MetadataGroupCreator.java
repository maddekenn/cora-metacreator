package se.uu.ub.cora.metacreator.recordtype;

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
		createAndAddTextWithNameInDataIdAndLinkedRecordType("textId", id + "Text", "text");
		createAndAddTextWithNameInDataIdAndLinkedRecordType("defTextId", id+"DefText", "text");
	}

	private void createAndAddTextWithNameInDataIdAndLinkedRecordType(String nameInData, String textId, String linkedRecordType) {
		SpiderDataGroup textIdGroup = SpiderDataGroup.withNameInData(nameInData);
		textIdGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", textId));
		textIdGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
		topLevelSpiderDataGroup.addChild(textIdGroup);
	}

	@Override
	protected void addAttributeType() {
		topLevelSpiderDataGroup.addAttributeByIdWithValue("type", "group");
	}

}
