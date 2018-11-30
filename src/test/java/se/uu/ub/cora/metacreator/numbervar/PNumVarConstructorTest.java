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
import static org.testng.Assert.assertNotNull;

import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

@Test
public class PNumVarConstructorTest {

	private PNumVarConstructor pNumVarConstructor;
	private String id;
	private String dataDividerString;

	@BeforeMethod
	public void setUp() {

		id = "someNumberVar";
		dataDividerString = "cora";
		pNumVarConstructor = new PNumVarConstructor(id, dataDividerString);
		// PVarConstructor.withTextVarIdAndDataDivider(id, dataDividerString);
	}

	@Test
	public void testCreateInputPNumVarFromMetadataIdAndDataDivider() {

		assertNotNull(pNumVarConstructor);
		SpiderDataGroup createdPNumVar = pNumVarConstructor.createInputPNumVar();

		assertEquals(createdPNumVar.getNameInData(), "presentation");

		assertCorrectAttribute(createdPNumVar);

		assertCorrectRecordInfo(createdPNumVar, "somePNumVar");

		assertEquals(createdPNumVar.getChildren().size(), 3);
		assertCorrectPresentationOf(id, createdPNumVar);
		assertEquals(createdPNumVar.extractAtomicValue("mode"), "input");

	}

	private void assertCorrectAttribute(SpiderDataGroup createdPVar) {
		Map<String, String> attributes = createdPVar.getAttributes();
		assertEquals(attributes.size(), 1);
		assertEquals(attributes.get("type"), "pNumVar");
	}

	private void assertCorrectRecordInfo(SpiderDataGroup createdPNumVar, String id) {
		SpiderDataGroup recordInfo = createdPNumVar.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), id);

		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordType"), "system");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "cora");
	}

	private void assertCorrectPresentationOf(String id, SpiderDataGroup createdPVar) {
		SpiderDataGroup presentationOf = createdPVar.extractGroup("presentationOf");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordType"),
				"metadataNumberVariable");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), id);
	}

	@Test
	public void testCreateOutputPVarFromMetadataIdAndDataDivider() {

		assertNotNull(pNumVarConstructor);
		SpiderDataGroup createdPNumVar = pNumVarConstructor.createOutputPVar();

		assertEquals(createdPNumVar.getNameInData(), "presentation");

		assertCorrectAttribute(createdPNumVar);

		assertCorrectRecordInfo(createdPNumVar, "someOutputPNumVar");

		assertEquals(createdPNumVar.getChildren().size(), 3);
		assertCorrectPresentationOf(id, createdPNumVar);

		assertEquals(createdPNumVar.extractAtomicValue("mode"), "output");

	}
}
