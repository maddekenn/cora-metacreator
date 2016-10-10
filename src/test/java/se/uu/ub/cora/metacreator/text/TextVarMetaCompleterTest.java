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

package se.uu.ub.cora.metacreator.text;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class TextVarMetaCompleterTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String userId;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		userId = "testUser";
	}

	@Test
	public void testWithNoTexts() {
		TextVarMetaCompleter textVarMetaCompleter = TextVarMetaCompleter
				.forImplementingTextType("textSystemOne");

		String id = "noTextsTextVar";
		SpiderDataGroup textVarGroup = createTextVarGroupWithIdAndTextIdAndDefTextId(id, "", "");

		textVarMetaCompleter.useExtendedFunctionality(userId, textVarGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 2);
		assertCorrectTextCreatedWithUserIdAndTypeAndId(0, "noTextsTextVarText");
		assertCorrectTextCreatedWithUserIdAndTypeAndId(1, "noTextsTextVarDefText");
		String textId = textVarGroup.extractAtomicValue("textId");
		assertEquals(textId, "noTextsTextVarText");
		String defTextId = textVarGroup.extractAtomicValue("defTextId");
		assertEquals(defTextId, "noTextsTextVarDefText");
	}

	private SpiderDataGroup createTextVarGroupWithIdAndTextIdAndDefTextId(String id, String textId,
			String defTextId) {
		SpiderDataGroup textVarGroup = SpiderDataGroup.withNameInData("textVar");

		SpiderDataGroup recordInfoGroup = SpiderDataGroup.withNameInData("recordInfo");
		textVarGroup.addChild(recordInfoGroup);
		recordInfoGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("id", id));

		SpiderDataGroup dataDivider = SpiderDataGroup.withNameInData("dataDivider");
		recordInfoGroup.addChild(dataDivider);
		dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", "cora"));

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

	private void assertCorrectTextCreatedWithUserIdAndTypeAndId(int createdTextNo,
			String createdIdForText) {
		SpiderRecordCreatorSpy spiderRecordCreator1 = instanceFactory.spiderRecordCreators
				.get(createdTextNo);
		assertEquals(spiderRecordCreator1.userId, userId);
		assertEquals(spiderRecordCreator1.type, "textSystemOne");
		SpiderDataGroup createdTextRecord = spiderRecordCreator1.record;
		SpiderDataGroup recordInfo = createdTextRecord.extractGroup("recordInfo");
		String id = recordInfo.extractAtomicValue("id");
		assertEquals(id, createdIdForText);
	}

	@Test
	public void testWithTextIdNoTextsInStorage() {
		TextVarMetaCompleter textVarMetaCompleter = TextVarMetaCompleter
				.forImplementingTextType("textSystemOne");

		SpiderDataGroup textVarGroup = createTextVarGroupWithIdAndTextIdAndDefTextId(
				"textIdNoTextsInStorageTextVar", "textIdNoTextsInStorageTextVarText",
				"textIdNoTextsInStorageTextVarDefText");

		textVarMetaCompleter.useExtendedFunctionality(userId, textVarGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 2);
		assertCorrectTextCreatedWithUserIdAndTypeAndId(0, "textIdNoTextsInStorageTextVarText");
		assertCorrectTextCreatedWithUserIdAndTypeAndId(1, "textIdNoTextsInStorageTextVarDefText");
		String textId = textVarGroup.extractAtomicValue("textId");
		assertEquals(textId, "textIdNoTextsInStorageTextVarText");
		String defTextId = textVarGroup.extractAtomicValue("defTextId");
		assertEquals(defTextId, "textIdNoTextsInStorageTextVarDefText");
	}

	@Test
	public void testWithNonStandardTextIdNoTextsInStorage() {
		TextVarMetaCompleter textVarMetaCompleter = TextVarMetaCompleter
				.forImplementingTextType("textSystemOne");

		SpiderDataGroup textVarGroup = createTextVarGroupWithIdAndTextIdAndDefTextId(
				"nonStandardtTxtIdNoTextsInStorageTextVar", "nonStandardText",
				"nonStandardDefText");

		textVarMetaCompleter.useExtendedFunctionality(userId, textVarGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 2);
		assertCorrectTextCreatedWithUserIdAndTypeAndId(0, "nonStandardText");
		assertCorrectTextCreatedWithUserIdAndTypeAndId(1, "nonStandardDefText");

		String textId = textVarGroup.extractAtomicValue("textId");
		assertEquals(textId, "nonStandardText");
		String defTextId = textVarGroup.extractAtomicValue("defTextId");
		assertEquals(defTextId, "nonStandardDefText");
	}

	@Test
	public void testWithTextIdTextsInStorage() {
		TextVarMetaCompleter textVarMetaCompleter = TextVarMetaCompleter
				.forImplementingTextType("textSystemOne");

		SpiderDataGroup textVarGroup = createTextVarGroupWithIdAndTextIdAndDefTextId(
				"textIdTextsInStorageTextVar", "textIdTextsInStorageTextVarText",
				"textIdTextsInStorageTextVarDefText");

		textVarMetaCompleter.useExtendedFunctionality(userId, textVarGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
		String textId = textVarGroup.extractAtomicValue("textId");
		assertEquals(textId, "textIdTextsInStorageTextVarText");
		String defTextId = textVarGroup.extractAtomicValue("defTextId");
		assertEquals(defTextId, "textIdTextsInStorageTextVarDefText");
	}

	@Test
	public void testWithTextIdOnlyTextInStorage() {
		TextVarMetaCompleter textVarMetaCompleter = TextVarMetaCompleter
				.forImplementingTextType("textSystemOne");

		SpiderDataGroup textVarGroup = createTextVarGroupWithIdAndTextIdAndDefTextId(
				"textIdOnlyTextInStorageTextVar", "textIdOnlyTextInStorageTextVarText",
				"textIdOnlyTextInStorageTextVarDefText");

		textVarMetaCompleter.useExtendedFunctionality(userId, textVarGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 1);
		assertCorrectTextCreatedWithUserIdAndTypeAndId(0, "textIdOnlyTextInStorageTextVarDefText");
		String textId = textVarGroup.extractAtomicValue("textId");
		assertEquals(textId, "textIdOnlyTextInStorageTextVarText");
		String defTextId = textVarGroup.extractAtomicValue("defTextId");
		assertEquals(defTextId, "textIdOnlyTextInStorageTextVarDefText");
	}

}
