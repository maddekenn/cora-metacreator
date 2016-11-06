package se.uu.ub.cora.metacreator.recordtype;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

import static org.testng.Assert.assertEquals;

import static org.testng.Assert.assertFalse;

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
        RecordTypeCreator recordTypeCreator = RecordTypeCreator.forImplementingTextType("textSystemOne");

        SpiderDataGroup recordType = DataCreator.createSpiderDataGroupForRecordTypeWithId("myRecordType");
        DataCreator.addAllValuesToSpiderDataGroup(recordType, "myRecordType");

        recordTypeCreator.useExtendedFunctionality(userId, recordType);
        assertEquals(instanceFactory.spiderRecordCreators.size(), 9);

        assertCorrectlyCreatedMetadataGroup(2, "myRecordTypeGroup", "recordInfoGroup");
        assertCorrectlyCreatedMetadataGroup(3, "myRecordTypeNewGroup", "recordInfoNewGroup");
        
        assertCorrectlyCreatedPresentationGroup(4, "myRecordTypeViewPGroup", "myRecordTypeGroup", "recordInfoPGroup");
        assertCorrectlyCreatedPresentationGroup(5, "myRecordTypeFormPGroup", "myRecordTypeGroup", "recordInfoPGroup");
        assertCorrectlyCreatedPresentationGroup(6, "myRecordTypeMenuPGroup", "myRecordTypeGroup", "recordInfoPGroup");
        assertCorrectlyCreatedPresentationGroup(7, "myRecordTypeListPGroup", "myRecordTypeGroup", "recordInfoPGroup");
        assertCorrectlyCreatedPresentationGroup(8, "myRecordTypeFormNewPGroup", "myRecordTypeNewGroup", "recordInfoNewPGroup");
    }
    
    private void assertCorrectlyCreatedMetadataGroup(int createdPGroupNo, String id, String childRefId) {
        SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
                .get(createdPGroupNo);
        assertEquals(spiderRecordCreator.type, "metadataGroup");
        assertCorrectUserAndRecordInfo(id, spiderRecordCreator);
        assertCorrectlyCreatedMetadataChildReference(childRefId, spiderRecordCreator);
}

	private void assertCorrectlyCreatedMetadataChildReference(String childRefId, SpiderRecordCreatorSpy spiderRecordCreator) {
		SpiderDataGroup childRef = getChildRef(spiderRecordCreator);
        assertEquals(childRef.extractAtomicValue("ref"), childRefId);
        assertEquals(childRef.extractAtomicValue("repeatMin"), "1");
        assertEquals(childRef.extractAtomicValue("repeatMax"), "1");
        
	}
	
	private void assertCorrectlyCreatedPresentationChildReference(String childRefId, SpiderRecordCreatorSpy spiderRecordCreator) {
		SpiderDataGroup childRef = getChildRef(spiderRecordCreator);
        assertEquals(childRef.extractAtomicValue("ref"), childRefId);
        assertEquals(childRef.extractAtomicValue("default"), "ref");
        assertFalse(childRef.containsChildWithNameInData("repeatMax"));
        
	}

	private SpiderDataGroup getChildRef(SpiderRecordCreatorSpy spiderRecordCreator) {
		SpiderDataGroup childReferences = spiderRecordCreator.record.extractGroup("childReferences");
        SpiderDataGroup childRef = (SpiderDataGroup) childReferences.getFirstChildWithNameInData("childReference");
		return childRef;
	}
    
    private void assertCorrectlyCreatedPresentationGroup(int createdPGroupNo, String id, String presentationOf, String childRefId) {
        SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
                .get(createdPGroupNo);
        assertEquals(spiderRecordCreator.type, "presentationGroup");
        SpiderDataGroup presentationOfGroup = spiderRecordCreator.record.extractGroup("presentationOf");
        assertEquals(presentationOfGroup.extractAtomicValue("linkedRecordId"), presentationOf);
        assertCorrectUserAndRecordInfo(id, spiderRecordCreator);
        assertCorrectlyCreatedPresentationChildReference(childRefId, spiderRecordCreator);
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
        RecordTypeCreator pGroupCreator = RecordTypeCreator.forImplementingTextType("textSystemOne");

        SpiderDataGroup recordType = DataCreator.createSpiderDataGroupForRecordTypeWithId("myRecordType2");
        DataCreator.addAllValuesToSpiderDataGroup(recordType, "myRecordType2");

        pGroupCreator.useExtendedFunctionality(userId, recordType);
        assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
    }

    @Test
    public void testRecordTypeCreatorNoTextsExists(){
        RecordTypeCreator recordTypeCreator = RecordTypeCreator.forImplementingTextType("textSystemOne");

        SpiderDataGroup recordType = DataCreator.createSpiderDataGroupForRecordTypeWithId("myRecordType");
        DataCreator.addAllValuesToSpiderDataGroup(recordType, "myRecordType");

        recordTypeCreator.useExtendedFunctionality(userId, recordType);
        assertEquals(instanceFactory.spiderRecordCreators.size(), 9);
        SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
                .get(0);
        assertEquals(spiderRecordCreator.type, "textSystemOne");
        SpiderRecordCreatorSpy spiderRecordCreator2 = instanceFactory.spiderRecordCreators
                .get(1);
        assertEquals(spiderRecordCreator2.type, "textSystemOne");
        

    }   
}
