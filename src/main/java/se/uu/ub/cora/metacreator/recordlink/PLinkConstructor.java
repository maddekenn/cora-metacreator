package se.uu.ub.cora.metacreator.recordlink;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.DataCreatorHelper;

public class PLinkConstructor {

	DataGroup constructPLinkWithIdDataDividerPresentationOfAndMode(String id, String dataDivider,
			String presentationOf, String mode) {

		DataGroup pLink = DataGroup.withNameInData("presentation");
		createAndAddRecordInfo(id, dataDivider, pLink);

		createAndAddPresentationOf(presentationOf, pLink);
		pLink.addChild(DataAtomic.withNameInDataAndValue("mode", mode));
		pLink.addAttributeByIdWithValue("type", "pRecordLink");
		return pLink;
	}

	private void createAndAddRecordInfo(String id, String dataDivider, DataGroup pCollVar) {
		DataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider(id,
				dataDivider);
		pCollVar.addChild(recordInfo);
	}

	private void createAndAddPresentationOf(String presentationOf, DataGroup pCollVar) {
		DataGroup presentationOfGroup = DataGroup.withNameInData("presentationOf");
		presentationOfGroup.addChild(
				DataAtomic.withNameInDataAndValue("linkedRecordType", "metadataRecordLink"));
		presentationOfGroup
				.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", presentationOf));
		pCollVar.addChild(presentationOfGroup);
	}

}
