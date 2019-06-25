package se.uu.ub.cora.metacreator;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;

public final class DataCreatorHelper {

	private static final String RECORD_INFO = "recordInfo";

	private DataCreatorHelper() {
		// not called
		throw new UnsupportedOperationException();
	}

	public static String extractDataDividerStringFromDataGroup(DataGroup topLevelGroup) {
		DataGroup dataDividerGroup = extractDataDividerGroupFromDataGroup(topLevelGroup);
		return dataDividerGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
	}

	private static DataGroup extractDataDividerGroupFromDataGroup(DataGroup topLevelGroup) {
		DataGroup recordInfoGroup = topLevelGroup.getFirstGroupWithNameInData(RECORD_INFO);
		return recordInfoGroup.getFirstGroupWithNameInData("dataDivider");
	}

	public static DataGroup createRecordInfoWithIdAndDataDivider(String id,
			String dataDividerLinkedRecordId) {
		DataGroup recordInfo = DataGroup.withNameInData(RECORD_INFO);
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", id));

		createAndAddDataDivider(dataDividerLinkedRecordId, recordInfo);

		return recordInfo;
	}

	private static void createAndAddDataDivider(String dataDividerLinkedRecordId,
			DataGroup recordInfo) {
		DataGroup dataDivider = DataGroup.withNameInData("dataDivider");
		dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(
				DataAtomic.withNameInDataAndValue("linkedRecordId", dataDividerLinkedRecordId));
		recordInfo.addChild(dataDivider);
	}

	public static String extractIdFromDataGroup(DataGroup mainDataGroup) {
		DataGroup recordInfo = mainDataGroup.getFirstGroupWithNameInData(RECORD_INFO);
		return recordInfo.getFirstAtomicValueWithNameInData("id");
	}

}
