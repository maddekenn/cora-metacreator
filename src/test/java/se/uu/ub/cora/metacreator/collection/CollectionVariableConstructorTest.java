package se.uu.ub.cora.metacreator.collection;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class CollectionVariableConstructorTest {

	@Test
	public void testConstructCollectionVar() {

		CollectionVariableConstructor constructor = new CollectionVariableConstructor();
		SpiderDataGroup collectionVar = constructor
				.constructCollectionVarWithIdNameInDataDataDividerAndRefCollection(
						"someCollectionVar", "someNameInData", "testSystem", "someCollection");

		assertEquals(collectionVar.extractAtomicValue("nameInData"), "someNameInData");

		assertCorrectRefCollection(collectionVar);

		assertCorrectRecordInfo(collectionVar);

	}

	private void assertCorrectRecordInfo(SpiderDataGroup record) {
		SpiderDataGroup recordInfo = record.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), "someCollectionVar");

		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "testSystem");
	}

	private void assertCorrectRefCollection(SpiderDataGroup record) {
		SpiderDataGroup refCollection = record.extractGroup("refCollection");
		assertEquals(refCollection.extractAtomicValue("linkedRecordType"),
				"metadataItemCollection");
		assertEquals(refCollection.extractAtomicValue("linkedRecordId"), "someCollection");
	}

}
