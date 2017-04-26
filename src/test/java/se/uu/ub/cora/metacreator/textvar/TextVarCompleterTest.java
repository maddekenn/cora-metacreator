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

package se.uu.ub.cora.metacreator.textvar;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class TextVarCompleterTest {
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
		TextVarCompleter textVarCompleter = TextVarCompleter
				.forTextLinkedRecordType("textSystemOne");

		String id = "noTextsTextVar";
		SpiderDataGroup textVarGroup = DataCreator.createTextVarGroupWithIdAndTextIdAndDefTextId(id,
				"", "");

		textVarCompleter.useExtendedFunctionality(userId, textVarGroup);

		SpiderDataGroup textIdGroup = textVarGroup.extractGroup("textId");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordId"), "noTextsTextVarText");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "textSystemOne");

		SpiderDataGroup defTextIdGroup = textVarGroup.extractGroup("defTextId");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordId"), "noTextsTextVarDefText");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "textSystemOne");
	}

	@Test
	public void testWithTextsInData() {
		TextVarCompleter textVarCompleter = TextVarCompleter
				.forTextLinkedRecordType("textSystemOne");

		String id = "noTextsTextVar";
		SpiderDataGroup textVarGroup = DataCreator.createTextVarGroupWithIdAndTextIdAndDefTextId(id,
				"someExistingText", "someExistingDefText");

		textVarCompleter.useExtendedFunctionality(userId, textVarGroup);

		SpiderDataGroup textIdGroup = textVarGroup.extractGroup("textId");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordId"), "someExistingText");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "someType");

		SpiderDataGroup defTextIdGroup = textVarGroup.extractGroup("defTextId");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordId"), "someExistingDefText");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "someType");

	}

}
