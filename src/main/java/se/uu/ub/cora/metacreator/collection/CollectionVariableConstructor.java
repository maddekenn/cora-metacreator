package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class CollectionVariableConstructor {

	protected SpiderDataGroup collectionVar;

	public SpiderDataGroup constructCollectionVarWithIdNameInDataDataDividerAndRefCollection(
			String id, String nameInData, String dataDivider, String refCollection) {

		return createSpiderDataGroup(id, nameInData, dataDivider, refCollection);
	}

	private SpiderDataGroup createSpiderDataGroup(String id, String nameInData, String dataDivider,
			String refCollection) {
		createSpiderDataGroup();
		addChildren(id, nameInData, dataDivider, refCollection);
		return collectionVar;
	}

	private void createSpiderDataGroup() {
		collectionVar = SpiderDataGroup.withNameInData("metadata");
		collectionVar.addAttributeByIdWithValue("type", "collectionVariable");
	}

	private void addChildren(String id, String nameInData, String dataDivider,
			String refCollection) {
		createAndAddRecordInfo(id, dataDivider, collectionVar);

		addAtomicValues(nameInData);
		addRefCollection(refCollection);
	}

	private void addAtomicValues(String nameInData) {
		collectionVar.addChild(SpiderDataAtomic.withNameInDataAndValue("nameInData", nameInData));
	}

	private void createAndAddRecordInfo(String id, String dataDivider,
			SpiderDataGroup collectionVar) {
		SpiderDataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider(id,
				dataDivider);
		collectionVar.addChild(recordInfo);
	}

	private void addRefCollection(String refCollection) {
		SpiderDataGroup refCollectionGroup = SpiderDataGroup.withNameInData("refCollection");
		refCollectionGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType",
				"metadataItemCollection"));
		refCollectionGroup
				.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", refCollection));
		collectionVar.addChild(refCollectionGroup);
	}

}
