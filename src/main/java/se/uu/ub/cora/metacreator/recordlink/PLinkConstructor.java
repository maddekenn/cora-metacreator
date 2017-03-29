package se.uu.ub.cora.metacreator.recordlink;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PLinkConstructor {

	SpiderDataGroup constructPLinkWithIdDataDividerPresentationOfAndMode(String id,
			String dataDivider, String presentationOf, String mode) {

		SpiderDataGroup pLink = SpiderDataGroup.withNameInData("presentation");
		createAndAddRecordInfo(id, dataDivider, pLink);

		createAndAddPresentationOf(presentationOf, pLink);
		pLink.addChild(SpiderDataAtomic.withNameInDataAndValue("mode", mode));
		pLink.addAttributeByIdWithValue("type", "pRecordLink");
		return pLink;
	}

	private void createAndAddRecordInfo(String id, String dataDivider, SpiderDataGroup pCollVar) {
		SpiderDataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider(id,
				dataDivider);
		pCollVar.addChild(recordInfo);
	}

	private void createAndAddPresentationOf(String presentationOf, SpiderDataGroup pCollVar) {
		SpiderDataGroup presentationOfGroup = SpiderDataGroup.withNameInData("presentationOf");
		presentationOfGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "metadataRecordLink"));
		presentationOfGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", presentationOf));
		pCollVar.addChild(presentationOfGroup);
	}

}
