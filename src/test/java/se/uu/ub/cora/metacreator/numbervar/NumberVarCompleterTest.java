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

package se.uu.ub.cora.metacreator.numbervar;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class NumberVarCompleterTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String userId;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		userId = "testUser";
	}

	@Test
	public void testGetimplementingTextType() {
		NumberVarCompleter numberVarCompleter = NumberVarCompleter.forImplementingTextType("coraText");
		assertEquals(numberVarCompleter.getImplementingTextType(), "coraText");

	}

	@Test
	public void testWithNoTexts() {
		NumberVarCompleter numberVarCompleter = NumberVarCompleter.forImplementingTextType("coraText");

		SpiderDataGroup numberVarGroup = DataCreator.createNumberVarUsingIdNameInDataAndDataDivider(
				"noTextsNumberVar", "metadata", "testSystem");

		numberVarCompleter.useExtendedFunctionality(userId, numberVarGroup);

		SpiderDataGroup textIdGroup = numberVarGroup.extractGroup("textId");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordId"), "noTextsNumberVarText");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "coraText");

		SpiderDataGroup defTextIdGroup = numberVarGroup.extractGroup("defTextId");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordId"),
				"noTextsNumberVarDefText");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "coraText");
	}

	@Test
	public void testWithTextsInData() {
		NumberVarCompleter numberVarCompleter = NumberVarCompleter.forImplementingTextType("coraText");

		SpiderDataGroup numberVarGroup = DataCreator.createNumberVarUsingIdNameInDataAndDataDivider(
				"noTextsNumberVar", "metadata", "testSystem");
		addTextsToGroup(numberVarGroup);

		numberVarCompleter.useExtendedFunctionality(userId, numberVarGroup);

		SpiderDataGroup textIdGroup = numberVarGroup.extractGroup("textId");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordId"), "someExistingText");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "someOtherTextType");

		SpiderDataGroup defTextIdGroup = numberVarGroup.extractGroup("defTextId");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordId"), "someExistingDefText");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "someOtherTextType");

	}

	private void addTextsToGroup(SpiderDataGroup numberVarGroup) {
		SpiderDataGroup textIdGroup = createTextGroupUsingNameInDataAndTextId("textId",
				"someExistingText");
		numberVarGroup.addChild(textIdGroup);
		SpiderDataGroup defTextIdGroup = createTextGroupUsingNameInDataAndTextId("defTextId",
				"someExistingDefText");
		numberVarGroup.addChild(defTextIdGroup);
	}

	private SpiderDataGroup createTextGroupUsingNameInDataAndTextId(String nameInData,
			String textId) {
		SpiderDataGroup textIdGroup = SpiderDataGroup.withNameInData(nameInData);
		textIdGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "someOtherTextType"));
		textIdGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", textId));
		return textIdGroup;
	}

}
