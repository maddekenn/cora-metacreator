package se.uu.ub.cora.metacreator.recordtype;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class RecordTypeCreatorTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String userId;

	@BeforeTest
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		userId = "testUser";
	}

	// TODO: när barnen är på plats, kolla att mode är rätt
	@Test
	public void testRecordTypeCreatorNoMetadataGroupOrPresentationsExists() {
		RecordTypeCreator recordTypeCreator = RecordTypeCreator
				.forImplementingTextType("textSystemOne");

		SpiderDataGroup recordType = DataCreator
				.createSpiderDataGroupForRecordTypeWithId("myRecordType");
		DataCreator.addAllValuesToSpiderDataGroup(recordType, "myRecordType");

		recordTypeCreator.useExtendedFunctionality(userId, recordType);
		assertEquals(instanceFactory.spiderRecordCreators.size(), 10);

		assertCorrectlyCreatedMetadataGroup(2, "myRecordTypeGroup", "recordInfoGroup",
				"myRecordType");
		assertCorrectlyCreatedMetadataGroup(3, "myRecordTypeNewGroup", "recordInfoNewGroup",
				"myRecordType");

		assertCorrectlyCreatedPresentationGroupWithOnlyRecordInfo(4, "myRecordTypeFormPGroup",
				"myRecordTypeGroup", "recordInfoPGroup");
		assertCorrectlyCreatedPresentationGroupWithOnlyRecordInfo(8, "myRecordTypeFormNewPGroup",
				"myRecordTypeNewGroup", "recordInfoNewPGroup");

		assertCorrectlyCreatedPresentationGroupWithOnlyRecordInfo(5, "myRecordTypeViewPGroup",
				"myRecordTypeGroup", "recordInfoOutputPGroup");
		assertCorrectlyCreatedPresentationGroupWithOnlyRecordInfo(6, "myRecordTypeMenuPGroup",
				"myRecordTypeGroup", "recordInfoOutputPGroup");
		assertCorrectlyCreatedPresentationGroupWithOnlyRecordInfo(7, "myRecordTypeListPGroup",
				"myRecordTypeGroup", "recordInfoOutputPGroup");
		assertCorrectlyCreatedPresentationGroupWithOnlyRecordInfo(9,
				"myRecordTypeAutocompletePGroup", "myRecordTypeGroup", "recordInfoPGroup");

	}

	private void assertCorrectlyCreatedMetadataGroup(int createdPGroupNo, String id,
			String childRefId, String nameInData) {
		SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
				.get(createdPGroupNo);
		assertEquals(spiderRecordCreator.type, "metadataGroup");

		SpiderDataGroup record = spiderRecordCreator.record;
		assertEquals(record.extractAtomicValue("nameInData"), nameInData);
		assertCorrectUserAndRecordInfo(id, spiderRecordCreator);
		assertCorrectlyCreatedMetadataChildReference(childRefId, spiderRecordCreator.record);
		SpiderDataGroup textIdGroup = record.extractGroup("textId");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "text");
		SpiderDataGroup defTextIdGroup = record.extractGroup("defTextId");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "text");

		assertEquals(record.extractAtomicValue("excludePGroupCreation"), "true");
	}

	private void assertCorrectlyCreatedMetadataChildReference(String childRefId,
			SpiderDataGroup record) {
		SpiderDataGroup childRef = getChildRef(record);
		SpiderDataGroup ref = (SpiderDataGroup) childRef.getFirstChildWithNameInData("ref");

		assertEquals(ref.extractAtomicValue("linkedRecordId"), childRefId);
		assertEquals(ref.extractAtomicValue("linkedRecordType"), "metadata");
		assertEquals(childRef.extractAtomicValue("repeatMin"), "1");
		assertEquals(childRef.extractAtomicValue("repeatMax"), "1");
	}

	private void assertCorrectlyCreatedPresentationGroupWithOnlyRecordInfo(int createdPGroupNo,
			String id, String presentationOf, String childRefId) {

		SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
				.get(createdPGroupNo);
		assertCorrectlyCreatedPresentationGroup(spiderRecordCreator, createdPGroupNo, id,
				presentationOf);
		assertCorrectlyCreatedPresentationChildReference(childRefId, spiderRecordCreator.record);
	}

	private void assertCorrectlyCreatedPresentationGroup(SpiderRecordCreatorSpy spiderRecordCreator,
			int createdPGroupNo, String id, String presentationOf) {
		assertEquals(spiderRecordCreator.type, "presentationGroup");
		SpiderDataGroup presentationOfGroup = spiderRecordCreator.record
				.extractGroup("presentationOf");
		assertEquals(presentationOfGroup.extractAtomicValue("linkedRecordId"), presentationOf);
		assertCorrectUserAndRecordInfo(id, spiderRecordCreator);
	}

	private void assertCorrectUserAndRecordInfo(String id,
			SpiderRecordCreatorSpy spiderRecordCreator) {
		assertEquals(spiderRecordCreator.userId, userId);
		SpiderDataGroup recordInfo = spiderRecordCreator.record.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), id);

		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "test");
	}

	private void assertCorrectlyCreatedPresentationChildReference(String childRefId,
			SpiderDataGroup record) {
		SpiderDataGroup childRef = getChildRef(record);
		SpiderDataGroup refGroup = (SpiderDataGroup) childRef
				.getFirstChildWithNameInData("refGroup");
		assertEquals(refGroup.getRepeatId(), "0");

		SpiderDataGroup ref = (SpiderDataGroup) refGroup.getFirstChildWithNameInData("ref");
		assertEquals(ref.extractAtomicValue("linkedRecordId"), childRefId);
		assertEquals(ref.extractAtomicValue("linkedRecordType"), "presentation");
		assertFalse(childRef.containsChildWithNameInData("default"));
		assertFalse(childRef.containsChildWithNameInData("repeatMax"));

	}

	private SpiderDataGroup getChildRef(SpiderDataGroup record) {
		SpiderDataGroup childReferences = record.extractGroup("childReferences");
		assertEquals(childReferences.getChildren().size(), 1);
		SpiderDataGroup childRef = (SpiderDataGroup) childReferences
				.getFirstChildWithNameInData("childReference");
		return childRef;
	}

	@Test
	public void testPGroupCreatorAllPresentationsExists() {
		RecordTypeCreator pGroupCreator = RecordTypeCreator
				.forImplementingTextType("textSystemOne");

		SpiderDataGroup recordType = DataCreator
				.createSpiderDataGroupForRecordTypeWithId("myRecordType2");
		DataCreator.addAllValuesToSpiderDataGroup(recordType, "myRecordType2");

		pGroupCreator.useExtendedFunctionality(userId, recordType);
		assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
	}

	@Test
	public void testRecordTypeCreatorNoTextsExists() {
		RecordTypeCreator recordTypeCreator = RecordTypeCreator
				.forImplementingTextType("textSystemOne");

		SpiderDataGroup recordType = DataCreator
				.createSpiderDataGroupForRecordTypeWithId("myRecordType");
		DataCreator.addAllValuesToSpiderDataGroup(recordType, "myRecordType");

		recordTypeCreator.useExtendedFunctionality(userId, recordType);
		assertEquals(instanceFactory.spiderRecordCreators.size(), 10);
		SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators.get(0);
		assertEquals(spiderRecordCreator.type, "textSystemOne");
		SpiderRecordCreatorSpy spiderRecordCreator2 = instanceFactory.spiderRecordCreators.get(1);
		assertEquals(spiderRecordCreator2.type, "textSystemOne");

	}

	@Test
	public void testRecordTypeCreatorMetadataGroupsExistButNoPresentations() {
		RecordTypeCreator recordTypeCreator = RecordTypeCreator
				.forImplementingTextType("textSystemOne");

		SpiderDataGroup recordType = DataCreator
				.createSpiderDataGroupForRecordTypeWithId("myRecordType3");
		DataCreator.addAllValuesToSpiderDataGroup(recordType, "myRecordType3");

		recordTypeCreator.useExtendedFunctionality(userId, recordType);
		assertEquals(instanceFactory.spiderRecordCreators.size(), 8);

		assertCorrectPresentationByIndexIdModeRecordInfoRefAndChildPresentation(2,
				"myRecordType3FormPGroup", "input", "recordInfoPGroup", "somePVar", 2);
		assertCorrectPresentationByIndexIdModeRecordInfoRefAndChildPresentation(6,
				"myRecordType3FormNewPGroup", "input", "recordInfoNewPGroup", "somePVar", 2);
	}

	private void assertCorrectPresentationByIndexIdModeRecordInfoRefAndChildPresentation(int index,
			String id, String mode, String recordInfoRef, String childPresentationId,
			int expectedNumberOfChildren) {
		SpiderRecordCreatorSpy spiderRecordCreatorSpy = instanceFactory.spiderRecordCreators
				.get(index);
		SpiderDataGroup createdRecord = spiderRecordCreatorSpy.record;
		SpiderDataGroup childReferences = createdRecord.extractGroup("childReferences");
		assertEquals(childReferences.getChildren().size(), expectedNumberOfChildren);
		assertCorrectChildByIndexAndRefId(childReferences, 0, childPresentationId);
		assertCorrectChildByIndexAndRefId(childReferences, 1, recordInfoRef);
		SpiderDataGroup ref = getRef(childReferences);

		SpiderDataGroup recordInfo = createdRecord.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), id);

		assertEquals(ref.extractAtomicValue("linkedRecordId"), childPresentationId);
		assertEquals(createdRecord.extractAtomicValue("mode"), mode);
	}

	private SpiderDataGroup getRef(SpiderDataGroup childReferences) {
		SpiderDataGroup childReference = childReferences.extractGroup("childReference");
		SpiderDataGroup refGroup = childReference.extractGroup("refGroup");
		return refGroup.extractGroup("ref");
	}

	private void assertCorrectChildByIndexAndRefId(SpiderDataGroup childReferences, int index,
			String childRefId) {
		SpiderDataGroup recordInfo = (SpiderDataGroup) childReferences.getChildren().get(index);
		SpiderDataGroup refGroup = recordInfo.extractGroup("refGroup");
		SpiderDataGroup ref = refGroup.extractGroup("ref");
		assertEquals(ref.extractAtomicValue("linkedRecordId"), childRefId);
	}

}
