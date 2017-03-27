package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PCollVarConstructor {

	SpiderDataGroup constructPCollVarWithIdDataDividerPresentationOfAndMode(String id,
			String dataDivider, String presentationOf, String mode) {

		SpiderDataGroup pCollVar = SpiderDataGroup.withNameInData("presentation");
		SpiderDataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider(id,
				dataDivider);
		pCollVar.addChild(recordInfo);

		return pCollVar;
	}

}
