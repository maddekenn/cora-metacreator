package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PresentationGroupCreator extends GroupCreator {

	private static final String PRESENTATION = "presentation";
	private String presentationOf;

	public PresentationGroupCreator(String id, String dataDivider, String presentationOf) {
		super(id, dataDivider);
		this.presentationOf = presentationOf;
	}

	public static PresentationGroupCreator withIdDataDividerAndPresentationOf(String id,
			String dataDivider, String presentationOf) {
		return new PresentationGroupCreator(id, dataDivider, presentationOf);
	}

	@Override
	public SpiderDataGroup createGroup(String refRecordInfoId) {
		super.createGroup(refRecordInfoId);
		createAndAddPresentationOf();

		return topLevelSpiderDataGroup;
	}

	private void createAndAddPresentationOf() {
		SpiderDataGroup presentationOfGroup = SpiderDataGroup.withNameInData("presentationOf");
		addLinkedRecordType(presentationOfGroup);
		addLinkedRecordId(presentationOfGroup);
		topLevelSpiderDataGroup.addChild(presentationOfGroup);
	}

	private void addLinkedRecordType(SpiderDataGroup presentationOfGroup) {
		presentationOfGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "metadataGroup"));
	}

	private void addLinkedRecordId(SpiderDataGroup presentationOfGroup) {
		presentationOfGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", presentationOf));
	}

	@Override
	SpiderDataGroup createTopLevelSpiderDataGroup() {
		return SpiderDataGroup.withNameInData(PRESENTATION);
	}

	@Override
	protected void addAttributeType() {
		topLevelSpiderDataGroup.addAttributeByIdWithValue("type", "pGroup");
	}

	@Override
	void addValuesForChildReference(SpiderDataGroup childReference) {
		childReference.addChild(SpiderDataAtomic.withNameInDataAndValue("default", "ref"));
	}

	@Override
	protected void addChildReferencesWithChildId(String refRecordInfoId) {
		SpiderDataGroup childReferences = SpiderDataGroup.withNameInData("childReferences");
		SpiderDataGroup childReference = SpiderDataGroup.withNameInData("childReference");

		SpiderDataGroup refGroup = SpiderDataGroup.withNameInData("refGroup");
		SpiderDataGroup ref = SpiderDataGroup.withNameInData("ref");
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", refRecordInfoId));
		ref.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", PRESENTATION));
		ref.addAttributeByIdWithValue("type", PRESENTATION);
		refGroup.addChild(ref);
		childReference.addChild(refGroup);

		addValuesForChildReference(childReference);
		childReference.setRepeatId("0");
		childReferences.addChild(childReference);
		topLevelSpiderDataGroup.addChild(childReferences);
	}

}
