package se.uu.ub.cora.metacreator.recordtype;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
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
		PresentationGroupCreator pGroupCreator = createPresentationGroupCreatorWithIdModeAndChildId("myRecordTypeViewId", "input", "searchTitleTextVar");
		pGroupCreator.createPGroupIfNotAlreadyExist();

		List<SpiderRecordCreatorSpy> spiderRecordCreators = instanceFactory.spiderRecordCreators;
		SpiderRecordCreatorSpy spiderRecordCreatorSpy = spiderRecordCreators.get(0);

		assertEquals(spiderRecordCreators.size(), 1);
		assertEquals(spiderRecordCreatorSpy.authToken, "testUser");

		SpiderDataGroup record = spiderRecordCreatorSpy.record;
		assertCorrectIdAndDataDivider(record);

		assertEquals(record.extractAtomicValue("mode"), "input");
		SpiderDataGroup presentationOf = record.extractGroup("presentationOf");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), "myRecordType");

		assertCorrectChildReferences(record);

	}

	private PresentationGroupCreator createPresentationGroupCreatorWithIdModeAndChildId(String id, String mode, String childId) {
		PresentationGroupCreator pGroupCreator = PresentationGroupCreator
				.withAuthTokenPresentationIdAndDataDivider("testUser", id,
						"cora");

		pGroupCreator.setPresentationOfAndMode("myRecordType", mode);
		List<SpiderDataElement> metadataChildren = createMetadataChildReferences();
		pGroupCreator.setMetadataChildReferences(metadataChildren);
		return pGroupCreator;
	}

	private void assertCorrectIdAndDataDivider(SpiderDataGroup record) {
		SpiderDataGroup recordInfo = record.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), "myRecordTypeViewId");
		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordType"), "system");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "cora");
	}

	private void assertCorrectChildReferences(SpiderDataGroup record) {
		SpiderDataGroup childReferences = record.extractGroup("childReferences");
		assertEquals(childReferences.getChildren().size(), 1);
		SpiderDataGroup ref = extractRefFromFirstChildReference(childReferences);
		assertEquals(ref.extractAtomicValue("linkedRecordId"), "searchTitlePVar");
		assertEquals(ref.getAttributes().get("type"), "presentation");
	}

	private SpiderDataGroup extractRefFromFirstChildReference(SpiderDataGroup childReferences) {
		SpiderDataGroup childReference = childReferences.extractGroup("childReference");
		SpiderDataGroup refGRoup = childReference.extractGroup("refGroup");
		return refGRoup.extractGroup("ref");
	}

	private List<SpiderDataElement> createMetadataChildReferences() {
		List<SpiderDataElement> metadataChildReferences = new ArrayList<>();
		SpiderDataGroup childReference = createChildReference();

		addRefPartToChildReference(childReference, "searchTitleTextVar");
		childReference.setRepeatId("0");
		metadataChildReferences.add(childReference);
		return metadataChildReferences;
	}

	private void addRefPartToChildReference(SpiderDataGroup childReference, String childId) {
		SpiderDataGroup ref = SpiderDataGroup.withNameInData("ref");
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "metadata"));
		ref.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", childId));
		childReference.addChild(ref);
	}

	private SpiderDataGroup createChildReference() {
		SpiderDataGroup childReference = SpiderDataGroup.withNameInData("childReference");
		childReference.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMin", "1"));
		childReference.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMax", "1"));
		return childReference;
	}

	@Test
	public void testCreatePresentationGroupWhenPGroupAlreadyExist() {
		PresentationGroupCreator pGroupCreator = createPresentationGroupCreatorWithIdModeAndChildId("someExistingPGroup", "input", "searchTitleTextVar");
		pGroupCreator.createPGroupIfNotAlreadyExist();
		assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
	}

}
