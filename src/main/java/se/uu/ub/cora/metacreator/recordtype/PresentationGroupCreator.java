package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.metacreator.group.PGroupConstructor;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;

import java.util.ArrayList;
import java.util.List;

public class PresentationGroupCreator{

	private static final String PRESENTATION = "presentation";
	private final String authToken;
	public String presentationId;
	private final String dataDivider;
	private String presentationOf;
	private String mode;
	private List<SpiderDataElement> metadataChildReferences;

	public PresentationGroupCreator(String authToken, String presentationId, String dataDivider) {
		this.authToken = authToken;
		this.presentationId = presentationId;
		this.dataDivider = dataDivider;
	}

	public static PresentationGroupCreator withAuthTokenPresentationIdAndDataDivider(String authToken, String presentationId, String dataDivider) {
		return new PresentationGroupCreator(authToken, presentationId, dataDivider);
	}

	public void setPresentationOfAndMode(String presentationOf, String mode) {
		this.presentationOf = presentationOf;
		this.mode = mode;
	}

	public void createGroup() {
//		List<SpiderDataElement> metadataChildren = new ArrayList<>();
		PGroupConstructor pGroupConstructor = new PGroupConstructor(authToken);

		SpiderDataGroup dataGroup = pGroupConstructor.constructPGroupWithIdDataDividerPresentationOfChildrenAndMode(presentationId, dataDivider, presentationOf, metadataChildReferences, mode);
		SpiderRecordCreator spiderRecordCreator = SpiderInstanceProvider
				.getSpiderRecordCreator();
		spiderRecordCreator.createAndStoreRecord(authToken, "presentationGroup",
				dataGroup);
	}

	public void setMetadataChildReferences(List<SpiderDataElement> metadataChildReferences) {
		this.metadataChildReferences = metadataChildReferences;
	}

//	public static PresentationGroupCreator withIdDataDividerAndPresentationOf(String id,
//			String dataDivider, String presentationOf) {
//		return new PresentationGroupCreator(id, dataDivider);
//	}

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

}
