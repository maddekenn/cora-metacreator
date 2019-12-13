package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.data.DataAtomicProvider;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataGroupProvider;
import se.uu.ub.cora.metacreator.DataCreatorHelper;

public class PCollVarConstructor {

	DataGroup constructPCollVarWithIdDataDividerPresentationOfAndMode(String id, String dataDivider,
			String presentationOf, String mode) {

		DataGroup pCollVar = createGroupWithRecordInfo(id, dataDivider);
		pCollVar.addAttributeByIdWithValue("type", "pCollVar");

		createAndAddChildren(presentationOf, mode, pCollVar);
		return pCollVar;
	}

	private void createAndAddChildren(String presentationOf, String mode, DataGroup pCollVar) {
		createAndAddPresentationOf(presentationOf, pCollVar);
		pCollVar.addChild(DataAtomicProvider.getDataAtomicUsingNameInDataAndValue("mode", mode));
		createAndAddEmptyTextId(pCollVar);
	}

	private void createAndAddEmptyTextId(DataGroup pCollVar) {
		DataGroup emptyTextIdGroup = DataGroupProvider.getDataGroupUsingNameInData("emptyTextId");
		emptyTextIdGroup.addChild(DataAtomicProvider
				.getDataAtomicUsingNameInDataAndValue("linkedRecordType", "coraText"));
		emptyTextIdGroup.addChild(DataAtomicProvider
				.getDataAtomicUsingNameInDataAndValue("linkedRecordId", "initialEmptyValueText"));
		pCollVar.addChild(emptyTextIdGroup);
	}

	private DataGroup createGroupWithRecordInfo(String id, String dataDivider) {
		DataGroup pCollVar = DataGroupProvider.getDataGroupUsingNameInData("presentation");
		createAndAddRecordInfo(id, dataDivider, pCollVar);
		return pCollVar;
	}

	private void createAndAddRecordInfo(String id, String dataDivider, DataGroup pCollVar) {
		DataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider(id,
				dataDivider);
		pCollVar.addChild(recordInfo);
	}

	private void createAndAddPresentationOf(String presentationOf, DataGroup pCollVar) {
		DataGroup presentationOfGroup = DataGroupProvider
				.getDataGroupUsingNameInData("presentationOf");
		presentationOfGroup.addChild(DataAtomicProvider.getDataAtomicUsingNameInDataAndValue(
				"linkedRecordType", "metadataCollectionVariable"));
		presentationOfGroup.addChild(DataAtomicProvider
				.getDataAtomicUsingNameInDataAndValue("linkedRecordId", presentationOf));
		pCollVar.addChild(presentationOfGroup);
	}

}
