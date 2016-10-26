package se.uu.ub.cora.metacreator.recordtype;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.recordtype.RecordTypeMetaCompleter;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

import static org.testng.Assert.assertEquals;

public class recordTypeMetaCompleterTest {
    private String userId;

    @BeforeMethod
    public void setUp() {
        userId = "testUser";
    }

    @Test
    public void testDefaultValues(){
        RecordTypeMetaCompleter metaCompleter = new RecordTypeMetaCompleter();

        SpiderDataGroup recordType = SpiderDataGroup.withNameInData("recordType");
        //recordInfo
        SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
        recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", "myRecordType"));
        recordType.addChild(recordInfo);
        SpiderDataGroup dataDivider = SpiderDataGroup.withNameInData("dataDivider");
        dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
        dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", "test"));
        recordInfo.addChild(dataDivider);

        recordType.addChild(SpiderDataAtomic.withNameInDataAndValue("abstract", "false"));
        recordType.addChild(SpiderDataAtomic.withNameInDataAndValue("userSuppliedId", "true"));

        metaCompleter.useExtendedFunctionality(userId, recordType);
        
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
}
