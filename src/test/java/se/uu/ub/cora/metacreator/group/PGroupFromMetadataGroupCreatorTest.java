package se.uu.ub.cora.metacreator.group;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.collection.PCollVarFromCollectionVarCreator;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class PGroupFromMetadataGroupCreatorTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String authToken;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		authToken = "testUser";
	}

	@Test
	public void testPGroupsDoesNotExist() {
		SpiderDataGroup metadataGroup = DataCreator.createMetadataGroupWithId("someTestGroup");

		PGroupFromMetadataGroupCreator creator = new PGroupFromMetadataGroupCreator();
		creator.useExtendedFunctionality(authToken, metadataGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 2);

		assertCorrectPGroupWithIndexPGroupIdAndChildId(0, "someTestPGroup", "somePVar");
		assertCorrectPGroupWithIndexPGroupIdAndChildId(1, "someTestOutputPGroup", "someOutputPVar");

	}

	private void assertCorrectPGroupWithIndexPGroupIdAndChildId(int index, String pGroupId, String childId) {
		SpiderRecordCreatorSpy spiderRecordCreatorSpy = instanceFactory.spiderRecordCreators.get(index);
		assertEquals(spiderRecordCreatorSpy.type, "presentationGroup");

		SpiderDataGroup record = spiderRecordCreatorSpy.record;

		assertEquals(record.getNameInData(), "presentation");

		assertCorrectRecordInfo(record, pGroupId);
		assertCorrectPresentationOf(record);

		assertCorrectChildRef(childId, record);
	}

	private void assertCorrectRecordInfo(SpiderDataGroup record, String expectedId) {
		SpiderDataGroup recordInfo = record.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), expectedId);
		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "test");
	}

	private void assertCorrectPresentationOf(SpiderDataGroup record) {
		SpiderDataGroup presentationOf = record.extractGroup("presentationOf");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), "someTestGroup");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordType"), "metadataGroup");
	}

	private void assertCorrectChildRef(String childId, SpiderDataGroup record) {
		SpiderDataGroup childReferences = record.extractGroup("childReferences");
		SpiderDataGroup childReference = childReferences.extractGroup("childReference");
		SpiderDataGroup refGroup = childReference.extractGroup("refGroup");
		SpiderDataGroup ref = refGroup.extractGroup("ref");
		assertEquals(ref.extractAtomicValue("linkedRecordType"), "presentation");
		assertEquals(ref.extractAtomicValue("linkedRecordId"), childId);
	}

	@Test
	public void testPGroupsAlreadyExist() {
		SpiderDataGroup metadataGroup = DataCreator.createMetadataGroupWithId("someExistingGroup");

		PGroupFromMetadataGroupCreator creator = new PGroupFromMetadataGroupCreator();
		creator.useExtendedFunctionality(authToken, metadataGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
	}

}
