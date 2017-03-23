package se.uu.ub.cora.metacreator;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

import static org.testng.Assert.assertEquals;

public class RecordCreatorHelperTest {

    private SpiderInstanceFactorySpy instanceFactory;
    private String authToken;

    @BeforeMethod
    public void setUp() {
        instanceFactory = new SpiderInstanceFactorySpy();
        SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
        authToken = "testUser";
    }

    @Test
    public void testInit(){
        RecordCreatorHelper creatorHelper = RecordCreatorHelper.withAuthToken("testUser");
        creatorHelper.createTextInStorageWithTextIdDataDividerAndTextType("someText", "testSystem", "textSystemOne");

        assertEquals(instanceFactory.spiderRecordCreators.size(), 1);
        SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
                .get(0);
        SpiderDataGroup recordInfo = spiderRecordCreator.record.extractGroup("recordInfo");
        assertEquals(recordInfo.extractAtomicValue("id"), "someText");
        assertEquals(spiderRecordCreator.type, "textSystemOne");

        SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
        assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "testSystem");
    }
}
