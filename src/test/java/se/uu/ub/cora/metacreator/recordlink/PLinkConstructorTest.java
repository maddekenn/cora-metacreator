package se.uu.ub.cora.metacreator.recordlink;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PLinkConstructorTest {

	@Test
	public void testConstructPLink() {
		PLinkConstructor constructor = new PLinkConstructor();
		SpiderDataGroup pCollVar = constructor.constructPLinkWithIdDataDividerPresentationOfAndMode(
				"somePLink", "testSystem", "someLink", "input");

		assertCorrectRecordInfo(pCollVar);
		assertCorrectPresentationOf(pCollVar);

		assertEquals(pCollVar.getNameInData(), "presentation");
		assertEquals(pCollVar.extractAtomicValue("mode"), "input");
		assertEquals(pCollVar.getAttributes().get("type"), "pRecordLink");

	}

	private void assertCorrectRecordInfo(SpiderDataGroup pCollVar) {
		SpiderDataGroup recordInfo = pCollVar.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), "somePLink");
		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "testSystem");
	}

	private void assertCorrectPresentationOf(SpiderDataGroup pCollVar) {
		SpiderDataGroup presentationOf = pCollVar.extractGroup("presentationOf");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordType"), "metadataRecordLink");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), "someLink");
	}
}
