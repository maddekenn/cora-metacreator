/*
 * Copyright 2017, 2018 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.uu.ub.cora.metacreator.group;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.metacreator.PresentationChildReference;
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

	private int repeatId = 0;

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
		SpiderDataGroup childReferences = SpiderDataGroup.withNameInData("childReferences");
		List<PresentationChildReference> childReferenceList = createChildren(
				metadataChildReferences);

		// TODO: kolla om barnet finns
		for (PresentationChildReference childRef : childReferenceList) {
			try {
				ensurePChildExists(childRef.recordIdentifier);
				createChildReferenceWithRepeatId(getRepeatId());
				childReferences.addChild(childRef.ref);
			} catch (RecordNotFoundException e) {
				// do nothing
			}
		}
		// TODO: lägg på repeatId på childReference

		ensurePGroupHasChildren(childReferenceList);
		return constructPGroupWithChildReferences(childReferenceList);
	}

	private List<PresentationChildReference> createChildren(
			List<SpiderDataElement> metadataChildReferences) {
		SpiderDataGroup childReferences = SpiderDataGroup.withNameInData("childReferences");
		List<PresentationChildReference> presentationChildren = new ArrayList<>();

		for (SpiderDataElement metadataChildReference : metadataChildReferences) {
			List<PresentationChildReference> possibleChildren = possiblyAddChildReferenceAndText(
					(SpiderDataGroup) metadataChildReference);
			presentationChildren.addAll(possibleChildren);
		}

		for (PresentationChildReference presentationChild : presentationChildren) {
			childReferences.addChild(presentationChild.ref);

		}
		// childReferences.addChild(childReference);
		return presentationChildren;

	}

	private List<PresentationChildReference> possiblyAddChildReferenceAndText(
			SpiderDataGroup metadataChildReference) {

		// List<SpiderDataGroup> possibleChildren = new ArrayList<>();
		List<PresentationChildReference> possibleChildren = new ArrayList<>();

		PresentationChildReference textChildReference = createChildReferenceForText(
				metadataChildReference);
		PresentationChildReference childReference = createChild(metadataChildReference);
		if (childReference != null) {
			possibleChildren.add(textChildReference);
			possibleChildren.add(childReference);
		}
		return possibleChildren;
	}

	private PresentationChildReference createChildReferenceForText(
			SpiderDataGroup metadataChildReference) {
		String metadataRefId = getMetadataRefId(metadataChildReference);
		SpiderDataGroup textChildReference = createChildReferenceWithRepeatId(getRepeatId());
		SpiderDataGroup refGroup = createRefGroupAndAddToChildReference(textChildReference);

		RecordIdentifier presRef = RecordIdentifier.usingTypeAndId("coraText",
				metadataRefId + "Text");
		SpiderDataGroup ref = createRef(presRef);
		ref.addAttributeByIdWithValue("type", "text");
		refGroup.addChild(ref);
		return PresentationChildReference.usingRefGroupAndRecordIdentifier(refGroup, presRef);

		// return presentationChildRef;
	}

	private String getRepeatId() {
		int currentRepeatId = repeatId;
		repeatId++;
		return String.valueOf(currentRepeatId);
	}

	private PresentationChildReference createChild(SpiderDataGroup metadataChildReference) {
		// try {
		return createChildReference(metadataChildReference);
		// } catch (DataException | RecordNotFoundException e) {
		// return null;
		// }
	}

	private PresentationChildReference createChildReference(
			SpiderDataGroup metadataChildReference) {
		SpiderDataGroup childReference = createChildReferenceWithRepeatId(getRepeatId());
		SpiderDataGroup refGroup = createRefGroupAndAddToChildReference(childReference);

		SpiderDataGroup ref = createRefAndAddToRefGroup(metadataChildReference, refGroup);
		refGroup.addChild(ref);

		RecordIdentifier recordIdentifier = null;
		// set type and id
		return PresentationChildReference.usingRefGroupAndRecordIdentifier(refGroup,
				recordIdentifier);

	}

	private SpiderDataGroup createChildReferenceWithRepeatId(String repeatId) {
		SpiderDataGroup childReference = SpiderDataGroup.withNameInData("childReference");
		createRefGroupAndAddToChildReference(childReference);
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

	private SpiderDataGroup createRefAndAddToRefGroup(SpiderDataGroup metadataChildReference,
			SpiderDataGroup refGroup) {
		SpiderDataGroup ref = possiblyCreateRef(metadataChildReference);
		return ref;

	}

	private SpiderDataGroup possiblyCreateRef(SpiderDataGroup metadataChildReference) {
		PresentationChildReference presRef = constructPresRefIdFromMetadataChild(
				metadataChildReference);

		// ensurePChildExists(presRef);

		return presRef.ref;
	}

	private PresentationChildReference constructPresRefIdFromMetadataChild(
			SpiderDataGroup metadataChildReference) {
		String metadataRefId = getMetadataRefId(metadataChildReference);
		if (metadataChildIsCollectionVar(metadataRefId)) {
			return constructChildAsPCollVar(metadataChildReference);
		} else if (metadataChildIsTextVariable(metadataRefId)) {
			return constructChildAsPVar(metadataChildReference);
		} else if (metadataChildIsResourceLink(metadataRefId)) {
			return constructChildAsPResourceLink(metadataChildReference);
		} else if (metadataChildIsRecordLink(metadataRefId)) {
			return constructChildAsPRecordLink(metadataChildReference);
		} else if (metadataChildIsGroup(metadataRefId)) {
			return constructChildAsPGroup(metadataChildReference);
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

	private PresentationChildReference constructChildAsPCollVar(
			SpiderDataGroup metadataChildReference) {
		ChildRefConstructor constructor = PCollVarChildRefConstructor
				.usingMetadataChildReferenceAndMode(metadataChildReference, mode);
		return constructor.getChildRef();
	}

	private boolean metadataChildIsTextVariable(String metadataRefId) {
		return metadataRefId.endsWith("TextVar");
	}

	private PresentationChildReference constructChildAsPVar(
			SpiderDataGroup metadataChildReference) {
		ChildRefConstructor constructor = PVarChildRefConstructor
				.usingMetadataChildReferenceAndMode(metadataChildReference, mode);
		return constructor.getChildRef();
	}

	private boolean metadataChildIsResourceLink(String metadataRefId) {
		return metadataRefId.endsWith("ResLink");
	}

	private PresentationChildReference constructChildAsPResourceLink(
			SpiderDataGroup metadataChildReference) {
		ChildRefConstructor constructor = PResLinkChildRefConstructor
				.usingMetadataChildReferenceAndMode(metadataChildReference, mode);
		return constructor.getChildRef();
	}

	private boolean metadataChildIsRecordLink(String metadataRefId) {
		return metadataRefId.endsWith("Link");
	}

	private PresentationChildReference constructChildAsPRecordLink(
			SpiderDataGroup metadataChildReference) {
		ChildRefConstructor constructor = PLinkChildRefConstructor
				.usingMetadataChildReferenceAndMode(metadataChildReference, mode);
		return constructor.getChildRef();
	}

	private boolean metadataChildIsGroup(String metadataRefId) {
		return metadataRefId.endsWith("Group");
	}

	private PresentationChildReference constructChildAsPGroup(
			SpiderDataGroup metadataChildReference) {
		ChildRefConstructor constructor = PGroupChildRefConstructor
				.usingMetadataChildReferenceAndMode(metadataChildReference, mode);
		return constructor.getChildRef();
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
