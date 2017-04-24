package se.uu.ub.cora.metacreator.collection;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class CollectionVariableConstructorTest {

	@Test
	public void testConstructCollectionVar() {

		CollectionVariableConstructor constructor = CollectionVariableConstructor.forImplementingTextType("someTextType");
		SpiderDataGroup collectionVar = constructor
				.constructCollectionVarWithIdNameInDataDataDividerAndRefCollection(
						"someCollectionVar", "someNameInData", "testSystem", "someCollection");

		assertEquals(collectionVar.extractAtomicValue("nameInData"), "someNameInData");

		assertCorrectTexts(collectionVar);

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

	private void assertCorrectTexts(SpiderDataGroup record) {
		SpiderDataGroup textGroup = record.extractGroup("textId");
		assertEquals(textGroup.extractAtomicValue("linkedRecordId"), "someCollectionVarText");
		assertEquals(textGroup.extractAtomicValue("linkedRecordType"), "someTextType");

		SpiderDataGroup defTextGroup = record.extractGroup("defTextId");
		assertEquals(defTextGroup.extractAtomicValue("linkedRecordId"), "someCollectionVarDefText");
		assertEquals(defTextGroup.extractAtomicValue("linkedRecordType"), "someTextType");
	}
}
