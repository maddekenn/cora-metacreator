package se.uu.ub.cora.metacreator.recordlink;

import se.uu.ub.cora.data.DataAtomicProvider;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataGroupProvider;
import se.uu.ub.cora.metacreator.DataCreatorHelper;

public class PLinkConstructor {

	DataGroup constructPLinkWithIdDataDividerPresentationOfAndMode(String id, String dataDivider,
			String presentationOf, String mode) {

		DataGroup pLink = DataGroupProvider.getDataGroupUsingNameInData("presentation");
		createAndAddRecordInfo(id, dataDivider, pLink);

		createAndAddPresentationOf(presentationOf, pLink);
		pLink.addChild(DataAtomicProvider.getDataAtomicUsingNameInDataAndValue("mode", mode));
		pLink.addAttributeByIdWithValue("type", "pRecordLink");
		return pLink;
	}

	private void createAndAddRecordInfo(String id, String dataDivider, DataGroup pCollVar) {
		DataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider(id,
				dataDivider);
		pCollVar.addChild(recordInfo);
	}

	private void createAndAddPresentationOf(String presentationOf, DataGroup pCollVar) {
		DataGroup presentationOfGroup = DataGroupProvider
				.getDataGroupUsingNameInData("presentationOf");
		presentationOfGroup.addChild(DataAtomicProvider
				.getDataAtomicUsingNameInDataAndValue("linkedRecordType", "metadataRecordLink"));
		presentationOfGroup.addChild(DataAtomicProvider
				.getDataAtomicUsingNameInDataAndValue("linkedRecordId", presentationOf));
		pCollVar.addChild(presentationOfGroup);
	}

}
