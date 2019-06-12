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

package se.uu.ub.cora.metacreator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class TextConstructorTest {

	@Test
	public void testCreateTextsFromMetadataId() {
		String textId = "someTextVar";
		String dataDividerString = "cora";
		TextConstructor textConstructor = TextConstructor.withTextIdAndDataDivider(textId, dataDividerString);
		assertNotNull(textConstructor);
		SpiderDataGroup createdText = textConstructor.createText();
		assertEquals(createdText.getNameInData(), "text");

		assertEquals(createdText.getChildren().size(), 3);

		assertCorrectRecordInfo(createdText);

		SpiderDataGroup textPart1 = (SpiderDataGroup) createdText.getChildren().get(1);
		assertEquals(textPart1.getChildren().size(), 1);
		assertEquals(textPart1.getAttributes().size(), 2);
		assertEquals(textPart1.getAttributes().get("type"), "default");
		assertEquals(textPart1.getAttributes().get("lang"), "sv");
		assertEquals(textPart1.extractAtomicValue("text"), "Text för:someTextVar");

		SpiderDataGroup textPart2 = (SpiderDataGroup) createdText.getChildren().get(2);
		assertEquals(textPart2.getChildren().size(), 1);
		assertEquals(textPart2.getAttributes().size(), 2);
		assertEquals(textPart2.getAttributes().get("type"), "alternative");
		assertEquals(textPart2.getAttributes().get("lang"), "en");
		assertEquals(textPart2.extractAtomicValue("text"), "Text for:someTextVar");

	}

	private void assertCorrectRecordInfo(SpiderDataGroup createdText) {
		SpiderDataGroup recordInfo = createdText.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), "someTextVar");

		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordType"), "system");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "cora");
	}
}
