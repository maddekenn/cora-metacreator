package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PCollVarConstructor {

	SpiderDataGroup constructPCollVarWithIdDataDividerPresentationOfAndMode(String id,
			String dataDivider, String presentationOf, String mode) {

		SpiderDataGroup pCollVar = SpiderDataGroup.withNameInData("presentation");
		createAndAddRecordInfo(id, dataDivider, pCollVar);

		createAndAddPresentationOf(presentationOf, pCollVar);
		pCollVar.addChild(SpiderDataAtomic.withNameInDataAndValue("mode", mode));
		pCollVar.addChild(
				SpiderDataAtomic.withNameInDataAndValue("emptyTextId", "initialEmptyValueText"));
		pCollVar.addAttributeByIdWithValue("type", "pCollVar");
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

}
