package se.uu.ub.cora.metacreator.group;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.PresentationChildReference;
import se.uu.ub.cora.metacreator.RecordIdentifier;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PVarChildRefConstructorTest {

	@Test
	public void testConstructorForInput() {
		SpiderDataGroup metadataChildRef = DataCreatorForPGroupConstructor
				.createMetadataChildRefWithIdAndRepeatId("identifierValueTextVar", "0");
		ChildRefConstructor constructor = PVarChildRefConstructor
				.usingMetadataChildReferenceAndMode(metadataChildRef, "input");

		PresentationChildReference childRef = constructor.getChildRef();
		assertCorrectRecordIdentifier(childRef, "identifierValuePVar");

		assertCorrectRef(childRef, "identifierValuePVar");

	}

	private void assertCorrectRef(PresentationChildReference childRef, String linkedRecordId) {
		SpiderDataGroup ref = childRef.ref;
		assertEquals(ref.extractAtomicValue("linkedRecordType"), "presentationVar");
		assertEquals(ref.extractAtomicValue("linkedRecordId"), linkedRecordId);
		assertEquals(ref.getAttributes().get("type"), "presentation");
	}

	private void assertCorrectRecordIdentifier(PresentationChildReference childRef,
			String identifierId) {
		RecordIdentifier recordIdentifier = childRef.recordIdentifier;
		assertEquals(recordIdentifier.type, "presentationVar");
		assertEquals(recordIdentifier.id, identifierId);
	}

	@Test
	public void testConstructorForOutput() {
		SpiderDataGroup metadataChildRef = DataCreatorForPGroupConstructor
				.createMetadataChildRefWithIdAndRepeatId("identifierValueTextVar", "0");
		ChildRefConstructor constructor = PVarChildRefConstructor
				.usingMetadataChildReferenceAndMode(metadataChildRef, "output");

		PresentationChildReference childRef = constructor.getChildRef();
		assertCorrectRecordIdentifier(childRef, "identifierValueOutputPVar");

		assertCorrectRef(childRef, "identifierValueOutputPVar");

	}
}
