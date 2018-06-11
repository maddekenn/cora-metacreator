package se.uu.ub.cora.metacreator.group;

import java.util.List;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.metacreator.RecordIdentifier;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.record.DataException;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class PGroupConstructor {

	private static final String LINKED_RECORD_ID = "linkedRecordId";
	private static final String PRESENTATION = "presentation";
	private String mode;
	private String authToken;
	private String id;
	private String dataDivider;
	private String presentationOf;

	public PGroupConstructor(String authToken) {
		this.authToken = authToken;
	}

	public SpiderDataGroup constructPGroupWithIdDataDividerPresentationOfChildrenAndMode(String id,
			String dataDivider, String presentationOf,
			List<SpiderDataElement> metadataChildReferences, String mode) {
		this.mode = mode;
		this.id = id;
		this.dataDivider = dataDivider;
		this.presentationOf = presentationOf;
		return possiblyCreatePGroup(metadataChildReferences);

	}

	private SpiderDataGroup possiblyCreatePGroup(List<SpiderDataElement> metadataChildReferences) {
		SpiderDataGroup childReferences = createChildren(metadataChildReferences);
		ensurePGroupHasChildren(childReferences);
		return constructPGroupWithChildReferences(childReferences);
	}

	private SpiderDataGroup createChildren(List<SpiderDataElement> metadataChildReferences) {
		SpiderDataGroup childReferences = SpiderDataGroup.withNameInData("childReferences");

		for (SpiderDataElement metadataChildReference : metadataChildReferences) {
			possiblyAddChildReference(childReferences, (SpiderDataGroup) metadataChildReference);
		}
		return childReferences;

	}

	private void possiblyAddChildReference(SpiderDataGroup childReferences,
			SpiderDataGroup metadataChildReference) {
		SpiderDataGroup childReference = createChild(metadataChildReference);
		if (childReference != null) {
			childReferences.addChild(childReference);
		}
	}

	private SpiderDataGroup createChild(SpiderDataGroup metadataChildReference) {
		try {
			return createChildReference(metadataChildReference);
		} catch (DataException | RecordNotFoundException e) {
			return null;
		}
	}

	private SpiderDataGroup createChildReference(SpiderDataGroup metadataChildReference) {
		SpiderDataGroup childReference = createChildReferenceWithRepeatId(
				metadataChildReference.getRepeatId());
		SpiderDataGroup refGroup = createRefGroupAndAddToChildReference(childReference);

		createRefAndAddToRefGroup(metadataChildReference, refGroup);

		return childReference;
	}

	private SpiderDataGroup createChildReferenceWithRepeatId(String repeatId) {
		SpiderDataGroup childReference = SpiderDataGroup.withNameInData("childReference");
		childReference.setRepeatId(repeatId);
		return childReference;
	}

	private SpiderDataGroup createRefGroupAndAddToChildReference(SpiderDataGroup childReference) {
		SpiderDataGroup refGroup = createRefGroup();
		childReference.addChild(refGroup);
		return refGroup;
	}

	private SpiderDataGroup createRefGroup() {
		SpiderDataGroup refGroup = SpiderDataGroup.withNameInData("refGroup");
		refGroup.setRepeatId("0");
		return refGroup;
	}

	private void createRefAndAddToRefGroup(SpiderDataGroup metadataChildReference,
			SpiderDataGroup refGroup) {
		SpiderDataGroup ref = possiblyCreateRef(metadataChildReference);
		refGroup.addChild(ref);
	}

	private SpiderDataGroup possiblyCreateRef(SpiderDataGroup metadataChildReference) {
		RecordIdentifier presRef = constructPresRefIdFromMetadataChild(metadataChildReference);
		ensurePChildExists(presRef);

		return createRef(presRef);
	}

	private RecordIdentifier constructPresRefIdFromMetadataChild(
			SpiderDataGroup metadataChildReference) {
		String metadataRefId = getMetadataRefId(metadataChildReference);

		String linkedRecordId = "";
		String linkedRecordType = "presentation";

		if (metadataChildIsCollectionVar(metadataRefId)) {
			linkedRecordId = getPChildRefIdFromRefIdUsingTypeAndPChildEnding(metadataRefId,
					"CollectionVar", "PCollVar");
			linkedRecordType = "presentationCollectionVar";
			// recordIdentifier = RecordIdentifier.usingTypeAndId(linkedRecordType,
			// linkedRecordId);
		} else if (metadataChildIsTextVariable(metadataRefId)) {
			linkedRecordId = getPChildRefIdFromRefIdUsingTypeAndPChildEnding(metadataRefId,
					"TextVar", "PVar");
			linkedRecordType = "presentationVar";
			// recordIdentifier = RecordIdentifier.usingTypeAndId(linkedRecordType,
			// linkedRecordId);
		} else if (metadataChildIsResourceLink(metadataRefId)) {
			linkedRecordId = getPChildRefIdFromRefIdUsingTypeAndPChildEnding(metadataRefId,
					"ResLink", "PResLink");
			linkedRecordType = "presentationResourceLink";
			// recordIdentifier = RecordIdentifier.usingTypeAndId(linkedRecordType,
			// linkedRecordId);
		} else if (metadataChildIsRecordLink(metadataRefId)) {
			linkedRecordId = getPChildRefIdFromRefIdUsingTypeAndPChildEnding(metadataRefId, "Link",
					"PLink");
			linkedRecordType = "presentationRecordLink";
			// recordIdentifier = RecordIdentifier.usingTypeAndId(linkedRecordType,
			// linkedRecordId);
		} else if (metadataChildIsGroup(metadataRefId)) {
			linkedRecordId = getPChildRefIdFromRefIdUsingTypeAndPChildEnding(metadataRefId, "Group",
					"PGroup");
			linkedRecordType = "presentationGroup";
			// recordIdentifier = RecordIdentifier.usingTypeAndId(linkedRecordType,
			// linkedRecordId);
		}
		if (!"".equals(linkedRecordId)) {
			return RecordIdentifier.usingTypeAndId(linkedRecordType, linkedRecordId);
		}
		throw new DataException("Not possible to construct childReferenceId from metadataId");
	}

	private String getMetadataRefId(SpiderDataGroup metadataChildReference) {
		SpiderDataGroup metadataRef = metadataChildReference.extractGroup("ref");
		return metadataRef.extractAtomicValue(LINKED_RECORD_ID);
	}

	private boolean metadataChildIsCollectionVar(String metadataRefId) {
		return metadataRefId.endsWith("CollectionVar");
	}

	private String getPChildRefIdFromRefIdUsingTypeAndPChildEnding(String metadataRefId,
			String childType, String presentationIdEnding) {
		String idPrefix = metadataRefId.substring(0, metadataRefId.indexOf(childType));
		idPrefix += possibleOutputString();
		return idPrefix.concat(presentationIdEnding);
	}

	private boolean metadataChildIsTextVariable(String metadataRefId) {
		return metadataRefId.endsWith("TextVar");
	}

	private boolean metadataChildIsResourceLink(String metadataRefId) {
		return metadataRefId.endsWith("ResLink");
	}

	private boolean metadataChildIsRecordLink(String metadataRefId) {
		return metadataRefId.endsWith("Link");
	}

	private boolean metadataChildIsGroup(String metadataRefId) {
		return metadataRefId.endsWith("Group");
	}

	private void ensurePChildExists(RecordIdentifier pChild) {
		SpiderRecordReader reader = SpiderInstanceProvider.getSpiderRecordReader();
		reader.readRecord(authToken, PRESENTATION, pChild.id);
	}

	private SpiderDataGroup createRef(RecordIdentifier presRef) {
		SpiderDataGroup ref = createRefDataGroup();
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", presRef.type));

		ref.addChild(SpiderDataAtomic.withNameInDataAndValue(LINKED_RECORD_ID, presRef.id));
		return ref;
	}

	private SpiderDataGroup createRefDataGroup() {
		SpiderDataGroup ref = SpiderDataGroup.withNameInData("ref");
		ref.addAttributeByIdWithValue("type", PRESENTATION);
		return ref;
	}

	private String possibleOutputString() {
		if ("output".equals(mode)) {
			return "Output";
		}
		return "";
	}

	private void ensurePGroupHasChildren(SpiderDataGroup childReferences) {
		if (!childReferences.containsChildWithNameInData("childReference")) {
			throw new DataException("No children were possible to add to presentationGroup for id"
					+ id + " and presentationOf " + presentationOf);
		}
	}

	private SpiderDataGroup constructPGroupWithChildReferences(SpiderDataGroup childReferences) {
		SpiderDataGroup pGroup = constructPGroup();
		addChildReferencesToPGroup(childReferences, pGroup);
		return pGroup;
	}

	private void addChildReferencesToPGroup(SpiderDataGroup childReferences,
			SpiderDataGroup pGroup) {
		pGroup.addChild(childReferences);
		pGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("mode", mode));
		createAndAddRecordInfoWithIdAndDataDivider(pGroup);
		createAndAddPresentationOf(pGroup);
	}

	private SpiderDataGroup constructPGroup() {
		SpiderDataGroup pGroup = SpiderDataGroup.withNameInData(PRESENTATION);
		pGroup.addAttributeByIdWithValue("type", "pGroup");
		return pGroup;
	}

	private void createAndAddRecordInfoWithIdAndDataDivider(SpiderDataGroup pGroup) {
		SpiderDataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider(id,
				dataDivider);
		pGroup.addChild(recordInfo);
	}

	private void createAndAddPresentationOf(SpiderDataGroup pGroup) {
		SpiderDataGroup presentationOfGroup = SpiderDataGroup.withNameInData("presentationOf");
		presentationOfGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "metadataGroup"));
		presentationOfGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue(LINKED_RECORD_ID, presentationOf));
		pGroup.addChild(presentationOfGroup);
	}

}
