package se.uu.ub.cora.metacreator.recordtype;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataElement;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class PresentationGroupCreatorTest {

	private SpiderInstanceFactorySpy instanceFactory;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
	}

	@Test
	public void testCreatePresentationGroupWhenPGroupDoesNotExist() {
		PresentationGroupCreator pGroupCreator = createPresentationGroupCreatorWithIdModeAndChildId(
				"myRecordTypeViewId", "input", "searchTitleTextVar");
		pGroupCreator.createPGroupIfNotAlreadyExist();

		List<SpiderRecordCreatorSpy> spiderRecordCreators = instanceFactory.spiderRecordCreators;
		SpiderRecordCreatorSpy spiderRecordCreatorSpy = spiderRecordCreators.get(0);

		assertEquals(spiderRecordCreators.size(), 1);
		assertEquals(spiderRecordCreatorSpy.authToken, "testUser");

		DataGroup record = spiderRecordCreatorSpy.record;
		assertCorrectIdAndDataDivider(record);

		assertEquals(record.getFirstAtomicValueWithNameInData("mode"), "input");
		DataGroup presentationOf = record.getFirstGroupWithNameInData("presentationOf");
		assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"myRecordType");

		assertCorrectChildReferences(record);

	}

	private PresentationGroupCreator createPresentationGroupCreatorWithIdModeAndChildId(String id,
			String mode, String childId) {
		PresentationGroupCreator pGroupCreator = PresentationGroupCreator
				.withAuthTokenPresentationIdAndDataDivider("testUser", id, "cora");

		pGroupCreator.setPresentationOfAndMode("myRecordType", mode);
		List<DataElement> metadataChildren = createMetadataChildReferences();
		pGroupCreator.setMetadataChildReferences(metadataChildren);
		return pGroupCreator;
	}

	private void assertCorrectIdAndDataDivider(DataGroup record) {
		DataGroup recordInfo = record.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "myRecordTypeViewId");
		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordType"), "system");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId"), "cora");
	}

	private void assertCorrectChildReferences(DataGroup record) {
		DataGroup childReferences = record.getFirstGroupWithNameInData("childReferences");
		assertEquals(childReferences.getChildren().size(), 2);
		DataGroup refText = extractRefFromChildReferenceByChildIndex(childReferences, 0);
		assertEquals(refText.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"searchTitleTextVarText");
		assertEquals(refText.getAttributes().get("type"), "text");
		DataGroup ref = extractRefFromChildReferenceByChildIndex(childReferences, 1);
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordId"), "searchTitlePVar");
		assertEquals(ref.getAttributes().get("type"), "presentation");
	}

	private DataGroup extractRefFromChildReferenceByChildIndex(DataGroup childReferences,
			int index) {
		DataGroup childReference = childReferences.getAllGroupsWithNameInData("childReference")
				.get(index);
		DataGroup refGRoup = childReference.getFirstGroupWithNameInData("refGroup");
		return refGRoup.getFirstGroupWithNameInData("ref");
	}

	private List<DataElement> createMetadataChildReferences() {
		List<DataElement> metadataChildReferences = new ArrayList<>();
		DataGroup childReference = createChildReference();

		addRefPartToChildReference(childReference, "searchTitleTextVar");
		childReference.setRepeatId("0");
		metadataChildReferences.add(childReference);
		return metadataChildReferences;
	}

	private void addRefPartToChildReference(DataGroup childReference, String childId) {
		DataGroup ref = DataGroup.withNameInData("ref");
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "metadata"));
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", childId));
		childReference.addChild(ref);
	}

	private DataGroup createChildReference() {
		DataGroup childReference = DataGroup.withNameInData("childReference");
		childReference.addChild(DataAtomic.withNameInDataAndValue("repeatMin", "1"));
		childReference.addChild(DataAtomic.withNameInDataAndValue("repeatMax", "1"));
		return childReference;
	}

	@Test
	public void testCreatePresentationGroupWhenPGroupAlreadyExist() {
		PresentationGroupCreator pGroupCreator = createPresentationGroupCreatorWithIdModeAndChildId(
				"someExistingPGroup", "input", "searchTitleTextVar");
		pGroupCreator.createPGroupIfNotAlreadyExist();
		assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
	}

}
