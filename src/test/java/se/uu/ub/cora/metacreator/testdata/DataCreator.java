/*
 * Copyright 2016 Olov McKie
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

package se.uu.ub.cora.metacreator.testdata;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;

public class DataCreator {
	public static DataGroup createTextVarGroupWithIdAndTextIdAndDefTextId(String id, String textId,
			String defTextId) {
		DataGroup textVarGroup = createGroupWithIdAndNameInDataAndDataDivider(id, "textVar",
				"cora");

		textVarGroup.addChild(DataAtomic.withNameInDataAndValue("nameInData", "my"));
		if (!"".equals(textId)) {
			addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(textVarGroup, "textId",
					"someType", textId);
		}
		if (!"".equals(defTextId)) {
			addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(textVarGroup,
					"defTextId", "someType", defTextId);
		}
		textVarGroup.addChild(DataAtomic.withNameInDataAndValue("regEx", ".*"));

		textVarGroup.addAttributeByIdWithValue("type", "textVariable");
		return textVarGroup;
	}

	public static DataGroup createDataGroupForRecordTypeWithId(String id) {
		DataGroup recordType = createGroupWithIdAndNameInDataAndDataDivider(id, "recordType",
				"test");
		recordType.addChild(DataAtomic.withNameInDataAndValue("abstract", "false"));
		recordType.addChild(DataAtomic.withNameInDataAndValue("userSuppliedId", "true"));
		return recordType;
	}

	private static DataGroup createGroupWithIdAndNameInDataAndDataDivider(String id,
			String nameInData, String dataDividerString) {
		DataGroup recordType = DataGroup.withNameInData(nameInData);
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", id));
		recordType.addChild(recordInfo);
		DataGroup dataDivider = DataGroup.withNameInData("dataDivider");
		dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider
				.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", dataDividerString));
		recordInfo.addChild(dataDivider);
		return recordType;
	}

	public static void addAllValuesToDataGroup(DataGroup recordType, String id) {
		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType, "metadataId",
				"metadataGroup", id + "Group");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType, "newMetadataId",
				"metadataGroup", id + "NewGroup");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType,
				"presentationViewId", "presentationGroup", id + "OutputPGroup");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType,
				"presentationFormId", "presentationGroup", id + "PGroup");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType,
				"newPresentationFormId", "presentationGroup", id + "NewPGroup");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType,
				"menuPresentationViewId", "presentationGroup", id + "MenuPGroup");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType,
				"listPresentationViewId", "presentationGroup", id + "ListPGroup");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType,
				"autocompletePresentationView", "presentationGroup", id + "AutocompletePGroup");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType, "textId",
				"implementingText", id + "Text");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType, "defTextId",
				"implementingText", id + "DefText");
		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType, "search",
				"search", id + "Search");
		recordType.addChild(DataAtomic.withNameInDataAndValue("public", "no"));

	}

	public static void addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(
			DataGroup dataGroup, String nameInData, String type, String id) {
		DataGroup link = DataGroup.withNameInData(nameInData);
		link.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", type));
		link.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", id));
		dataGroup.addChild(link);
	}

	public static DataGroup createCollectionItemGroupWithIdTextIdDefTextIdAndImplementingTextType(
			String id, String textId, String defTextId, String implementingTextType) {
		DataGroup item = DataGroup.withNameInData("metadata");

		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", id));

		DataGroup dataDivider = DataGroup.withNameInData("dataDivider");
		dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "test"));

		recordInfo.addChild(dataDivider);
		item.addChild(recordInfo);

		String nameInData = id.substring(0, id.indexOf("Item"));
		item.addChild(DataAtomic.withNameInDataAndValue("nameInData", nameInData));

		if (!"".equals(textId)) {
			DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(item,
					"textId", implementingTextType, textId);
		}
		if (!"".equals(defTextId)) {
			DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(item,
					"defTextId", implementingTextType, defTextId);
		}
		return item;
	}

	public static DataGroup createItemCollectionWithId(String id) {
		DataGroup itemCollection = createGroupWithIdAndNameInDataAndDataDivider(id, "metadata",
				"test");
		itemCollection.addChild(DataAtomic.withNameInDataAndValue("nameInData", "someNameInData"));

		DataGroup itemReferences = DataGroup.withNameInData("collectionItemReferences");

		DataGroup ref1 = createItemRefWithLinkedIdAndRepeatId("firstItem", "0");
		itemReferences.addChild(ref1);

		DataGroup ref2 = createItemRefWithLinkedIdAndRepeatId("secondItem", "1");
		itemReferences.addChild(ref2);

		DataGroup ref3 = createItemRefWithLinkedIdAndRepeatId("thirdItem", "2");
		itemReferences.addChild(ref3);

		itemCollection.addChild(itemReferences);
		itemCollection.addAttributeByIdWithValue("type", "itemCollection");

		return itemCollection;
	}

	public static DataGroup createItemRefWithLinkedIdAndRepeatId(String itemId, String repeatId) {
		DataGroup ref = DataGroup.withNameInData("ref");
		ref.addChild(
				DataAtomic.withNameInDataAndValue("linkedRecordType", "genericCollectionItem"));
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", itemId));
		ref.setRepeatId(repeatId);
		return ref;
	}

	public static DataGroup createCollectionVariableWithIdDataDividerAndNameInData(String id,
			String dataDividerString, String nameInData) {
		DataGroup collectionVar = createGroupWithIdAndNameInDataAndDataDivider(id, "metadata",
				dataDividerString);
		collectionVar.addChild(DataAtomic.withNameInDataAndValue("nameInData", nameInData));
		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(collectionVar, "textId",
				"text", id + "Text");
		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(collectionVar, "defTextId",
				"text", id + "DefText");

		collectionVar.addAttributeByIdWithValue("type", "collectionVariable");

		return collectionVar;
	}

	public static DataGroup createRecordLinkWithIdDataDividerNameInDataAndLinkedRecordType(
			String id, String dataDividerString, String nameInData, String linkedRecordType) {

		DataGroup recordLink = createGroupWithIdAndNameInDataAndDataDivider(id, "metadata",
				dataDividerString);
		recordLink.addChild(DataAtomic.withNameInDataAndValue("nameInData", nameInData));
		recordLink
				.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordLink, "textId",
				"text", id + "Text");
		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordLink, "defTextId",
				"text", id + "DefText");

		recordLink.addAttributeByIdWithValue("type", "recordLink");

		return recordLink;
	}

	public static DataGroup createSearchWithId(String id) {
		DataGroup search = createGroupWithIdAndNameInDataAndDataDivider(id, "search", "test");
		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(search, "metadataId",
				"metadataGroup", "autoCompleteSearchGroup");
		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(search, "presentationId",
				"presentationGroup", "autocompleteSearchPGroup");
		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(search,
				"recordTypeToSearchIn", "recordType", "metadataItemCollection");
		DataGroup recordTypeToSearchIn = search.getFirstGroupWithNameInData("recordTypeToSearchIn");
		recordTypeToSearchIn.setRepeatId("0");

		return search;
	}

	public static DataGroup createMetadataGroupWithIdAndTextVarAsChildReference(String id) {
		DataGroup metadataGroup = createGroupWithIdAndNameInDataAndDataDivider(id, "metadata",
				"test");
		metadataGroup.addAttributeByIdWithValue("type", "group");
		metadataGroup
				.addChild(DataAtomic.withNameInDataAndValue("nameInData", "someGroupNameInData"));

		DataGroup childReferences = DataGroup.withNameInData("childReferences");

		DataGroup childReference = DataGroup.withNameInData("childReference");
		childReference.setRepeatId("0");
		childReference.addChild(DataAtomic.withNameInDataAndValue("repeatMin", "0"));
		childReference.addChild(DataAtomic.withNameInDataAndValue("repeatMax", "1"));

		DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(childReference,
				"ref", "metadata", "someTextVar");

		childReferences.addChild(childReference);
		metadataGroup.addChild(childReferences);

		return metadataGroup;
	}

	public static DataGroup createMetadataGroupWithId(String id) {
		DataGroup metadataGroup = createGroupWithIdAndNameInDataAndDataDivider(id, "metadata",
				"test");
		metadataGroup.addAttributeByIdWithValue("type", "group");
		metadataGroup
				.addChild(DataAtomic.withNameInDataAndValue("nameInData", "someGroupNameInData"));

		DataGroup childReferences = DataGroup.withNameInData("childReferences");

		metadataGroup.addChild(childReferences);

		return metadataGroup;
	}

	public static DataGroup createMetadataRecordLinkWithId(String id) {
		DataGroup metadataRecordLink = createGroupWithIdAndNameInDataAndDataDivider(id, "metadata",
				"test");
		metadataRecordLink.addAttributeByIdWithValue("type", "recordLink");
		metadataRecordLink.addChild(
				DataAtomic.withNameInDataAndValue("nameInData", "someRecordLinkNameInData"));
		metadataRecordLink
				.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "someRecordType"));

		return metadataRecordLink;
	}

	public static DataGroup createNumberVarUsingIdNameInDataAndDataDivider(String id,
			String nameInData, String dataDividerString) {
		DataGroup numberVarGroup = DataCreator.createGroupWithIdAndNameInDataAndDataDivider(id,
				nameInData, dataDividerString);

		numberVarGroup.addChild(DataAtomic.withNameInDataAndValue("nameInData", "someNameInData"));
		numberVarGroup.addChild(DataAtomic.withNameInDataAndValue("min", "0"));
		numberVarGroup.addChild(DataAtomic.withNameInDataAndValue("max", "20"));
		numberVarGroup.addChild(DataAtomic.withNameInDataAndValue("warningMin", "2"));
		numberVarGroup.addChild(DataAtomic.withNameInDataAndValue("warningMax", "10"));
		numberVarGroup.addChild(DataAtomic.withNameInDataAndValue("numberOfDecimals", "0"));
		return numberVarGroup;
	}
}
