package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;

public abstract class GroupCreator {
	protected String id;
	protected String dataDivider;
	protected DataGroup topLevelSpiderDataGroup;

	public GroupCreator(String id, String dataDivider) {
		this.id = id;
		this.dataDivider = dataDivider;
	}

	public DataGroup createGroup(String refRecordInfoId) {
		topLevelSpiderDataGroup = createTopLevelSpiderDataGroup();

		createAndAddRecordInfoToSpiderDataGroup();

		addChildReferencesWithChildId(refRecordInfoId);
		addAttributeType();
		return topLevelSpiderDataGroup;
	}

	abstract DataGroup createTopLevelSpiderDataGroup();

	abstract void addAttributeType();

	protected void createAndAddRecordInfoToSpiderDataGroup() {
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", id));

		DataGroup dataDividerGroup = DataGroup.withNameInData("dataDivider");
		dataDividerGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDividerGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", dataDivider));

		recordInfo.addChild(dataDividerGroup);
		topLevelSpiderDataGroup.addChild(recordInfo);
	}

	protected void addChildReferencesWithChildId(String refRecordInfoId) {
		DataGroup childReferences = DataGroup.withNameInData("childReferences");
		DataGroup childReference = DataGroup.withNameInData("childReference");

		DataGroup refGroup = DataGroup.withNameInData("ref");
		refGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", refRecordInfoId));
		refGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "metadataGroup"));
		childReference.addChild(refGroup);

		addValuesForChildReference(childReference);
		childReference.setRepeatId("0");
		childReferences.addChild(childReference);
		topLevelSpiderDataGroup.addChild(childReferences);
	}

	void addValuesForChildReference(DataGroup childReference) {
		childReference.addChild(DataAtomic.withNameInDataAndValue("repeatMin", "1"));
		childReference.addChild(DataAtomic.withNameInDataAndValue("repeatMax", "1"));
	}
}
