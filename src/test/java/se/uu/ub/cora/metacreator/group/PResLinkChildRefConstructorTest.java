/*
 * Copyright 2018 Uppsala University Library
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
package se.uu.ub.cora.metacreator.group;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.PresentationChildReference;
import se.uu.ub.cora.metacreator.RecordIdentifier;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PResLinkChildRefConstructorTest {

	@Test
	public void testConstructorForInput() {
		SpiderDataGroup metadataChildRef = DataCreatorForPresentationsConstructor
				.createMetadataChildRefWithIdAndRepeatId("identifierResLink", "0");
		ChildRefConstructor constructor = PResLinkChildRefConstructor
				.usingMetadataChildReferenceAndMode(metadataChildRef, "input");

		PresentationChildReference childRef = constructor.getChildRef();
		assertCorrectRecordIdentifier(childRef, "identifierPResLink");

		assertCorrectRef(childRef, "identifierPResLink");

	}

	private void assertCorrectRef(PresentationChildReference childRef, String linkedRecordId) {
		SpiderDataGroup ref = childRef.ref;
		assertEquals(ref.extractAtomicValue("linkedRecordType"), "presentationResourceLink");
		assertEquals(ref.extractAtomicValue("linkedRecordId"), linkedRecordId);
		assertEquals(ref.getAttributes().get("type"), "presentation");
	}

	private void assertCorrectRecordIdentifier(PresentationChildReference childRef,
			String identifierId) {
		RecordIdentifier recordIdentifier = childRef.recordIdentifier;
		assertEquals(recordIdentifier.type, "presentationResourceLink");
		assertEquals(recordIdentifier.id, identifierId);
	}

	@Test
	public void testConstructorForOutput() {
		SpiderDataGroup metadataChildRef = DataCreatorForPresentationsConstructor
				.createMetadataChildRefWithIdAndRepeatId("identifierResLink", "0");
		ChildRefConstructor constructor = PResLinkChildRefConstructor
				.usingMetadataChildReferenceAndMode(metadataChildRef, "output");

		PresentationChildReference childRef = constructor.getChildRef();
		assertCorrectRecordIdentifier(childRef, "identifierOutputPResLink");

		assertCorrectRef(childRef, "identifierOutputPResLink");

	}
}
