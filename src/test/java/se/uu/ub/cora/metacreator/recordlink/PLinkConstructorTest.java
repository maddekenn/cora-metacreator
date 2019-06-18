package se.uu.ub.cora.metacreator.recordlink;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;

public class PLinkConstructorTest {

	@Test
	public void testConstructPLink() {
		PLinkConstructor constructor = new PLinkConstructor();
		DataGroup pCollVar = constructor.constructPLinkWithIdDataDividerPresentationOfAndMode(
				"somePLink", "testSystem", "someLink", "input");

		assertCorrectRecordInfo(pCollVar);
		assertCorrectPresentationOf(pCollVar);

		assertEquals(pCollVar.getNameInData(), "presentation");
		assertEquals(pCollVar.getFirstAtomicValueWithNameInData("mode"), "input");
		assertEquals(pCollVar.getAttributes().get("type"), "pRecordLink");

	}

	private void assertCorrectRecordInfo(DataGroup pCollVar) {
		DataGroup recordInfo = pCollVar.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "somePLink");
		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId"), "testSystem");
	}

	private void assertCorrectPresentationOf(DataGroup pCollVar) {
		DataGroup presentationOf = pCollVar.getFirstGroupWithNameInData("presentationOf");
		assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"metadataRecordLink");
		assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someLink");
	}
}
