package se.uu.ub.cora.metacreator.recordtype;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class SearchGroupCreatorTest {

	@Test
	public void testCreateSearchGroup() {

		SearchGroupCreator searchGroupCreator = SearchGroupCreator
				.withIdIdAndDataDividerAndRecordType("myRecordTypeSearch", "cora", "myRecordType");

		SpiderDataGroup searchGroup = searchGroupCreator.createGroup("");
		SpiderDataGroup recordInfo = searchGroup.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), "myRecordTypeSearch");

		assertCorrectDataDivider(recordInfo);

		assertFalse(searchGroup.containsChildWithNameInData("childReferences"));

		assertCorrectRecordTypeToSearchIn(searchGroup);

		assertCorrectMetadataId(searchGroup);

		assertCorrectPresentationId(searchGroup);

		assertEquals(searchGroup.extractAtomicValue("searchGroup"), "autocomplete");

		assertCorrectTexts(searchGroup);

	}

	private void assertCorrectDataDivider(SpiderDataGroup recordInfo) {
		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordType"), "system");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "cora");
	}

	private void assertCorrectRecordTypeToSearchIn(SpiderDataGroup searchGroup) {
		SpiderDataGroup recordTypeToSearchIn = searchGroup.extractGroup("recordTypeToSearchIn");
		assertEquals(recordTypeToSearchIn.extractAtomicValue("linkedRecordId"), "myRecordType");
		assertEquals(recordTypeToSearchIn.extractAtomicValue("linkedRecordType"), "recordType");
		assertNotNull(recordTypeToSearchIn.getRepeatId());
	}


	private void assertCorrectMetadataId(SpiderDataGroup searchGroup) {
		SpiderDataGroup metadataId = searchGroup.extractGroup("metadataId");
		assertEquals(metadataId.extractAtomicValue("linkedRecordId"), "autocompleteSearchGroup");
	}

	private void assertCorrectPresentationId(SpiderDataGroup searchGroup) {
		SpiderDataGroup presentationId = searchGroup.extractGroup("presentationId");
		assertEquals(presentationId.extractAtomicValue("linkedRecordId"),
				"autocompleteSearchPGroup");
	}

	private void assertCorrectTexts(SpiderDataGroup searchGroup) {
		SpiderDataGroup textIdGroup = searchGroup.extractGroup("textId");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordId"), "myRecordTypeSearchText");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "text");

		SpiderDataGroup defTextIdGroup = searchGroup.extractGroup("defTextId");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordId"),
				"myRecordTypeSearchDefText");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "text");
	}
}
