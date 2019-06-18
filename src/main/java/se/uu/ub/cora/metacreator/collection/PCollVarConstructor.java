package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;
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
		pCollVar.addChild(DataAtomic.withNameInDataAndValue("mode", mode));
		createAndAddEmptyTextId(pCollVar);
	}

	private void createAndAddEmptyTextId(DataGroup pCollVar) {
		DataGroup emptyTextIdGroup = DataGroup.withNameInData("emptyTextId");
		emptyTextIdGroup
				.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "coraText"));
		emptyTextIdGroup.addChild(
				DataAtomic.withNameInDataAndValue("linkedRecordId", "initialEmptyValueText"));
		pCollVar.addChild(emptyTextIdGroup);
	}

	private DataGroup createGroupWithRecordInfo(String id, String dataDivider) {
		DataGroup pCollVar = DataGroup.withNameInData("presentation");
		createAndAddRecordInfo(id, dataDivider, pCollVar);
		return pCollVar;
	}

	private void createAndAddRecordInfo(String id, String dataDivider, DataGroup pCollVar) {
		DataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider(id,
				dataDivider);
		pCollVar.addChild(recordInfo);
	}

	private void createAndAddPresentationOf(String presentationOf, DataGroup pCollVar) {
		DataGroup presentationOfGroup = DataGroup.withNameInData("presentationOf");
		presentationOfGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType",
				"metadataCollectionVariable"));
		presentationOfGroup
				.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", presentationOf));
		pCollVar.addChild(presentationOfGroup);
	}

}
