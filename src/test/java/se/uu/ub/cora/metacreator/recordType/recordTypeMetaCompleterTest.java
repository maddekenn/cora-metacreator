package se.uu.ub.cora.metacreator.recordType;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

import static org.testng.Assert.assertEquals;

public class recordTypeMetaCompleterTest {
    private SpiderInstanceFactorySpy instanceFactory;
    private String userId;

    @BeforeMethod
    public void setUp() {
        instanceFactory = new SpiderInstanceFactorySpy();
        SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
        userId = "testUser";
    }

    @Test
    public void testDefaultValues(){
        RecordTypeMetaCompleter metaCompleter = new RecordTypeMetaCompleter();

        SpiderDataGroup recordType = SpiderDataGroup.withNameInData("recordType");
        //recordInfo
        SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
        recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", "myRecordType"));

        SpiderDataGroup dataDivider = SpiderDataGroup.withNameInData("dataDivider");
        dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
        dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", "test"));
        recordInfo.addChild(dataDivider);

        recordType.addChild(SpiderDataAtomic.withNameInDataAndValue("abstract", "false"));
        recordType.addChild(SpiderDataAtomic.withNameInDataAndValue("userSuppliedId", "true"));

        metaCompleter.useExtendedFunctionality(userId, recordType);

        assertEquals(instanceFactory.spiderRecordCreators.size(), 1);

    }

//    {
//        "children": [
//        {
//            "name": "abstract",
//                "value": "false"
//        },
//        {
//            "children": [
//            {
//                "name": "id",
//                    "value": "testRecordType"
//            },
//            {
//                "children": [
//                {
//                    "name": "linkedRecordType",
//                        "value": "system"
//                },
//                {
//                    "name": "linkedRecordId",
//                        "value": "test"
//                }
//                ],
//                "name": "dataDivider"
//            },
//
//            ],
//            "name": "recordInfo"
//        },
//        {
//            "name": "userSuppliedId",
//                "value": "true"
//        }
//        ],
//        "name": "recordType"
//    }
}
