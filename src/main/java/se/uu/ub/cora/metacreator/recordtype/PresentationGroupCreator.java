package se.uu.ub.cora.metacreator.recordtype;

public class PresentationGroupCreator{

	private static final String PRESENTATION = "presentation";
	private String presentationOf;
	public String presentationId;

	public PresentationGroupCreator(String presentationId, String dataDivider) {
		this.presentationId = presentationId;
		this.presentationOf = presentationOf;
	}

	public static PresentationGroupCreator withIdDataDividerAndPresentationOf(String id,
			String dataDivider, String presentationOf) {
		return new PresentationGroupCreator(id, dataDivider);
	}

//	@Override
//	public SpiderDataGroup createGroup(String refRecordInfoId) {
//		super.createGroup(refRecordInfoId);
//		createAndAddPresentationOf();
//
//		return topLevelSpiderDataGroup;
//	}
//
//	private void createAndAddPresentationOf() {
//		SpiderDataGroup presentationOfGroup = SpiderDataGroup.withNameInData("presentationOf");
//		addLinkedRecordType(presentationOfGroup);
//		addLinkedRecordId(presentationOfGroup);
//		topLevelSpiderDataGroup.addChild(presentationOfGroup);
//	}
//
//	private void addLinkedRecordType(SpiderDataGroup presentationOfGroup) {
//		presentationOfGroup.addChild(
//				SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "metadataGroup"));
//	}
//
//	private void addLinkedRecordId(SpiderDataGroup presentationOfGroup) {
//		presentationOfGroup.addChild(
//				SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", presentationOf));
//	}
//
//	@Override
//	SpiderDataGroup createTopLevelSpiderDataGroup() {
//		return SpiderDataGroup.withNameInData(PRESENTATION);
//	}
//
//	@Override
//	protected void addAttributeType() {
//		topLevelSpiderDataGroup.addAttributeByIdWithValue("type", "pGroup");
//	}
//
//	@Override
//	protected void addChildReferencesWithChildId(String refRecordInfoId) {
//		SpiderDataGroup childReferences = SpiderDataGroup.withNameInData("childReferences");
//		SpiderDataGroup childReference = SpiderDataGroup.withNameInData("childReference");
//
//		SpiderDataGroup refGroup = createRefGroup(refRecordInfoId);
//		childReference.addChild(refGroup);
//
//		addValuesForChildReference(childReference);
//		childReferences.addChild(childReference);
//		topLevelSpiderDataGroup.addChild(childReferences);
//	}
//
//	@Override
//	void addValuesForChildReference(SpiderDataGroup childReference) {
//		childReference.setRepeatId("0");
//	}
//
//	private SpiderDataGroup createRefGroup(String refRecordInfoId) {
//		SpiderDataGroup refGroup = SpiderDataGroup.withNameInData("refGroup");
//		refGroup.setRepeatId("0");
//		SpiderDataGroup ref = createRefPartOfRefGroup(refRecordInfoId);
//		refGroup.addChild(ref);
//		return refGroup;
//	}
//
//	private SpiderDataGroup createRefPartOfRefGroup(String refRecordInfoId) {
//		SpiderDataGroup ref = SpiderDataGroup.withNameInData("ref");
//		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", refRecordInfoId));
//		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", PRESENTATION));
//		ref.addAttributeByIdWithValue("type", PRESENTATION);
//		return ref;
//	}

	public static PresentationGroupCreator withPresentationIdAndDataDivider(String presentationId, String dataDivider) {
		return new PresentationGroupCreator(presentationId, dataDivider);
	}
}
