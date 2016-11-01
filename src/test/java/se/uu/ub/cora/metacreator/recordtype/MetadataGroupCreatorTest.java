package se.uu.ub.cora.metacreator.recordtype;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class MetadataGroupCreatorTest {

	
	@Test
	public void testCreateMetadataGroup(){
		MetadataGroupCreator creator = MetadataGroupCreator.withIdAndNameInDataAndDataDivider("myRecordTypeGroup", "myRecordType", "cora");
		SpiderDataGroup metadataGroup = creator.createGroup("recordInfoGroup");
		
		SpiderDataGroup recordInfo = metadataGroup.extractGroup("recordInfo");
        assertEquals(recordInfo.extractAtomicValue("id"), "myRecordTypeGroup");
        
        SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordType"), "system");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "cora");
		
		assertEquals(metadataGroup.extractAtomicValue("nameInData"), "myRecordType");
		assertEquals(metadataGroup.extractAtomicValue("textId"), "myRecordTypeGroupText");
		assertEquals(metadataGroup.extractAtomicValue("defTextId"), "myRecordTypeGroupDefText");
		
		assertCorrectChildReferences(metadataGroup);
		
		assertEquals(metadataGroup.getAttributes().get("type"), "group");
	}

	private void assertCorrectChildReferences(SpiderDataGroup metadataGroup) {
		SpiderDataGroup childRefs = metadataGroup.extractGroup("childReferences");
		assertEquals(childRefs.getChildren().size(), 1);
		
		SpiderDataGroup childRef = (SpiderDataGroup)childRefs.getFirstChildWithNameInData("childReference");
		SpiderDataAtomic ref = (SpiderDataAtomic) childRef.getFirstChildWithNameInData("ref");
		assertEquals(ref.getValue(), "recordInfoGroup");
		
		SpiderDataAtomic repeatMin = (SpiderDataAtomic) childRef.getFirstChildWithNameInData("repeatMin");
		assertEquals(repeatMin.getValue(), "1");
		
		SpiderDataAtomic repeatMax = (SpiderDataAtomic) childRef.getFirstChildWithNameInData("repeatMax");
		assertEquals(repeatMax.getValue(), "1");
	}
}
