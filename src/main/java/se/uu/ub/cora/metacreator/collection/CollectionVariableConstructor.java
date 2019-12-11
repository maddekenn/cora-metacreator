package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.data.DataAtomicProvider;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataGroupProvider;
import se.uu.ub.cora.metacreator.DataCreatorHelper;

public class CollectionVariableConstructor {

	protected DataGroup collectionVar;

	public DataGroup constructCollectionVarWithIdNameInDataDataDividerAndRefCollection(String id,
			String nameInData, String dataDivider, String refCollection) {

		return createSpiderDataGroup(id, nameInData, dataDivider, refCollection);
	}

	private DataGroup createSpiderDataGroup(String id, String nameInData, String dataDivider,
			String refCollection) {
		createSpiderDataGroup();
		addChildren(id, nameInData, dataDivider, refCollection);
		return collectionVar;
	}

	private void createSpiderDataGroup() {
		collectionVar = DataGroupProvider.getDataGroupUsingNameInData("metadata");
		collectionVar.addAttributeByIdWithValue("type", "collectionVariable");
	}

	private void addChildren(String id, String nameInData, String dataDivider,
			String refCollection) {
		createAndAddRecordInfo(id, dataDivider, collectionVar);

		addAtomicValues(nameInData);
		addRefCollection(refCollection);
	}

	private void addAtomicValues(String nameInData) {
		collectionVar.addChild(
				DataAtomicProvider.getDataAtomicUsingNameInDataAndValue("nameInData", nameInData));
	}

	private void createAndAddRecordInfo(String id, String dataDivider, DataGroup collectionVar) {
		DataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider(id,
				dataDivider);
		collectionVar.addChild(recordInfo);
	}

	private void addRefCollection(String refCollection) {
		DataGroup refCollectionGroup = DataGroupProvider
				.getDataGroupUsingNameInData("refCollection");
		refCollectionGroup.addChild(DataAtomicProvider.getDataAtomicUsingNameInDataAndValue(
				"linkedRecordType", "metadataItemCollection"));
		refCollectionGroup.addChild(DataAtomicProvider
				.getDataAtomicUsingNameInDataAndValue("linkedRecordId", refCollection));
		collectionVar.addChild(refCollectionGroup);
	}

}
