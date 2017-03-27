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

		SpiderDataGroup recordInfo = pCollVar.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), "somePCollVar");
		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "testSystem");

	}
}
