package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;

public class MetadataGroupCreator extends GroupCreator {

	private String nameInData;

	public MetadataGroupCreator(String id, String nameInData, String dataDivider) {
		super(id, dataDivider);
		this.nameInData = nameInData;
	}

	public static MetadataGroupCreator withIdAndNameInDataAndDataDivider(String id,
			String nameInData, String dataDivider) {
		return new MetadataGroupCreator(id, nameInData, dataDivider);
	}

	@Override
	public DataGroup createGroup(String refRecordInfoId) {

		super.createGroup(refRecordInfoId);
		addNameInData();
		addTextIds();

		return topLevelDataGroup;
	}

	private void addNameInData() {
		topLevelDataGroup
				.addChild(DataAtomic.withNameInDataAndValue("nameInData", nameInData));
	}

	@Override
	protected DataGroup createTopLevelDataGroup() {
		return DataGroup.withNameInData("metadata");
	}

	private void addTextIds() {
		createAndAddTextWithNameInDataIdAndLinkedRecordType("textId", id + "Text", "coraText");
		createAndAddTextWithNameInDataIdAndLinkedRecordType("defTextId", id + "DefText",
				"coraText");
	}

	private void createAndAddTextWithNameInDataIdAndLinkedRecordType(String nameInData,
			String textId, String linkedRecordType) {
		DataGroup textIdGroup = DataGroup.withNameInData(nameInData);
		textIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", textId));
		textIdGroup
				.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
		topLevelDataGroup.addChild(textIdGroup);
	}

	@Override
	protected void addAttributeType() {
		topLevelDataGroup.addAttributeByIdWithValue("type", "group");
	}

}
