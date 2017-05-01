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
		assertEquals(instanceFactory.spiderRecordCreators.size(), 9);

		assertCorrectlyCreatedMetadataGroup(2, "myRecordTypeGroup", "recordInfoGroup",
				"myRecordType");
		assertCorrectlyCreatedMetadataGroup(3, "myRecordTypeNewGroup", "recordInfoNewGroup",
				"myRecordType");

		assertCorrectlyCreatedPresentationGroup(4, "myRecordTypeFormPGroup", "myRecordTypeGroup",
				"recordInfoPGroup");
		assertCorrectlyCreatedPresentationGroup(5, "myRecordTypeViewPGroup", "myRecordTypeGroup",
				"recordInfoOutputPGroup");
		assertCorrectlyCreatedPresentationGroup(6, "myRecordTypeMenuPGroup", "myRecordTypeGroup",
				"recordInfoOutputPGroup");
		assertCorrectlyCreatedPresentationGroup(7, "myRecordTypeListPGroup", "myRecordTypeGroup",
				"recordInfoOutputPGroup");
		assertCorrectlyCreatedPresentationGroup(8, "myRecordTypeFormNewPGroup",
				"myRecordTypeNewGroup", "recordInfoNewPGroup");

	}

	private void assertCorrectlyCreatedMetadataGroup(int createdPGroupNo, String id,
			String childRefId, String nameInData) {
		SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
				.get(createdPGroupNo);
		assertEquals(spiderRecordCreator.type, "metadataGroup");

		SpiderDataGroup record = spiderRecordCreator.record;
		assertEquals(record.extractAtomicValue("nameInData"), nameInData);
		assertCorrectUserAndRecordInfo(id, spiderRecordCreator);
		assertCorrectlyCreatedMetadataChildReference(childRefId, spiderRecordCreator);
		SpiderDataGroup textIdGroup = record.extractGroup("textId");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "text");
		SpiderDataGroup defTextIdGroup = record.extractGroup("defTextId");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "text");

		assertEquals(record.extractAtomicValue("excludePGroupCreation"), "true");
	}

	private void assertCorrectlyCreatedMetadataChildReference(String childRefId,
			SpiderRecordCreatorSpy spiderRecordCreator) {
		SpiderDataGroup childRef = getChildRef(spiderRecordCreator);
		SpiderDataGroup ref = (SpiderDataGroup) childRef.getFirstChildWithNameInData("ref");

		assertEquals(ref.extractAtomicValue("linkedRecordId"), childRefId);
		assertEquals(ref.extractAtomicValue("linkedRecordType"), "metadata");
		assertEquals(childRef.extractAtomicValue("repeatMin"), "1");
		assertEquals(childRef.extractAtomicValue("repeatMax"), "1");
	}

	private void assertCorrectlyCreatedPresentationChildReference(String childRefId,
			SpiderRecordCreatorSpy spiderRecordCreator) {
		SpiderDataGroup childRef = getChildRef(spiderRecordCreator);
		SpiderDataGroup refGroup = (SpiderDataGroup) childRef
				.getFirstChildWithNameInData("refGroup");
		assertEquals(refGroup.getRepeatId(), "0");

		SpiderDataGroup ref = (SpiderDataGroup) refGroup.getFirstChildWithNameInData("ref");
		assertEquals(ref.extractAtomicValue("linkedRecordId"), childRefId);
		assertEquals(ref.extractAtomicValue("linkedRecordType"), "presentation");
		assertFalse(childRef.containsChildWithNameInData("default"));
		assertFalse(childRef.containsChildWithNameInData("repeatMax"));

	}

	private SpiderDataGroup getChildRef(SpiderRecordCreatorSpy spiderRecordCreator) {
		SpiderDataGroup childReferences = spiderRecordCreator.record
				.extractGroup("childReferences");
		SpiderDataGroup childRef = (SpiderDataGroup) childReferences
				.getFirstChildWithNameInData("childReference");
		return childRef;
	}

	private void assertCorrectlyCreatedPresentationGroup(int createdPGroupNo, String id,
			String presentationOf, String childRefId) {
		SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
				.get(createdPGroupNo);
		assertEquals(spiderRecordCreator.type, "presentationGroup");
		SpiderDataGroup presentationOfGroup = spiderRecordCreator.record
				.extractGroup("presentationOf");
		assertEquals(presentationOfGroup.extractAtomicValue("linkedRecordId"), presentationOf);
		assertCorrectUserAndRecordInfo(id, spiderRecordCreator);
		assertCorrectlyCreatedPresentationChildReference(childRefId, spiderRecordCreator);
	}

	private void assertCorrectUserAndRecordInfo(String id,
			SpiderRecordCreatorSpy spiderRecordCreator) {
		assertEquals(spiderRecordCreator.userId, userId);
		SpiderDataGroup recordInfo = spiderRecordCreator.record.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), id);

		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "test");
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
		assertEquals(instanceFactory.spiderRecordCreators.size(), 9);
		SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators.get(0);
		assertEquals(spiderRecordCreator.type, "textSystemOne");
		SpiderRecordCreatorSpy spiderRecordCreator2 = instanceFactory.spiderRecordCreators.get(1);
		assertEquals(spiderRecordCreator2.type, "textSystemOne");

	}
}
