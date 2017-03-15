package se.uu.ub.cora.metacreator.recordtype;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PresentationGroupCreatorTest {

	@Test
	public void testCreatePGroup() {

		PresentationGroupCreator pGroupCreator = PresentationGroupCreator
				.withIdDataDividerAndPresentationOf("myRecordTypeViewId", "cora",
						"myRecordTypeGroup");
		SpiderDataGroup pGroup = pGroupCreator.createGroup("recordInfoPGroup");

		SpiderDataGroup recordInfo = pGroup.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), "myRecordTypeViewId");

		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordType"), "system");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "cora");

		assertEquals(pGroup.getAttributes().get("type"), "pGroup");

		SpiderDataGroup presentationOf = pGroup.extractGroup("presentationOf");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), "myRecordTypeGroup");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordType"), "metadataGroup");

		assertCorrectChildReferences(pGroup);
	}

	private void assertCorrectChildReferences(SpiderDataGroup metadataGroup) {
		SpiderDataGroup childRefs = metadataGroup.extractGroup("childReferences");
		assertEquals(childRefs.getChildren().size(), 1);

		SpiderDataGroup childRef = (SpiderDataGroup) childRefs
				.getFirstChildWithNameInData("childReference");
		SpiderDataGroup refGroup = (SpiderDataGroup) childRef
				.getFirstChildWithNameInData("refGroup");

		SpiderDataGroup ref = (SpiderDataGroup) refGroup.getFirstChildWithNameInData("ref");
		assertEquals(ref.extractAtomicValue("linkedRecordId"), "recordInfoPGroup");
		assertEquals(ref.extractAtomicValue("linkedRecordType"), "presentationGroup");

		String defaultValue = childRef.extractAtomicValue("default");
		assertEquals(defaultValue, "ref");
	}

}
