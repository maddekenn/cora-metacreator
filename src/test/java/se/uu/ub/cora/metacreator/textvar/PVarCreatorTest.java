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
import static org.testng.Assert.assertNotNull;

import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

@Test
public class PVarCreatorTest {

	private PVarCreator pVarCreator;
	private String id;
	private String dataDividerString;

	@BeforeMethod
	public void setUp() {

		id = "someTextVar";
		dataDividerString = "cora";
		pVarCreator = PVarCreator.withTextVarIdAndDataDivider(id, dataDividerString);
	}

	@Test
	public void testCreateInputPVarFromMetadataIdAndDataDivider() {

		assertNotNull(pVarCreator);
		SpiderDataGroup createdPVar = pVarCreator.createInputPVar();

		assertEquals(createdPVar.getNameInData(), "presentation");

		assertCorrectAttribute(createdPVar);

		assertCorrectRecordInfo(createdPVar, "someTextVarPVar");

		assertEquals(createdPVar.getChildren().size(), 4);
		assertCorrectPresentationOf(id, createdPVar);

		assertEquals(createdPVar.extractAtomicValue("mode"), "input");
		assertEquals(createdPVar.extractAtomicValue("inputType"), "input");

	}

	private void assertCorrectAttribute(SpiderDataGroup createdPVar) {
		Map<String, String> attributes = createdPVar.getAttributes();
		assertEquals(attributes.size(), 1);
		assertEquals(attributes.get("type"), "pVar");
	}

	private void assertCorrectRecordInfo(SpiderDataGroup createdPVar, String id) {
		SpiderDataGroup recordInfo = createdPVar.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), id);

		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordType"), "system");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "cora");
	}

	private void assertCorrectPresentationOf(String id, SpiderDataGroup createdPVar) {
		SpiderDataGroup presentationOf = createdPVar.extractGroup("presentationOf");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordType"), "metadataTextVariable");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), id);
	}

	@Test
	public void testCreateOutputPVarFromMetadataIdAndDataDivider() {

		assertNotNull(pVarCreator);
		SpiderDataGroup createdPVar = pVarCreator.createOutputPVar();

		assertEquals(createdPVar.getNameInData(), "presentation");

		assertCorrectAttribute(createdPVar);

		assertCorrectRecordInfo(createdPVar, "someTextVarOutputPVar");

		assertEquals(createdPVar.getChildren().size(), 4);
		assertCorrectPresentationOf(id, createdPVar);

		assertEquals(createdPVar.extractAtomicValue("mode"), "output");
		assertEquals(createdPVar.extractAtomicValue("inputType"), "input");

	}
}
