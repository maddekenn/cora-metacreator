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
		SpiderDataGroup textVarGroup = createGroupWithIdAndNameInDataAndDataDivider(id, "textVar", "cora");

		textVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("nameInData", "my"));
		if (!"".equals(textId)) {
			textVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("textId", textId));
		}
		if (!"".equals(defTextId)) {
			textVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("defTextId", defTextId));
		}
		textVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("regEx", ".*"));

		textVarGroup.addAttributeByIdWithValue("type", "textVariable");
		return textVarGroup;
	}

	public static SpiderDataGroup createSpiderDataGroupForRecordTypeWithId(String id) {
		SpiderDataGroup recordType = createGroupWithIdAndNameInDataAndDataDivider(id, "recordType", "test");
		recordType.addChild(SpiderDataAtomic.withNameInDataAndValue("abstract", "false"));
		recordType.addChild(SpiderDataAtomic.withNameInDataAndValue("userSuppliedId", "true"));
		return recordType;
	}

	private static SpiderDataGroup createGroupWithIdAndNameInDataAndDataDivider(String id, String nameInData, String dataDividerString) {
		SpiderDataGroup recordType = SpiderDataGroup.withNameInData(nameInData);
		SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
		recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", id));
		recordType.addChild(recordInfo);
		SpiderDataGroup dataDivider = SpiderDataGroup.withNameInData("dataDivider");
		dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", dataDividerString));
		recordInfo.addChild(dataDivider);
		return recordType;
	}

	public static void addAllValuesToSpiderDataGroup(SpiderDataGroup recordType, String id) {
		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType, "metadataId",
				"metadataGroup", id + "Group");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType, "newMetadataId",
				"metadataGroup", id + "NewGroup");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType,
				"presentationViewId", "presentationGroup", id + "ViewPGroup");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType,
				"presentationFormId", "presentationGroup", id + "FormPGroup");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType,
				"newPresentationFormId", "presentationGroup", id + "FormNewPGroup");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType,
				"menuPresentationViewId", "presentationGroup", id + "MenuPGroup");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType,
				"listPresentationViewId", "presentationGroup", id + "ListPGroup");

		addAtomicValueWithNameInDataAndValue(recordType, "selfPresentationViewId",
				id + "ViewSelfPGroup");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType, "textId",
				"text", id + "Text");

		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType, "defTextId",
				"text", id + "DefText");
		addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordType, "search",
				"search", id + "Search");
	}

	private static void addAtomicValueWithNameInDataAndValue(SpiderDataGroup spiderDataGroup,
			String nameInData, String value) {
		spiderDataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue(nameInData, value));
	}

	private static void addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(
			SpiderDataGroup spiderDataGroup, String nameInData, String type, String id) {
		SpiderDataGroup link = SpiderDataGroup.withNameInData(nameInData);
		link.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", type));
		link.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", id));
		spiderDataGroup.addChild(link);
	}

	public static SpiderDataGroup createCollectionItemGroupWithIdAndTextIdAndDefTextId(String id,
			String textId, String defTextId) {
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
			item.addChild(SpiderDataAtomic.withNameInDataAndValue("textId", textId));
		}
		if (!"".equals(defTextId)) {
			item.addChild(SpiderDataAtomic.withNameInDataAndValue("defTextId", defTextId));
		}
		return item;
	}


	public static SpiderDataGroup createItemCollectionWithId(String id) {
		SpiderDataGroup itemCollection = createGroupWithIdAndNameInDataAndDataDivider(id, "metadata", "test");
		itemCollection.addChild(SpiderDataAtomic.withNameInDataAndValue("nameInData", "someItemCollection"));

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

	private static SpiderDataGroup createItemRefWithLinkedIdAndRepeatId(String itemId, String repeatId) {
		SpiderDataGroup ref = SpiderDataGroup.withNameInData("ref");
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "metadataCollectionItem"));
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", itemId));
		ref.setRepeatId(repeatId);
		return ref;
	}
}
