package se.uu.ub.cora.metacreator.recordtype;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

import java.util.ArrayList;
import java.util.List;

public class PresentationGroupCreatorTest {

	@Test
	public void testCreatePGroup() {
		SpiderInstanceFactorySpy instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		PresentationGroupCreator pGroupCreator = PresentationGroupCreator
				.withAuthTokenPresentationIdAndDataDivider("testUser", "myRecordTypeViewId", "cora");

		pGroupCreator.setPresentationOfAndMode("myRecordType", "input");
		List<SpiderDataElement> metadataChildren = createMetadataChildReferences();
		pGroupCreator.setMetadataChildReferences(metadataChildren);

		pGroupCreator.createGroup();
		List<SpiderRecordCreatorSpy> spiderRecordCreators = instanceFactory.spiderRecordCreators;
		SpiderRecordCreatorSpy spiderRecordCreatorSpy = spiderRecordCreators.get(0);

		assertEquals(pGroupCreator.presentationId, "myRecordTypeViewId");

		assertEquals(spiderRecordCreators.size(), 1);
		assertEquals(spiderRecordCreatorSpy.userId, "testUser");

		SpiderDataGroup record = spiderRecordCreatorSpy.record;
		assertEquals(record.extractAtomicValue("mode"), "input");
		SpiderDataGroup presentationOf = record.extractGroup("presentationOf");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), "myRecordType");

		SpiderDataGroup recordInfo = record.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), "myRecordTypeViewId");




//

//		SpiderDataGroup pGroup = pGroupCreator.createGroup("recordInfoPGroup");
//
//		SpiderDataGroup recordInfo = pGroup.extractGroup("recordInfo");
//		assertEquals(recordInfo.extractAtomicValue("id"), "myRecordTypeViewId");
//
//		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
//		assertEquals(dataDivider.extractAtomicValue("linkedRecordType"), "system");
//		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "cora");
//
//		assertEquals(pGroup.getAttributes().get("type"), "pGroup");
//
//		SpiderDataGroup presentationOf = pGroup.extractGroup("presentationOf");
//		assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), "myRecordTypeGroup");
//		assertEquals(presentationOf.extractAtomicValue("linkedRecordType"), "metadataGroup");
////		assertEquals(pGroup.extractAtomicValue("mode"), "output");
//		assertCorrectChildReferences(pGroup);
	}

	private List<SpiderDataElement> createMetadataChildReferences() {
		List<SpiderDataElement> metadataChildReferences = new ArrayList<>();
		SpiderDataGroup childReference = SpiderDataGroup.withNameInData("childReference");
		childReference.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMin", "1"));
		childReference.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMax", "1"));

		SpiderDataGroup ref = SpiderDataGroup.withNameInData("ref");
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "metadata"));
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", "searchTitleTextVar"));
		childReference.addChild(ref);
		childReference.setRepeatId("0");
		metadataChildReferences.add(childReference);
		return metadataChildReferences;

//		{
//			"name": "childReference",
//				"children": [
//			{
//				"name": "repeatMin",
//					"value": "1"
//			},
//			{
//				"name": "repeatMax",
//					"value": "1"
//			},
//			{
//				"name": "ref",
//					"children": [
//				{
//					"name": "linkedRecordType",
//						"value": "metadata"
//				},
//				{
//					"name": "linkedRecordId",
//						"value": "searchTitleTextVar"
//				}
//				]
//			}
//			],
//			"repeatId": "0"
//		}
	}

	private void assertCorrectChildReferences(SpiderDataGroup metadataGroup) {
		SpiderDataGroup childRefs = metadataGroup.extractGroup("childReferences");
		assertEquals(childRefs.getChildren().size(), 1);

		SpiderDataGroup childRef = (SpiderDataGroup) childRefs
				.getFirstChildWithNameInData("childReference");
		SpiderDataGroup refGroup = (SpiderDataGroup) childRef
				.getFirstChildWithNameInData("refGroup");

		SpiderDataGroup ref = (SpiderDataGroup) refGroup.getFirstChildWithNameInData("ref");
		assertEquals(ref.extractAtomicValue("linkedRecordId"), "recordInfoPGroup");
		assertEquals(ref.extractAtomicValue("linkedRecordType"), "presentation");
		assertEquals(ref.getAttributes().get("type"), "presentation");

	}

}
