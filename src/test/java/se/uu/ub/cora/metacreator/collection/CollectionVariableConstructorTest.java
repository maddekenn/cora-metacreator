package se.uu.ub.cora.metacreator.collection;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;

public class CollectionVariableConstructorTest {

	@Test
	public void testConstructCollectionVar() {

		CollectionVariableConstructor constructor = new CollectionVariableConstructor();
		DataGroup collectionVar = constructor
				.constructCollectionVarWithIdNameInDataDataDividerAndRefCollection(
						"someCollectionVar", "someNameInData", "testSystem", "someCollection");

		assertEquals(collectionVar.getFirstAtomicValueWithNameInData("nameInData"),
				"someNameInData");

		assertCorrectRefCollection(collectionVar);

		assertCorrectRecordInfo(collectionVar);

	}

	private void assertCorrectRecordInfo(DataGroup record) {
		DataGroup recordInfo = record.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "someCollectionVar");

		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId"), "testSystem");
	}

	private void assertCorrectRefCollection(DataGroup record) {
		DataGroup refCollection = record.getFirstGroupWithNameInData("refCollection");
		assertEquals(refCollection.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"metadataItemCollection");
		assertEquals(refCollection.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someCollection");
	}

}
