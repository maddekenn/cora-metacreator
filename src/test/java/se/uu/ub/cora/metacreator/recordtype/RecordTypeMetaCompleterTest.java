package se.uu.ub.cora.metacreator.recordtype;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class RecordTypeMetaCompleterTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String userId;
	private RecordTypeMetaCompleter metaCompleter;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		metaCompleter = new RecordTypeMetaCompleter();
		userId = "testUser";
	}

	@Test
	public void testDefaultValuesWhenAllValuesMissing() {

		SpiderDataGroup recordType = DataCreator
				.createSpiderDataGroupForRecordTypeWithId("myRecordType");
		metaCompleter.useExtendedFunctionality(userId, recordType);
		assertAllValuesWereAddedCorrectly(recordType);
	}

	private void assertAllValuesWereAddedCorrectly(SpiderDataGroup recordType) {
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,"metadataId"), "myRecordTypeGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,"newMetadataId"), "myRecordTypeNewGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,"presentationViewId"), "myRecordTypeViewPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,"presentationFormId"), "myRecordTypeFormPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,"newPresentationFormId"),
				"myRecordTypeFormNewPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,"menuPresentationViewId"),
				"myRecordTypeMenuPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,"listPresentationViewId"),
				"myRecordTypeListPGroup");
		assertEquals(recordType.extractAtomicValue("selfPresentationViewId"),
				"myRecordTypeViewSelfPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,"textId"), "myRecordTypeText");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,"defTextId"), "myRecordTypeDefText");

		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,"search"), "myRecordTypeSearch");
		 assertEquals(recordType.extractAtomicValue("searchPresentationFormId"),
		 "myRecordTypeFormSearchPGroup");
	}

	@Test
	public void testDefaultValuesWhenAllValuesPresent() {
		SpiderDataGroup recordType = DataCreator
				.createSpiderDataGroupForRecordTypeWithId("mySpecial");
		DataCreator.addAllValuesToSpiderDataGroup(recordType, "mySpecial");

		metaCompleter.useExtendedFunctionality(userId, recordType);

		assertEquals(recordType.getChildren().size(), 14);

		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,"metadataId"), "mySpecialGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "newMetadataId"), "mySpecialNewGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "presentationViewId"), "mySpecialViewPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "presentationFormId"), "mySpecialFormPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "newPresentationFormId"),
				"mySpecialFormNewPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "menuPresentationViewId"),
				"mySpecialMenuPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "listPresentationViewId"),
				"mySpecialListPGroup");
		assertEquals(recordType.extractAtomicValue("selfPresentationViewId"),
				"mySpecialViewSelfPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "textId"), "mySpecialText");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "defTextId"), "mySpecialDefText");
		SpiderDataGroup search = recordType.extractGroup("search");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "search"), "mySpecialSearch");
	}

	private String extractLinkedRecordIdFromLinkInDataGroupByNameInData(SpiderDataGroup dataGroup, String nameInData){
		SpiderDataGroup link = (SpiderDataGroup) dataGroup.getFirstChildWithNameInData(nameInData);
		SpiderDataAtomic linkedRecordId = (SpiderDataAtomic)link.getFirstChildWithNameInData("linkedRecordId");
		return linkedRecordId.getValue();
	}

}
