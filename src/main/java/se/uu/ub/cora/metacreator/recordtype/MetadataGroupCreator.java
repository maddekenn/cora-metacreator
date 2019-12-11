package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.data.DataAtomicProvider;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataGroupProvider;

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
		topLevelDataGroup.addChild(
				DataAtomicProvider.getDataAtomicUsingNameInDataAndValue("nameInData", nameInData));
	}

	@Override
	protected DataGroup createTopLevelSpiderDataGroup() {
		return DataGroupProvider.getDataGroupUsingNameInData("metadata");
	}

	private void addTextIds() {
		createAndAddTextWithNameInDataIdAndLinkedRecordType("textId", id + "Text", "coraText");
		createAndAddTextWithNameInDataIdAndLinkedRecordType("defTextId", id + "DefText",
				"coraText");
	}

	private void createAndAddTextWithNameInDataIdAndLinkedRecordType(String nameInData,
			String textId, String linkedRecordType) {
		DataGroup textIdGroup = DataGroupProvider.getDataGroupUsingNameInData(nameInData);
		textIdGroup.addChild(
				DataAtomicProvider.getDataAtomicUsingNameInDataAndValue("linkedRecordId", textId));
		textIdGroup.addChild(DataAtomicProvider
				.getDataAtomicUsingNameInDataAndValue("linkedRecordType", linkedRecordType));
		topLevelDataGroup.addChild(textIdGroup);
	}

	@Override
	protected void addAttributeType() {
		topLevelDataGroup.addAttributeByIdWithValue("type", "group");
	}

}
