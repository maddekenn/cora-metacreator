package se.uu.ub.cora.metacreator;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class DataCreatorHelper {

	public static String extractDataDividerStringFromDataGroup(SpiderDataGroup topLevelGroup) {
		SpiderDataGroup dataDividerGroup = extractDataDividerGroupFromSpiderDataGroup(
				topLevelGroup);
		return dataDividerGroup.extractAtomicValue("linkedRecordId");
	}

	private static SpiderDataGroup extractDataDividerGroupFromSpiderDataGroup(
			SpiderDataGroup topLevelGroup) {
		SpiderDataGroup recordInfoGroup = topLevelGroup.extractGroup("recordInfo");
		return recordInfoGroup.extractGroup("dataDivider");
	}

	public static SpiderDataGroup createRecordInfoWithIdAndDataDivider(String id,
			String dataDividerLinkedRecordId) {
		SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
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
}
