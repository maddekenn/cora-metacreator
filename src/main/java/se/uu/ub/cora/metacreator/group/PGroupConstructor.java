package se.uu.ub.cora.metacreator.group;

import java.util.List;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.record.DataException;

public class PGroupConstructor {

	private static final String LINKED_RECORD_ID = "linkedRecordId";
	private SpiderDataGroup pGroup;
	private String mode;

	public SpiderDataGroup constructPGroupWithIdDataDividerPresentationOfAndChildren(String id,
			String dataDivider, String presentationOf,
			List<SpiderDataElement> metadataChildReferences, String mode) {
		this.mode = mode;
		SpiderDataGroup childReferences = createChildren(metadataChildReferences);
		ensurePGroupHasChildren(childReferences);
		createPGroup(id, dataDivider, presentationOf, childReferences);

		return pGroup;
	}

	private void createPGroup(String id, String dataDivider, String presentationOf,
			SpiderDataGroup childReferences) {
		pGroup = SpiderDataGroup.withNameInData("presentation");
		pGroup.addAttributeByIdWithValue("type", "pGroup");
		pGroup.addChild(childReferences);
		createAndAddRecordInfoWithIdAndDataDivider(id, dataDivider);
		createAndAddPresentationOf(presentationOf);
	}

	private void ensurePGroupHasChildren(SpiderDataGroup childReferences) {
		if (!childReferences.containsChildWithNameInData("childReference")) {
			throw new DataException("No children were possible to add to presenationGroup");
		}
	}

	private void createAndAddRecordInfoWithIdAndDataDivider(String id, String dataDivider) {
		SpiderDataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider(id,
				dataDivider);
		pGroup.addChild(recordInfo);
	}

	private void createAndAddPresentationOf(String presentationOf) {
		SpiderDataGroup presentationOfGroup = SpiderDataGroup.withNameInData("presentationOf");
		presentationOfGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "metadataGroup"));
		presentationOfGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue(LINKED_RECORD_ID, presentationOf));
		pGroup.addChild(presentationOfGroup);
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
			SpiderDataGroup childReference = SpiderDataGroup.withNameInData("childReference");
			childReference.setRepeatId(metadataChildReference.getRepeatId());

			SpiderDataGroup ref = createRef(metadataChildReference);

			childReference.addChild(ref);
			return childReference;
		} catch (DataException e) {
			return null;
		}
	}

	private SpiderDataGroup createRef(SpiderDataGroup metadataChildReference) {
		SpiderDataGroup ref = SpiderDataGroup.withNameInData("ref");
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "presentation"));

		String presRefId = constructPresRefIdFromMetadataChild(metadataChildReference);
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue(LINKED_RECORD_ID, presRefId));
		return ref;
	}

	private String getMetadataRefId(SpiderDataGroup metadataChildReference) {
		SpiderDataGroup metadataRef = metadataChildReference.extractGroup("ref");
		return metadataRef.extractAtomicValue(LINKED_RECORD_ID);
	}

	private String constructPresRefIdFromMetadataChild(SpiderDataGroup metadataChildReference) {
		String metadataRefId = getMetadataRefId(metadataChildReference);

		if (metadataChildIsCollectionVar(metadataRefId)) {
			return constructPCollVarRefId(metadataRefId);
		} else if (metadataChildIsTextVariable(metadataRefId)) {
			return constructPVarRefId(metadataRefId);
		} else if (metadataChildIsResourceLink(metadataRefId)) {
			return constructPResLinkRefId(metadataRefId);
		} else if (metadataChildIsRecordLink(metadataRefId)) {
			return constructPLinkRefId(metadataRefId);
		} else if (metadataChildIsGroup(metadataRefId)) {
			return constructPGroupRefId(metadataRefId);
		}
		throw new DataException("Not possible to construct childReferenceId from metadatId");
	}

	private boolean metadataChildIsCollectionVar(String metadataRefId) {
		return metadataRefId.endsWith("CollectionVar");
	}

	private String constructPCollVarRefId(String metadataRefId) {
		String idPrefix = metadataRefId.substring(0, metadataRefId.indexOf("CollectionVar"));
		idPrefix += possibleOutputString();
		return idPrefix.concat("PCollVar");
	}

	private String possibleOutputString() {
		if ("output".equals(mode)) {
			return "Output";
		}
		return "";
	}

	private boolean metadataChildIsTextVariable(String metadataRefId) {
		return metadataRefId.endsWith("TextVar");
	}

	private String constructPVarRefId(String metadataRefId) {
		String idPrefix = metadataRefId.substring(0, metadataRefId.indexOf("TextVar"));
		idPrefix += possibleOutputString();
		return idPrefix.concat("PVar");
	}

	private boolean metadataChildIsResourceLink(String metadataRefId) {
		return metadataRefId.endsWith("ResLink");
	}

	private String constructPResLinkRefId(String metadataRefId) {
		String idPrefix = metadataRefId.substring(0, metadataRefId.indexOf("ResLink"));
		idPrefix += possibleOutputString();
		return idPrefix.concat("PResLink");
	}

	private boolean metadataChildIsRecordLink(String metadataRefId) {
		return metadataRefId.endsWith("Link");
	}

	private String constructPLinkRefId(String metadataRefId) {
		String idPrefix = metadataRefId.substring(0, metadataRefId.indexOf("Link"));
		idPrefix += possibleOutputString();
		return idPrefix.concat("PLink");
	}

	private boolean metadataChildIsGroup(String metadataRefId) {
		return metadataRefId.endsWith("Group");
	}

	private String constructPGroupRefId(String metadataRefId) {
		String idPrefix = metadataRefId.substring(0, metadataRefId.indexOf("Group"));
		idPrefix += possibleOutputString();
		return idPrefix.concat("PGroup");
	}

}
