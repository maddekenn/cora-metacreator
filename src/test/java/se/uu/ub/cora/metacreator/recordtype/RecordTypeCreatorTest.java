package se.uu.ub.cora.metacreator.recordtype;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

import static org.testng.Assert.assertEquals;

public class RecordTypeCreatorTest {
    private SpiderInstanceFactorySpy instanceFactory;
    private String userId;


    @BeforeMethod
    public void setUp() {
        instanceFactory = new SpiderInstanceFactorySpy();
        SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
        userId = "testUser";
    }

    @Test
    public void testRecordTypeCreatorNoPresentationsExists(){
        RecordTypeCreator recordTypeCreator = new RecordTypeCreator();

        SpiderDataGroup recordType = DataCreator.createSpiderDataGroupForRecordTypeWithId("myRecordType");
        DataCreator.addAllValuesToSpiderDataGroup(recordType, "myRecordType");

        recordTypeCreator.useExtendedFunctionality(userId, recordType);
        assertEquals(instanceFactory.spiderRecordCreators.size(), 7);

        SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
                .get(0);
        assertEquals(spiderRecordCreator.type, "metadataGroup");
        assertEquals(spiderRecordCreator.userId, userId);
        
        assertCorrectlyCreatedPresentationGroup(2, "myRecordTypeViewPGroup");
        assertCorrectlyCreatedPresentationGroup(3, "myRecordTypeFormPGroup");
        assertCorrectlyCreatedPresentationGroup(4, "myRecordTypeFormNewPGroup");
        assertCorrectlyCreatedPresentationGroup(5, "myRecordTypeMenuPGroup");
        assertCorrectlyCreatedPresentationGroup(6, "myRecordTypeListPGroup");
    }

    private void assertCorrectlyCreatedPresentationGroup(int createdPGroupNo, String id) {
        SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
                .get(createdPGroupNo);
        assertEquals(spiderRecordCreator.userId, userId);
        assertEquals(spiderRecordCreator.type, "presentationGroup");
        SpiderDataGroup recordInfo = spiderRecordCreator.record.extractGroup("recordInfo");
        assertEquals(recordInfo.extractAtomicValue("id"), id);
    }


    @Test
    public void testPGroupCreatorAllPresentationsExists(){
        RecordTypeCreator pGroupCreator = new RecordTypeCreator();

        SpiderDataGroup recordType = DataCreator.createSpiderDataGroupForRecordTypeWithId("myRecordType2");
        DataCreator.addAllValuesToSpiderDataGroup(recordType, "myRecordType2");

        pGroupCreator.useExtendedFunctionality(userId, recordType);
        assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
    }
    
}
