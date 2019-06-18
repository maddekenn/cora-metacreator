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

import se.uu.ub.cora.data.DataGroup;

public class TextConstructorTest {

	@Test
	public void testCreateTextsFromMetadataId() {
		String textId = "someTextVar";
		String dataDividerString = "cora";
		TextConstructor textConstructor = TextConstructor.withTextIdAndDataDivider(textId,
				dataDividerString);
		assertNotNull(textConstructor);
		DataGroup createdText = textConstructor.createText();
		assertEquals(createdText.getNameInData(), "text");

		assertEquals(createdText.getChildren().size(), 3);

		assertCorrectRecordInfo(createdText);

		DataGroup textPart1 = (DataGroup) createdText.getChildren().get(1);
		assertEquals(textPart1.getChildren().size(), 1);
		assertEquals(textPart1.getAttributes().size(), 2);
		assertEquals(textPart1.getAttributes().get("type"), "default");
		assertEquals(textPart1.getAttributes().get("lang"), "sv");
		assertEquals(textPart1.getFirstAtomicValueWithNameInData("text"), "Text för:someTextVar");

		DataGroup textPart2 = (DataGroup) createdText.getChildren().get(2);
		assertEquals(textPart2.getChildren().size(), 1);
		assertEquals(textPart2.getAttributes().size(), 2);
		assertEquals(textPart2.getAttributes().get("type"), "alternative");
		assertEquals(textPart2.getAttributes().get("lang"), "en");
		assertEquals(textPart2.getFirstAtomicValueWithNameInData("text"), "Text for:someTextVar");

	}

	private void assertCorrectRecordInfo(DataGroup createdText) {
		DataGroup recordInfo = createdText.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "someTextVar");

		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordType"), "system");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId"), "cora");
	}
}
