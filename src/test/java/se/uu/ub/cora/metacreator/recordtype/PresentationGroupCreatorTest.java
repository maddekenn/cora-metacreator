package se.uu.ub.cora.metacreator.recordtype;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PresentationGroupCreatorTest {
	
	@Test
	public void testCreatePGroup(){
		
		PresentationGroupCreator pGroupCreator = PresentationGroupCreator.withIdDataDividerAndPresentationOf("myRecordTypeViewId", "cora", "myRecordTypeGroup");
		SpiderDataGroup pGroup = pGroupCreator.createGroup("recordInfoPGroup");

        SpiderDataGroup recordInfo = pGroup.extractGroup("recordInfo");
        assertEquals(recordInfo.extractAtomicValue("id"), "myRecordTypeViewId");

		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordType"), "system");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "cora");
        
        assertEquals(pGroup.getAttributes().get("type"), "pGroup");

		SpiderDataGroup presentationOf = pGroup.extractGroup("presentationOf");
        assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), "myRecordTypeGroup");
        assertEquals(presentationOf.extractAtomicValue("linkedRecordType"), "metadataGroup");

		assertCorrectChildReferences(pGroup);
	}

	private void assertCorrectChildReferences(SpiderDataGroup metadataGroup) {
		SpiderDataGroup childRefs = metadataGroup.extractGroup("childReferences");
		assertEquals(childRefs.getChildren().size(), 1);
		SpiderDataGroup childRef = (SpiderDataGroup)childRefs.getFirstChildWithNameInData("childReference");
		SpiderDataAtomic ref = (SpiderDataAtomic) childRef.getFirstChildWithNameInData("ref");
		assertEquals(ref.getValue(), "recordInfoPGroup");
		SpiderDataAtomic repeatMin = (SpiderDataAtomic) childRef.getFirstChildWithNameInData("repeatMin");
		assertEquals(repeatMin.getValue(), "1");
		SpiderDataAtomic repeatMax = (SpiderDataAtomic) childRef.getFirstChildWithNameInData("repeatMax");
		assertEquals(repeatMax.getValue(), "1");
	}

}
