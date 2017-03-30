package se.uu.ub.cora.metacreator;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public final class DataCreatorHelper {

	private static final String RECORD_INFO = "recordInfo";

	private DataCreatorHelper() {
		// not called
		throw new UnsupportedOperationException();
	}

	public static String extractDataDividerStringFromDataGroup(SpiderDataGroup topLevelGroup) {
		SpiderDataGroup dataDividerGroup = extractDataDividerGroupFromSpiderDataGroup(
				topLevelGroup);
		return dataDividerGroup.extractAtomicValue("linkedRecordId");
	}

	private static SpiderDataGroup extractDataDividerGroupFromSpiderDataGroup(
			SpiderDataGroup topLevelGroup) {
		SpiderDataGroup recordInfoGroup = topLevelGroup.extractGroup(RECORD_INFO);
		return recordInfoGroup.extractGroup("dataDivider");
	}

	public static SpiderDataGroup createRecordInfoWithIdAndDataDivider(String id,
			String dataDividerLinkedRecordId) {
		SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData(RECORD_INFO);
		recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", id));

		createAndAddDataDivider(dataDividerLinkedRecordId, recordInfo);

		return recordInfo;
	}

	private static void createAndAddDataDivider(String dataDividerLinkedRecordId,
			SpiderDataGroup recordInfo) {
		SpiderDataGroup dataDivider = SpiderDataGroup.withNameInData("dataDivider");
		dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId",
				dataDividerLinkedRecordId));
		recordInfo.addChild(dataDivider);
	}

	public static String extractIdFromDataGroup(SpiderDataGroup mainDataGroup) {
		SpiderDataGroup recordInfo = mainDataGroup.extractGroup(RECORD_INFO);
		return recordInfo.extractAtomicValue("id");
	}

}
