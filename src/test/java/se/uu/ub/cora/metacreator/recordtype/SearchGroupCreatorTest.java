package se.uu.ub.cora.metacreator.recordtype;

import org.testng.annotations.Test;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

import static org.testng.Assert.*;

public class SearchGroupCreatorTest {

	@Test
	public void testCreateSearchGroup() {

		SearchGroupCreator searchGroupCreator = SearchGroupCreator
				.withIdIdAndDataDividerAndRecordType("myRecordTypeSearch", "cora", "myRecordType");

		SpiderDataGroup searchGroup = searchGroupCreator.createGroup("");
		SpiderDataGroup recordInfo = searchGroup.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), "myRecordTypeSearch");

		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordType"), "system");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "cora");

		assertFalse(searchGroup.containsChildWithNameInData("childReferences"));

		SpiderDataGroup recordTypeToSearchIn = searchGroup.extractGroup("recordTypeToSearchIn");
		assertEquals(recordTypeToSearchIn.extractAtomicValue("linkedRecordId"), "myRecordType");
		assertEquals(recordTypeToSearchIn.extractAtomicValue("linkedRecordType"), "recordType");
		assertNotNull(recordTypeToSearchIn.getRepeatId());

		SpiderDataGroup metadataId = searchGroup.extractGroup("metadataId");
		assertEquals(metadataId.extractAtomicValue("linkedRecordId"), "autocompleteSearchGroup");

		SpiderDataGroup presentationId = searchGroup.extractGroup("presentationId");
		assertEquals(presentationId.extractAtomicValue("linkedRecordId"),
				"autocompleteSearchPGroup");


	}
}
