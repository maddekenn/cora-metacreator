package se.uu.ub.cora.metacreator.collection;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;

public class PCollVarConstructorTest {

	@Test
	public void testPCollVarConstructor() {
		PCollVarConstructor constructor = new PCollVarConstructor();
		DataGroup pCollVar = constructor
				.constructPCollVarWithIdDataDividerPresentationOfAndMode("somePCollVar",
						"testSystem", "someCollectionVar", "input");

		assertCorrectRecordInfo(pCollVar);
		assertCorrectPresentationOf(pCollVar);

		assertEquals(pCollVar.getNameInData(), "presentation");
		assertEquals(pCollVar.getFirstAtomicValueWithNameInData("mode"), "input");
		DataGroup emptyValue = pCollVar.getFirstGroupWithNameInData("emptyTextId");
		assertEquals(emptyValue.getFirstAtomicValueWithNameInData("linkedRecordType"), "coraText");
		assertEquals(emptyValue.getFirstAtomicValueWithNameInData("linkedRecordId"), "initialEmptyValueText");
		assertEquals(pCollVar.getAttributes().get("type"), "pCollVar");

	}

	private void assertCorrectRecordInfo(DataGroup pCollVar) {
		DataGroup recordInfo = pCollVar.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "somePCollVar");
		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId"), "testSystem");
	}

	private void assertCorrectPresentationOf(DataGroup pCollVar) {
		DataGroup presentationOf = pCollVar.getFirstGroupWithNameInData("presentationOf");
		assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"metadataCollectionVariable");
		assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordId"), "someCollectionVar");
	}
}
