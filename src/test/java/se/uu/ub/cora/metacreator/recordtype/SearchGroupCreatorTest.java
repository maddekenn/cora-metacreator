package se.uu.ub.cora.metacreator.recordtype;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class SearchGroupCreatorTest {

	@Test
	public void testCreateSearchGroup() {

		SearchGroupCreator searchGroupCreator = SearchGroupCreator
				.withIdIdAndDataDividerAndRecordType("myRecordTypeSearch", "cora", "myRecordType");

		SpiderDataGroup searchGroup = searchGroupCreator.createGroup();
		SpiderDataGroup recordInfo = searchGroup.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), "myRecordTypeSearch");

		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordType"), "system");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "cora");

		assertEquals(searchGroup.getAttributes().get("type"), "search");

		SpiderDataGroup recordTypeToSearchIn = searchGroup.extractGroup("recordTypeToSearchIn");
		assertEquals(recordTypeToSearchIn.extractAtomicValue("linkedRecordId"), "myRecordType");
		assertEquals(recordTypeToSearchIn.extractAtomicValue("linkedRecordType"), "recordType");

		// kolla metadatagrupp - vad ska den vara?? nåt default?
		// Kolla presentationsgrupp - vad ska den vara??
		// assertCorrectChildReferences(pGroup);
	}

	// private void assertCorrectChildReferences(SpiderDataGroup metadataGroup)
	// {
	// SpiderDataGroup childRefs =
	// metadataGroup.extractGroup("childReferences");
	// assertEquals(childRefs.getChildren().size(), 1);
	//
	// SpiderDataGroup childRef =
	// (SpiderDataGroup)childRefs.getFirstChildWithNameInData("childReference");
	// SpiderDataGroup ref = (SpiderDataGroup)
	// childRef.getFirstChildWithNameInData("ref");
	// assertEquals(ref.extractAtomicValue("linkedRecordId"),
	// "recordInfoPGroup");
	//
	// String defaultValue = childRef.extractAtomicValue("default");
	// assertEquals(defaultValue, "ref");
	// }

}
