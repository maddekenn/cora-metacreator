package se.uu.ub.cora.metacreator.recordtype;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

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

		assertCorrectlyCreatedPresentationGroupWithIndexIdPresentationOfModeAndRecordInfo(4, "myRecordTypeFormPGroup",
				"myRecordTypeGroup", "input", "recordInfoPGroup");
		assertCorrectlyCreatedPresentationGroupWithIndexIdPresentationOfModeAndRecordInfo(5, "myRecordTypeFormNewPGroup",
				"myRecordTypeNewGroup", "input", "recordInfoNewPGroup");

		assertCorrectlyCreatedPresentationGroupWithIndexIdPresentationOfModeAndRecordInfo(6, "myRecordTypeViewPGroup",
				"myRecordTypeGroup", "output", "recordInfoOutputPGroup");
		assertCorrectlyCreatedPresentationGroupWithIndexIdPresentationOfModeAndRecordInfo(7, "myRecordTypeMenuPGroup",
				"myRecordTypeGroup", "output", "recordInfoOutputPGroup");
		assertCorrectlyCreatedPresentationGroupWithIndexIdPresentationOfModeAndRecordInfo(8, "myRecordTypeListPGroup",
				"myRecordTypeGroup", "output", "recordInfoOutputPGroup");
		assertCorrectlyCreatedPresentationGroupWithIndexIdPresentationOfModeAndRecordInfo(9,
				"myRecordTypeAutocompletePGroup", "myRecordTypeGroup","input", "recordInfoPGroup");

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
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "coraText");
		SpiderDataGroup defTextIdGroup = record.extractGroup("defTextId");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "coraText");

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

	private void assertCorrectlyCreatedPresentationGroupWithIndexIdPresentationOfModeAndRecordInfo(int index,
																								   String id, String presentationOf, String mode, String childRefId) {

		SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
				.get(index);
		assertCorrectlyCreatedPresentationGroup(spiderRecordCreator, index, id,
				presentationOf, mode);
		assertCorrectlyCreatedPresentationChildReference(childRefId, spiderRecordCreator.record);
	}

	private void assertCorrectlyCreatedPresentationGroup(SpiderRecordCreatorSpy spiderRecordCreator,
														 int createdPGroupNo, String id, String presentationOf, String mode) {
		assertEquals(spiderRecordCreator.type, "presentationGroup");
		SpiderDataGroup record = spiderRecordCreator.record;
		SpiderDataGroup presentationOfGroup = record
				.extractGroup("presentationOf");
		assertEquals(presentationOfGroup.extractAtomicValue("linkedRecordId"), presentationOf);
		assertCorrectUserAndRecordInfo(id, spiderRecordCreator);
		assertEquals(record.extractAtomicValue("mode"), mode);
	}

	private void assertCorrectUserAndRecordInfo(String id,
			SpiderRecordCreatorSpy spiderRecordCreator) {
		assertEquals(spiderRecordCreator.authToken, userId);
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
		assertCorrectPresentationByIndexIdModeRecordInfoRefAndChildPresentation(3,
				"myRecordType3FormNewPGroup", "input", "recordInfoNewPGroup", "somePVar", 2);

		assertCorrectPresentationByIndexIdModeRecordInfoRefAndChildPresentation(4, "myRecordType3ViewPGroup",
				"output", "recordInfoOutputPGroup", "someOutputPVar", 2);
		assertCorrectlyCreatedPresentationGroupWithIndexIdPresentationOfModeAndRecordInfo(5, "myRecordType3MenuPGroup",
				"myRecordType3Group", "output", "recordInfoOutputPGroup");
		assertCorrectlyCreatedPresentationGroupWithIndexIdPresentationOfModeAndRecordInfo(6, "myRecordType3ListPGroup",
				"myRecordType3Group", "output", "recordInfoOutputPGroup");
		assertCorrectlyCreatedPresentationGroupWithIndexIdPresentationOfModeAndRecordInfo(7,
				"myRecordType3AutocompletePGroup", "myRecordType3Group", "input", "recordInfoPGroup");
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

	@Test
	public void testRecordTypeCreatorMetadataGroupsExistButNoPresentationsAndOneChildPresentationDoesNotExist() {
		RecordTypeCreator recordTypeCreator = RecordTypeCreator
				.forImplementingTextType("textSystemOne");

		SpiderDataGroup recordType = DataCreator
				.createSpiderDataGroupForRecordTypeWithId("myRecordType4");
		DataCreator.addAllValuesToSpiderDataGroup(recordType, "myRecordType4");

		recordTypeCreator.useExtendedFunctionality(userId, recordType);
		assertEquals(instanceFactory.spiderRecordCreators.size(), 8);
		assertCorrectNumberOfChildReferencesForIndex(2, 2);
		assertCorrectNumberOfChildReferencesForIndex(2, 3);
		assertCorrectNumberOfChildReferencesForIndex(2, 4);
		assertCorrectNumberOfChildReferencesForIndex(1, 5);
		assertCorrectNumberOfChildReferencesForIndex(1, 6);
		assertCorrectNumberOfChildReferencesForIndex(1, 7);

	}

	private void assertCorrectNumberOfChildReferencesForIndex(int numberOfChildReferences, int index) {
		SpiderRecordCreatorSpy spiderRecordCreatorSpy = instanceFactory.spiderRecordCreators.get(index);
		SpiderDataGroup childReferences = spiderRecordCreatorSpy.record.extractGroup("childReferences");
		assertEquals(childReferences.getChildren().size(), numberOfChildReferences);
	}

}
