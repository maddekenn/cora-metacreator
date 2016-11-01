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

        assertCorrectlyCreatedMetadataGroup(0, "myRecordTypeGroup");
        assertCorrectlyCreatedMetadataGroup(1, "myRecordTypeNewGroup");
        
        assertCorrectlyCreatedPresentationGroup(2, "myRecordTypeViewPGroup", "myRecordTypeGroup");
        assertCorrectlyCreatedPresentationGroup(3, "myRecordTypeFormPGroup", "myRecordTypeGroup");
        assertCorrectlyCreatedPresentationGroup(4, "myRecordTypeMenuPGroup", "myRecordTypeGroup");
        assertCorrectlyCreatedPresentationGroup(5, "myRecordTypeListPGroup", "myRecordTypeGroup");
        assertCorrectlyCreatedPresentationGroup(6, "myRecordTypeFormNewPGroup", "myRecordTypeNewGroup");
    }
    
    private void assertCorrectlyCreatedMetadataGroup(int createdPGroupNo, String id) {
        SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
                .get(createdPGroupNo);
        assertEquals(spiderRecordCreator.type, "metadataGroup");
        assertCorrectUserAndRecordInfo(id, spiderRecordCreator);
    }
    
    private void assertCorrectlyCreatedPresentationGroup(int createdPGroupNo, String id, String presentationOf) {
        SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
                .get(createdPGroupNo);
        assertEquals(spiderRecordCreator.type, "presentationGroup");
        SpiderDataGroup presentationOfGroup = spiderRecordCreator.record.extractGroup("presentationOf");
        assertEquals(presentationOfGroup.extractAtomicValue("linkedRecordId"), presentationOf);
        assertCorrectUserAndRecordInfo(id, spiderRecordCreator);
    }

	private void assertCorrectUserAndRecordInfo(String id, SpiderRecordCreatorSpy spiderRecordCreator) {
		assertEquals(spiderRecordCreator.userId, userId);
        SpiderDataGroup recordInfo = spiderRecordCreator.record.extractGroup("recordInfo");
        assertEquals(recordInfo.extractAtomicValue("id"), id);
        
        SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
        assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "test");
	}

    @Test
    public void testPGroupCreatorAllPresentationsExists(){
        RecordTypeCreator pGroupCreator = new RecordTypeCreator();

        SpiderDataGroup recordType = DataCreator.createSpiderDataGroupForRecordTypeWithId("myRecordType2");
        DataCreator.addAllValuesToSpiderDataGroup(recordType, "myRecordType2");

        pGroupCreator.useExtendedFunctionality(userId, recordType);
        assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
    }
    
    @Test
    public void testRecordTypeCreatorNoTextsExists(){
        RecordTypeCreator recordTypeCreator = new RecordTypeCreator();

        SpiderDataGroup recordType = DataCreator.createSpiderDataGroupForRecordTypeWithId("myRecordType");
        DataCreator.addAllValuesToSpiderDataGroup(recordType, "myRecordType");

        recordTypeCreator.useExtendedFunctionality(userId, recordType);
        assertEquals(instanceFactory.spiderRecordCreators.size(), 8);
        SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
                .get(0);
        assertEquals(spiderRecordCreator.type, "metadataTextVariable");
        

    }   
}
