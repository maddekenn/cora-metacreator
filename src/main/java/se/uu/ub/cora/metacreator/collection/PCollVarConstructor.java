package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PCollVarConstructor {

	SpiderDataGroup constructPCollVarWithIdDataDividerPresentationOfAndMode(String id,
			String dataDivider, String presentationOf, String mode) {

		SpiderDataGroup pCollVar = createGroupWithRecordInfo(id, dataDivider);

		createAndAddPresentationOf(presentationOf, pCollVar);
		addAtomicValues(mode, pCollVar);
		pCollVar.addAttributeByIdWithValue("type", "pCollVar");
		return pCollVar;
	}

	private SpiderDataGroup createGroupWithRecordInfo(String id, String dataDivider) {
		SpiderDataGroup pCollVar = SpiderDataGroup.withNameInData("presentation");
		createAndAddRecordInfo(id, dataDivider, pCollVar);
		return pCollVar;
	}

	private void createAndAddRecordInfo(String id, String dataDivider, SpiderDataGroup pCollVar) {
		SpiderDataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider(id,
				dataDivider);
		pCollVar.addChild(recordInfo);
	}

	private void createAndAddPresentationOf(String presentationOf, SpiderDataGroup pCollVar) {
		SpiderDataGroup presentationOfGroup = SpiderDataGroup.withNameInData("presentationOf");
		presentationOfGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType",
				"metadataCollectionVariable"));
		presentationOfGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", presentationOf));
		pCollVar.addChild(presentationOfGroup);
	}

	private void addAtomicValues(String mode, SpiderDataGroup pCollVar) {
		pCollVar.addChild(SpiderDataAtomic.withNameInDataAndValue("mode", mode));
		pCollVar.addChild(
				SpiderDataAtomic.withNameInDataAndValue("emptyTextId", "initialEmptyValueText"));
	}

}
