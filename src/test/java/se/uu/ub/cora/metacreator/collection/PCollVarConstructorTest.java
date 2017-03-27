package se.uu.ub.cora.metacreator.collection;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PCollVarConstructorTest {

	@Test
	public void testInit() {
		PCollVarConstructor constructor = new PCollVarConstructor();
		SpiderDataGroup pCollVar = constructor
				.constructPCollVarWithIdDataDividerPresentationOfAndMode("somePCollVar",
						"testSystem", "someCollectionVar", "input");

		assertEquals(pCollVar.getNameInData(), "presentation");

		assertCorrectRecordInfo(pCollVar);

		assertCorrectPresentationOf(pCollVar);

		assertEquals(pCollVar.extractAtomicValue("mode"), "input");

	}

	private void assertCorrectRecordInfo(SpiderDataGroup pCollVar) {
		SpiderDataGroup recordInfo = pCollVar.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), "somePCollVar");
		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "testSystem");
	}

	private void assertCorrectPresentationOf(SpiderDataGroup pCollVar) {
		SpiderDataGroup presentationOf = pCollVar.extractGroup("presentationOf");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordType"),
				"metadataCollectionVariable");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), "someCollectionVar");
	}
}
