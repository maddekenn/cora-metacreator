package se.uu.ub.cora.metacreator.recordtype;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PGroupCreatorTest {
	
	@Test
	public void testCreatePGroup(){
		
		PGroupCreator pGroupCreator = PGroupCreator.withIdDataDividerAndPresentationOf("myRecordTypeViewId", "cora", "myRecordTypeGroup");
		SpiderDataGroup pGroup = pGroupCreator.createPresentationGroup();

        SpiderDataGroup recordInfo = pGroup.extractGroup("recordInfo");
        assertEquals(recordInfo.extractAtomicValue("id"), "myRecordTypeViewId");
        SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordType"), "system");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "cora");
        
        assertEquals(pGroup.getAttributes().get("type"), "pGroup");
        SpiderDataGroup presentationOf = pGroup.extractGroup("presentationOf");
        assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), "myRecordTypeGroup");
	}

}
