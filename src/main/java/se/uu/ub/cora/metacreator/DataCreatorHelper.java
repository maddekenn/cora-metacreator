package se.uu.ub.cora.metacreator;

import se.uu.ub.cora.data.DataAtomicProvider;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataGroupProvider;

public final class DataCreatorHelper {

	private static final String RECORD_INFO = "recordInfo";

	private DataCreatorHelper() {
		// not called
		throw new UnsupportedOperationException();
	}

	public static String extractDataDividerStringFromDataGroup(DataGroup topLevelGroup) {
		DataGroup dataDividerGroup = extractDataDividerGroupFromSpiderDataGroup(topLevelGroup);
		return dataDividerGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
	}

	private static DataGroup extractDataDividerGroupFromSpiderDataGroup(DataGroup topLevelGroup) {
		DataGroup recordInfoGroup = topLevelGroup.getFirstGroupWithNameInData(RECORD_INFO);
		return recordInfoGroup.getFirstGroupWithNameInData("dataDivider");
	}

	public static DataGroup createRecordInfoWithIdAndDataDivider(String id,
			String dataDividerLinkedRecordId) {
		DataGroup recordInfo = DataGroupProvider.getDataGroupUsingNameInData(RECORD_INFO);
		recordInfo.addChild(DataAtomicProvider.getDataAtomicUsingNameInDataAndValue("id", id));

		createAndAddDataDivider(dataDividerLinkedRecordId, recordInfo);

		return recordInfo;
	}

	private static void createAndAddDataDivider(String dataDividerLinkedRecordId,
			DataGroup recordInfo) {
		DataGroup dataDivider = DataGroupProvider.getDataGroupUsingNameInData("dataDivider");
		dataDivider.addChild(DataAtomicProvider
				.getDataAtomicUsingNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(DataAtomicProvider
				.getDataAtomicUsingNameInDataAndValue("linkedRecordId", dataDividerLinkedRecordId));
		recordInfo.addChild(dataDivider);
	}

	public static String extractIdFromDataGroup(DataGroup mainDataGroup) {
		DataGroup recordInfo = mainDataGroup.getFirstGroupWithNameInData(RECORD_INFO);
		return recordInfo.getFirstAtomicValueWithNameInData("id");
	}

}
