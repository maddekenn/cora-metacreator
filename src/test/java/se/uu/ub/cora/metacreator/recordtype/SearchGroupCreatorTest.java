package se.uu.ub.cora.metacreator.recordtype;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;

public class SearchGroupCreatorTest {

	@Test
	public void testCreateSearchGroup() {

		SearchGroupCreator searchGroupCreator = SearchGroupCreator
				.withIdIdAndDataDividerAndRecordType("myRecordTypeSearch", "cora", "myRecordType");

		DataGroup searchGroup = searchGroupCreator.createGroup("");
		DataGroup recordInfo = searchGroup.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "myRecordTypeSearch");

		assertCorrectDataDivider(recordInfo);

		assertFalse(searchGroup.containsChildWithNameInData("childReferences"));

		assertCorrectRecordTypeToSearchIn(searchGroup);

		assertCorrectMetadataId(searchGroup);

		assertCorrectPresentationId(searchGroup);

		assertEquals(searchGroup.getFirstAtomicValueWithNameInData("searchGroup"), "autocomplete");

		assertCorrectTexts(searchGroup);

	}

	private void assertCorrectDataDivider(DataGroup recordInfo) {
		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordType"), "system");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId"), "cora");
	}

	private void assertCorrectRecordTypeToSearchIn(DataGroup searchGroup) {
		DataGroup recordTypeToSearchIn = searchGroup
				.getFirstGroupWithNameInData("recordTypeToSearchIn");
		assertEquals(recordTypeToSearchIn.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"myRecordType");
		assertEquals(recordTypeToSearchIn.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"recordType");
		assertNotNull(recordTypeToSearchIn.getRepeatId());
	}

	private void assertCorrectMetadataId(DataGroup searchGroup) {
		DataGroup metadataId = searchGroup.getFirstGroupWithNameInData("metadataId");
		assertEquals(metadataId.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"autocompleteSearchGroup");
	}

	private void assertCorrectPresentationId(DataGroup searchGroup) {
		DataGroup presentationId = searchGroup.getFirstGroupWithNameInData("presentationId");
		assertEquals(presentationId.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"autocompleteSearchPGroup");
	}

	private void assertCorrectTexts(DataGroup searchGroup) {
		DataGroup textIdGroup = searchGroup.getFirstGroupWithNameInData("textId");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"myRecordTypeSearchText");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"), "coraText");

		DataGroup defTextIdGroup = searchGroup.getFirstGroupWithNameInData("defTextId");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"myRecordTypeSearchDefText");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"coraText");
	}
}
