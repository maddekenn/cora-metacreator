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
import se.uu.ub.cora.storage.RecordNotFoundException;

public final class PGroupConstructor {

	private static final String CHILD_REFERENCE = "childReference";
	private static final String LINKED_RECORD_ID = "linkedRecordId";
	private static final String PRESENTATION = "presentation";

	private String mode;
	private String authToken;
	private String id;
	private String dataDivider;
	private String presentationOf;

	private int repeatId = 0;
	private PChildRefConstructorFactory pChildRefConstructorFactory;

	private PGroupConstructor(String authToken,
			PChildRefConstructorFactory pChildRefConstructorFactory) {
		this.authToken = authToken;
		this.pChildRefConstructorFactory = pChildRefConstructorFactory;
	}

	public static PGroupConstructor usingAuthTokenAndPChildRefConstructorFactory(String authToken,
			PChildRefConstructorFactory constructorFactory) {
		return new PGroupConstructor(authToken, constructorFactory);
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
		SpiderDataGroup childReferences = createChildReferencesDataGroup(metadataChildReferences);
		throwExceptionIfPGroupHasNoChildren(childReferences);
		return constructPGroupWithChildReferences(childReferences);
	}

	private SpiderDataGroup createChildReferencesDataGroup(
			List<SpiderDataElement> metadataChildReferences) {
		List<PresentationChildReference> childReferenceList = createChildren(
				metadataChildReferences);

		SpiderDataGroup childReferences = SpiderDataGroup.withNameInData("childReferences");
		createChildReferences(childReferences, childReferenceList);
		return childReferences;
	}

	private List<PresentationChildReference> createChildren(
			List<SpiderDataElement> metadataChildReferences) {
		List<PresentationChildReference> presentationChildren = new ArrayList<>();

		for (SpiderDataElement metadataChildReference : metadataChildReferences) {
			try {
				PChildRefConstructor constructor = getConstructorFromMetadataChild(
						(SpiderDataGroup) metadataChildReference);

				List<PresentationChildReference> possibleChildren = possiblyAddChildReferenceAndText(
						(SpiderDataGroup) metadataChildReference, constructor);
				presentationChildren.addAll(possibleChildren);
			} catch (DataException e) {
				// do nothing
			}
		}

		return presentationChildren;
	}

	private PChildRefConstructor getConstructorFromMetadataChild(
			SpiderDataGroup metadataChildReference) {
		return pChildRefConstructorFactory.factor(metadataChildReference, mode);
	}

	private String getMetadataRefId(SpiderDataGroup metadataChildReference) {
		SpiderDataGroup metadataRef = metadataChildReference.extractGroup("ref");
		return metadataRef.extractAtomicValue(LINKED_RECORD_ID);
	}

	private void createChildReferences(SpiderDataGroup childReferences,
			List<PresentationChildReference> childReferenceList) {
		for (PresentationChildReference childRef : childReferenceList) {
			try {
				possiblyCreateChildReference(childReferences, childRef);
			} catch (RecordNotFoundException e) {
				// do nothing
			}
		}
	}

	private List<PresentationChildReference> possiblyAddChildReferenceAndText(
			SpiderDataGroup metadataChildReference, PChildRefConstructor constructor) {

		List<PresentationChildReference> possibleChildren = new ArrayList<>();

		PresentationChildReference textChildReference = createChildReferenceForText(
				metadataChildReference);
		possibleChildren.add(textChildReference);
		PresentationChildReference childReference = constructor.getChildRef();
		possibleChildren.add(childReference);
		return possibleChildren;
	}

	private void possiblyCreateChildReference(SpiderDataGroup childReferences,
			PresentationChildReference childRef) {
		ensurePChildExists(childRef.recordIdentifier);
		SpiderDataGroup childReference = createChildReference(childRef);
		childReferences.addChild(childReference);
	}

	private PresentationChildReference createChildReferenceForText(
			SpiderDataGroup metadataChildReference) {
		String metadataRefId = getMetadataRefId(metadataChildReference);

		RecordIdentifier presRef = RecordIdentifier.usingTypeAndId("coraText",
				metadataRefId + "Text");
		SpiderDataGroup ref = createRef(presRef);
		ref.addAttributeByIdWithValue("type", "text");
		return PresentationChildReference.usingRefGroupAndRecordIdentifier(ref, presRef);
	}

	private SpiderDataGroup createChildReference(PresentationChildReference childRef) {
		SpiderDataGroup refGroup = createRefGroup();
		refGroup.addChild(childRef.ref);

		SpiderDataGroup childReference = SpiderDataGroup.withNameInData(CHILD_REFERENCE);
		childReference.setRepeatId(getRepeatId());
		childReference.addChild(refGroup);
		return childReference;
	}

	private String getRepeatId() {
		int currentRepeatId = repeatId;
		repeatId++;
		return String.valueOf(currentRepeatId);
	}

	private SpiderDataGroup createRefGroup() {
		SpiderDataGroup refGroup = SpiderDataGroup.withNameInData("refGroup");
		refGroup.setRepeatId("0");
		return refGroup;
	}

	private void ensurePChildExists(RecordIdentifier pChild) {
		SpiderRecordReader reader = SpiderInstanceProvider.getSpiderRecordReader();
		reader.readRecord(authToken, pChild.type, pChild.id);
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

	private void throwExceptionIfPGroupHasNoChildren(SpiderDataGroup childReferences) {
		if (!childReferences.containsChildWithNameInData(CHILD_REFERENCE)) {
			throw new DataException("No children were possible to add to presentationGroup for id"
					+ id + " and presentationOf " + presentationOf);
		}
	}

	private SpiderDataGroup constructPGroupWithChildReferences(SpiderDataGroup childReferences) {
		SpiderDataGroup pGroup = constructPGroup();
		addChildReferencesToPGroup(childReferences, pGroup);
		return pGroup;
	}

	private void addChildReferencesToPGroup(SpiderDataElement childReferences,
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

	public PChildRefConstructorFactory getPChildRefConstructorFactory() {
		return pChildRefConstructorFactory;
	}
}
