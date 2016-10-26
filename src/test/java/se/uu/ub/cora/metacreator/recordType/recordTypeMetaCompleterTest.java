package se.uu.ub.cora.metacreator.recordType;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

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

        //spiderDataGroup som innehåller id, abstract, userSuppliedId och inte så mycket mer
        SpiderDataGroup spiderDataGroup = SpiderDataGroup.withNameInData("recordType");
        //recordInfo
        SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
        recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", "myRecordType"));
        SpiderDataGroup dataDivider = SpiderDataGroup.withNameInData("dataDivider");
        metaCompleter.useExtendedFunctionality(userId, spiderDataGroup);

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
