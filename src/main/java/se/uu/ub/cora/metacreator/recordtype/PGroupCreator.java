package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PGroupCreator {

	private String id;
	private String dataDivider;
	private String presentationOf;

	public PGroupCreator(String id, String dataDivider, String presentationOf) {
		this.id = id;
		this.dataDivider = dataDivider;
		this.presentationOf = presentationOf;
	}

	public static PGroupCreator withIdDataDividerAndPresentationOf(String id, String dataDivider,
			String presentationOf) {
		return new PGroupCreator(id, dataDivider, presentationOf);
	}

	public SpiderDataGroup createPresentationGroup() {
		SpiderDataGroup presentationGroup = SpiderDataGroup.withNameInData("presentation");

		createAndAddRecordInfo(presentationGroup);
		createAndAddPresentationOf(presentationGroup);
		addAttributeType(presentationGroup);
		
		return presentationGroup;
	}

	private void createAndAddRecordInfo(SpiderDataGroup presentationGroup) {
		SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
		recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", id));

		SpiderDataGroup dataDividerGroup = SpiderDataGroup.withNameInData("dataDivider");
		dataDividerGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDividerGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", dataDivider));

		recordInfo.addChild(dataDividerGroup);
		presentationGroup.addChild(recordInfo);
	}

	private void createAndAddPresentationOf(SpiderDataGroup presentationGroup) {
		SpiderDataGroup presentationOfGroup = SpiderDataGroup.withNameInData("presentationOf");
		presentationOfGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "metadataGroup"));
		presentationOfGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", presentationOf));
		presentationGroup.addChild(presentationOfGroup);
	}

	private void addAttributeType(SpiderDataGroup presentationGroup) {
		presentationGroup.addAttributeByIdWithValue("type", "pGroup");
	}

}
