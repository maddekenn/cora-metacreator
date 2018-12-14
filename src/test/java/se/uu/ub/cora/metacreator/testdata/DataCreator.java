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

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class DataCreator {
	public static SpiderDataGroup createTextVarGroupWithIdAndTextIdAndDefTextId(String id,
			String textId, String defTextId) {
		SpiderDataGroup textVarGroup = createGroupWithIdAndNameInDataAndDataDivider(id, "textVar",
				"cora");

		textVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("nameInData", "my"));
		if (!"".equals(textId)) {
			addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(textVarGroup, "textId",
					"someType", textId);
		}
		if (!"".equals(defTextId)) {
			addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(textVarGroup,
					"defTextId", "someType", defTextId);
		}
		textVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("regEx", ".*"));

		textVarGroup.addAttributeByIdWithValue("type", "textVariable");
		return textVarGroup;
	}

	public static SpiderDataGroup createSpiderDataGroupForRecordTypeWithId(String id) {
		SpiderDataGroup recordType = createGroupWithIdAndNameInDataAndDataDivider(id, "recordType",
				"test");
		recordType.addChild(SpiderDataAtomic.withNameInDataAndValue("abstract", "false"));
		recordType.addChild(SpiderDataAtomic.withNameInDataAndValue("userSuppliedId", "true"));
		return recordType;
	}

	private static SpiderDataGroup createGroupWithIdAndNameInDataAndDataDivider(String id,
			String nameInData, String dataDividerString) {
		SpiderDataGroup recordType = SpiderDataGroup.withNameInData(nameInData);
		SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
		recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", id));
		recordType.addChild(recordInfo);
		SpiderDataGroup dataDivider = SpiderDataGroup.withNameInData("dataDivider");
		dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", dataDividerString));
		recordInfo.addChild(dataDivider);
		return recordType;
	}

	public static void addAllValuesToSpiderDataGroup(SpiderDataGroup recordType, String id) {
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
	}

	private static void addAtomicValueWithNameInDataAndValue(SpiderDataGroup spiderDataGroup,
			String nameInData, String value) {
		spiderDataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue(nameInData, value));
	}

	public static void addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(
			SpiderDataGroup spiderDataGroup, String nameInData, String type, String id) {
		SpiderDataGroup link = SpiderDataGroup.withNameInData(nameInData);
		link.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", type));
		link.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", id));
		spiderDataGroup.addChild(link);
	}

	public static SpiderDataGroup createCollectionItemGroupWithIdTextIdDefTextIdAndImplementingTextType(
			String id, String textId, String defTextId, String implementingTextType) {
		SpiderDataGroup item = SpiderDataGroup.withNameInData("metadata");

		SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
		recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", id));

		SpiderDataGroup dataDivider = SpiderDataGroup.withNameInData("dataDivider");
		dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", "test"));

		recordInfo.addChild(dataDivider);
		item.addChild(recordInfo);

		String nameInData = id.substring(0, id.indexOf("Item"));
		item.addChild(SpiderDataAtomic.withNameInDataAndValue("nameInData", nameInData));

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

	public static SpiderDataGroup createItemCollectionWithId(String id) {
		SpiderDataGroup itemCollection = createGroupWithIdAndNameInDataAndDataDivider(id,
				"metadata", "test");
		itemCollection
				.addChild(SpiderDataAtomic.withNameInDataAndValue("nameInData", "someNameInData"));

		SpiderDataGroup itemReferences = SpiderDataGroup.withNameInData("collectionItemReferences");

		SpiderDataGroup ref1 = createItemRefWithLinkedIdAndRepeatId("firstItem", "0");
		itemReferences.addChild(ref1);

		SpiderDataGroup ref2 = createItemRefWithLinkedIdAndRepeatId("secondItem", "1");
		itemReferences.addChild(ref2);

		SpiderDataGroup ref3 = createItemRefWithLinkedIdAndRepeatId("thirdItem", "2");
		itemReferences.addChild(ref3);

		itemCollection.addChild(itemReferences);
		itemCollection.addAttributeByIdWithValue("type", "itemCollection");

		return itemCollection;
	}

	public static SpiderDataGroup createItemRefWithLinkedIdAndRepeatId(String itemId,
			String repeatId) {
		SpiderDataGroup ref = SpiderDataGroup.withNameInData("ref");
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType",
				"genericCollectionItem"));
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", itemId));
		ref.setRepeatId(repeatId);
		return ref;
	}

	public static SpiderDataGroup createCollectionVariableWithIdDataDividerAndNameInData(String id,
			String dataDividerString, String nameInData) {
		SpiderDataGroup collectionVar = createGroupWithIdAndNameInDataAndDataDivider(id, "metadata",
				dataDividerString);
		collectionVar.addChild(SpiderDataAtomic.withNameInDataAndValue("nameInData", nameInData));
		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(collectionVar, "textId",
				"text", id + "Text");
		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(collectionVar, "defTextId",
				"text", id + "DefText");

		collectionVar.addAttributeByIdWithValue("type", "collectionVariable");

		return collectionVar;
	}

	public static SpiderDataGroup createRecordLinkWithIdDataDividerNameInDataAndLinkedRecordType(
			String id, String dataDividerString, String nameInData, String linkedRecordType) {

		SpiderDataGroup recordLink = createGroupWithIdAndNameInDataAndDataDivider(id, "metadata",
				dataDividerString);
		recordLink.addChild(SpiderDataAtomic.withNameInDataAndValue("nameInData", nameInData));
		recordLink.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordLink, "textId",
				"text", id + "Text");
		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordLink, "defTextId",
				"text", id + "DefText");

		recordLink.addAttributeByIdWithValue("type", "recordLink");

		return recordLink;
	}

	public static SpiderDataGroup createSearchWithId(String id) {
		SpiderDataGroup search = createGroupWithIdAndNameInDataAndDataDivider(id, "search", "test");
		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(search, "metadataId",
				"metadataGroup", "autoCompleteSearchGroup");
		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(search, "presentationId",
				"presentationGroup", "autocompleteSearchPGroup");
		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(search,
				"recordTypeToSearchIn", "recordType", "metadataItemCollection");
		SpiderDataGroup recordTypeToSearchIn = search.extractGroup("recordTypeToSearchIn");
		recordTypeToSearchIn.setRepeatId("0");

		return search;
	}

	public static SpiderDataGroup createMetadataGroupWithIdAndTextVarAsChildReference(String id) {
		SpiderDataGroup metadataGroup = createGroupWithIdAndNameInDataAndDataDivider(id, "metadata",
				"test");
		metadataGroup.addAttributeByIdWithValue("type", "group");
		metadataGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue("nameInData", "someGroupNameInData"));

		SpiderDataGroup childReferences = SpiderDataGroup.withNameInData("childReferences");

		SpiderDataGroup childReference = SpiderDataGroup.withNameInData("childReference");
		childReference.setRepeatId("0");
		childReference.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMin", "0"));
		childReference.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMax", "1"));

		DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(childReference,
				"ref", "metadata", "someTextVar");

		childReferences.addChild(childReference);
		metadataGroup.addChild(childReferences);

		return metadataGroup;
	}

	public static SpiderDataGroup createMetadataGroupWithId(String id) {
		SpiderDataGroup metadataGroup = createGroupWithIdAndNameInDataAndDataDivider(id, "metadata",
				"test");
		metadataGroup.addAttributeByIdWithValue("type", "group");
		metadataGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue("nameInData", "someGroupNameInData"));

		SpiderDataGroup childReferences = SpiderDataGroup.withNameInData("childReferences");

		metadataGroup.addChild(childReferences);

		return metadataGroup;
	}

	public static SpiderDataGroup createMetadataRecordLinkWithId(String id) {
		SpiderDataGroup metadataRecordLink = createGroupWithIdAndNameInDataAndDataDivider(id,
				"metadata", "test");
		metadataRecordLink.addAttributeByIdWithValue("type", "recordLink");
		metadataRecordLink.addChild(
				SpiderDataAtomic.withNameInDataAndValue("nameInData", "someRecordLinkNameInData"));
		metadataRecordLink.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "someRecordType"));

		return metadataRecordLink;
	}

	public static SpiderDataGroup createNumberVarUsingIdNameInDataAndDataDivider(String id,
			String nameInData, String dataDividerString) {
		SpiderDataGroup numberVarGroup = DataCreator
				.createGroupWithIdAndNameInDataAndDataDivider(id, nameInData, dataDividerString);

		numberVarGroup
				.addChild(SpiderDataAtomic.withNameInDataAndValue("nameInData", "someNameInData"));
		numberVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("min", "0"));
		numberVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("max", "20"));
		numberVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("warningMin", "2"));
		numberVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("warningMax", "10"));
		numberVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("numberOfDecimals", "0"));
		return numberVarGroup;
	}
}
