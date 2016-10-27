package se.uu.ub.cora.metacreator.recordtype;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

import static org.testng.Assert.assertEquals;

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
    public void testDefaultValuesWhenAllValuesMissing(){

        SpiderDataGroup recordType = DataCreator.createSpiderDataGroupForRecordTypeWithId("myRecordType");
        metaCompleter.useExtendedFunctionality(userId, recordType);
        assertAllValuesWereAddedCorrectly(recordType);
    }

    private void assertAllValuesWereAddedCorrectly(SpiderDataGroup recordType) {
        assertEquals(recordType.extractAtomicValue("metadataId"), "myRecordTypeGroup");
        assertEquals(recordType.extractAtomicValue("newMetadataId"), "myRecordTypeNewGroup");
        assertEquals(recordType.extractAtomicValue("presentationViewId"), "myRecordTypeViewPGroup");
        assertEquals(recordType.extractAtomicValue("presentationFormId"), "myRecordTypeFormPGroup");
        assertEquals(recordType.extractAtomicValue("newPresentationFormId"), "myRecordTypeFormNewPGroup");
        assertEquals(recordType.extractAtomicValue("menuPresentationViewId"), "myRecordTypeMenuPGroup");
        assertEquals(recordType.extractAtomicValue("listPresentationViewId"), "myRecordTypeListPGroup");
        assertEquals(recordType.extractAtomicValue("searchMetadataId"), "myRecordTypeSearchGroup");
        assertEquals(recordType.extractAtomicValue("searchPresentationFormId"), "myRecordTypeFormSearchPGroup");
        assertEquals(recordType.extractAtomicValue("permissionKey"), "RECORDTYPE_MYRECORDTYPE");
        assertEquals(recordType.extractAtomicValue("selfPresentationViewId"), "myRecordTypeViewSelfPGroup");
        assertEquals(recordType.extractAtomicValue("textId"), "myRecordTypeText");
        assertEquals(recordType.extractAtomicValue("defTextId"), "myRecordTypeDefText");
    }

    @Test
    public void testDefaultValuesWhenAllValuesPresent(){
        SpiderDataGroup recordType = DataCreator.createSpiderDataGroupForRecordTypeWithId("mySpecial");
        DataCreator.addAllValuesToSpiderDataGroup(recordType, "mySpecial");

        metaCompleter.useExtendedFunctionality(userId, recordType);

        assertEquals(recordType.getChildren().size(), 16);

        assertEquals(recordType.extractAtomicValue("metadataId"), "mySpecialGroup");
        assertEquals(recordType.extractAtomicValue("newMetadataId"), "mySpecialNewGroup");
        assertEquals(recordType.extractAtomicValue("presentationViewId"), "mySpecialViewPGroup");
        assertEquals(recordType.extractAtomicValue("presentationFormId"), "mySpecialFormPGroup");
        assertEquals(recordType.extractAtomicValue("newPresentationFormId"), "mySpecialFormNewPGroup");
        assertEquals(recordType.extractAtomicValue("menuPresentationViewId"), "mySpecialMenuPGroup");
        assertEquals(recordType.extractAtomicValue("listPresentationViewId"), "mySpecialListPGroup");
        assertEquals(recordType.extractAtomicValue("searchMetadataId"), "mySpecialSearchGroup");
        assertEquals(recordType.extractAtomicValue("searchPresentationFormId"), "mySpecialFormSearchPGroup");
        assertEquals(recordType.extractAtomicValue("permissionKey"), "RECORDTYPE_MYSPECIAL");
        assertEquals(recordType.extractAtomicValue("selfPresentationViewId"), "mySpecialViewSelfPGroup");
        assertEquals(recordType.extractAtomicValue("textId"), "mySpecialText");
        assertEquals(recordType.extractAtomicValue("defTextId"), "mySpecialDefText");
    }


}
