package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.data.DataAtomicProvider;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataGroupProvider;

public abstract class GroupCreator {
	protected String id;
	protected String dataDivider;
	protected DataGroup topLevelDataGroup;

	public GroupCreator(String id, String dataDivider) {
		this.id = id;
		this.dataDivider = dataDivider;
	}

	public DataGroup createGroup(String refRecordInfoId) {
		topLevelDataGroup = createTopLevelSpiderDataGroup();

		createAndAddRecordInfoToSpiderDataGroup();

		addChildReferencesWithChildId(refRecordInfoId);
		addAttributeType();
		return topLevelDataGroup;
	}

	abstract DataGroup createTopLevelSpiderDataGroup();

	abstract void addAttributeType();

	protected void createAndAddRecordInfoToSpiderDataGroup() {
		DataGroup recordInfo = DataGroupProvider.getDataGroupUsingNameInData("recordInfo");
		recordInfo.addChild(DataAtomicProvider.getDataAtomicUsingNameInDataAndValue("id", id));

		DataGroup dataDividerGroup = DataGroupProvider.getDataGroupUsingNameInData("dataDivider");
		dataDividerGroup.addChild(DataAtomicProvider
				.getDataAtomicUsingNameInDataAndValue("linkedRecordType", "system"));
		dataDividerGroup.addChild(DataAtomicProvider
				.getDataAtomicUsingNameInDataAndValue("linkedRecordId", dataDivider));

		recordInfo.addChild(dataDividerGroup);
		topLevelDataGroup.addChild(recordInfo);
	}

	protected void addChildReferencesWithChildId(String refRecordInfoId) {
		DataGroup childReferences = DataGroupProvider
				.getDataGroupUsingNameInData("childReferences");
		DataGroup childReference = DataGroupProvider.getDataGroupUsingNameInData("childReference");

		DataGroup refGroup = DataGroupProvider.getDataGroupUsingNameInData("ref");
		refGroup.addChild(DataAtomicProvider.getDataAtomicUsingNameInDataAndValue("linkedRecordId",
				refRecordInfoId));
		refGroup.addChild(DataAtomicProvider
				.getDataAtomicUsingNameInDataAndValue("linkedRecordType", "metadataGroup"));
		childReference.addChild(refGroup);

		addValuesForChildReference(childReference);
		childReference.setRepeatId("0");
		childReferences.addChild(childReference);
		topLevelDataGroup.addChild(childReferences);
	}

	void addValuesForChildReference(DataGroup childReference) {
		childReference.addChild(
				DataAtomicProvider.getDataAtomicUsingNameInDataAndValue("repeatMin", "1"));
		childReference.addChild(
				DataAtomicProvider.getDataAtomicUsingNameInDataAndValue("repeatMax", "1"));
	}
}
