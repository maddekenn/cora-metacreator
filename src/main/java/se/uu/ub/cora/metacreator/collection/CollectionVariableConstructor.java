package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.DataCreatorHelper;

public class CollectionVariableConstructor {

	protected DataGroup collectionVar;

	public DataGroup constructCollectionVarWithIdNameInDataDataDividerAndRefCollection(String id,
			String nameInData, String dataDivider, String refCollection) {

		return createDataGroup(id, nameInData, dataDivider, refCollection);
	}

	private DataGroup createDataGroup(String id, String nameInData, String dataDivider,
			String refCollection) {
		createDataGroup();
		addChildren(id, nameInData, dataDivider, refCollection);
		return collectionVar;
	}

	private void createDataGroup() {
		collectionVar = DataGroup.withNameInData("metadata");
		collectionVar.addAttributeByIdWithValue("type", "collectionVariable");
	}

	private void addChildren(String id, String nameInData, String dataDivider,
			String refCollection) {
		createAndAddRecordInfo(id, dataDivider, collectionVar);

		addAtomicValues(nameInData);
		addRefCollection(refCollection);
	}

	private void addAtomicValues(String nameInData) {
		collectionVar.addChild(DataAtomic.withNameInDataAndValue("nameInData", nameInData));
	}

	private void createAndAddRecordInfo(String id, String dataDivider, DataGroup collectionVar) {
		DataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider(id,
				dataDivider);
		collectionVar.addChild(recordInfo);
	}

	private void addRefCollection(String refCollection) {
		DataGroup refCollectionGroup = DataGroup.withNameInData("refCollection");
		refCollectionGroup.addChild(
				DataAtomic.withNameInDataAndValue("linkedRecordType", "metadataItemCollection"));
		refCollectionGroup
				.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", refCollection));
		collectionVar.addChild(refCollectionGroup);
	}

}
