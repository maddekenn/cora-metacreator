package se.uu.ub.cora.metacreator.group;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.record.DataException;

public class PGroupConstructorTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String authToken;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		authToken = "testUser";
	}

	@Test
	public void testGroupConstructorForInput() {
		PGroupConstructor constructor = new PGroupConstructor(authToken);
		List<SpiderDataElement> metadataChildReferences = createChildren();
		SpiderDataGroup pGroup = constructor
				.constructPGroupWithIdDataDividerPresentationOfChildrenAndMode("someTestPGroup",
						"testSystem", "someTestGroup", metadataChildReferences, "input");
		assertEquals(pGroup.getAttributes().get("type"), "pGroup");
		assertEquals(pGroup.getNameInData(), "presentation");
		assertCorrectRecordInfo(pGroup);
		assertCorrectPresentationOf(pGroup);

		assertCorrectChildReferences(pGroup);
		assertEquals(instanceFactory.spiderRecordReaders.size(), 6);
	}

	private List<SpiderDataElement> createChildren() {
		List<SpiderDataElement> childReferences = new ArrayList<>();

		SpiderDataGroup childRef = createMetadataChildRefWithIdAndRepeatId(
				"identifierTypeCollectionVar", "0");
		childReferences.add(childRef);

		SpiderDataGroup childRef2 = createMetadataChildRefWithIdAndRepeatId(
				"identifierValueTextVar", "1");
		childReferences.add(childRef2);

		SpiderDataGroup childRef3 = createMetadataChildRefWithIdAndRepeatId(
				"identifierResLink", "2");
		childReferences.add(childRef3);

		SpiderDataGroup childRef4 = createMetadataChildRefWithIdAndRepeatId("identifierLink", "3");
		childReferences.add(childRef4);

		SpiderDataGroup childRef5 = createMetadataChildRefWithIdAndRepeatId("identifierChildGroup",
				"4");
		childReferences.add(childRef5);
		SpiderDataGroup childRef6 = createMetadataChildRefWithIdAndRepeatId(
				"identifierChildGroupWithUnclearEnding", "5");
		childReferences.add(childRef6);
		SpiderDataGroup childRef7 = createMetadataChildRefWithIdAndRepeatId(
				"identifierChildHasNoPresentationTextVar", "6");
		childReferences.add(childRef7);
		return childReferences;
	}

	private SpiderDataGroup createMetadataChildRefWithIdAndRepeatId(String childRefId,
			String repeatId) {
		SpiderDataGroup childRef = SpiderDataGroup.withNameInData("childReference");
		childRef.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMin", "1"));
		childRef.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMax", "1"));
		childRef.setRepeatId(repeatId);

		SpiderDataGroup ref = SpiderDataGroup.withNameInData("ref");
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "metadata"));
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", childRefId));
		childRef.addChild(ref);
		return childRef;
	}

	private void assertCorrectChildReferences(SpiderDataGroup pGroup) {
		SpiderDataGroup childReferences = pGroup.extractGroup("childReferences");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 0, "0",
				"identifierTypeCollectionVarText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 1, "1",
				"identifierTypePCollVar", "presentationCollectionVar");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 2, "2",
				"identifierValueTextVarText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 3, "3",
				"identifierValuePVar", "presentationVar");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 4, "4",
				"identifierResLinkText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 5, "5",
				"identifierPResLink", "presentationResourceLink");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 6, "6",
				"identifierLinkText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 7, "7",
				"identifierPLink", "presentationRecordLink");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 8, "8",
				"identifierChildGroupText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 9, "9",
				"identifierChildPGroup", "presentationGroup");
		assertEquals(childReferences.getChildren().size(), 10);

	}

	private void assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(
			SpiderDataGroup childReferences, int index, String repeatId, String id,
			String recordType) {
		SpiderDataGroup firstChild = (SpiderDataGroup) childReferences.getChildren().get(index);
		assertEquals(firstChild.getNameInData(), "childReference");
		assertEquals(firstChild.getRepeatId(), repeatId);

		SpiderDataGroup refGroup = firstChild.extractGroup("refGroup");
		assertEquals(refGroup.getRepeatId(), "0");

		SpiderDataGroup firstChildRef = refGroup.extractGroup("ref");
		assertEquals(firstChildRef.extractAtomicValue("linkedRecordType"), recordType);
		assertEquals(firstChildRef.extractAtomicValue("linkedRecordId"), id);
		String typeAttribute = "presentation";
		if("coraText".equals(recordType)){
			typeAttribute = "text";
		}
		assertEquals(firstChildRef.getAttributes().get("type"), typeAttribute);
	}

	private void assertCorrectRecordInfo(SpiderDataGroup pGroup) {
		SpiderDataGroup recordInfo = pGroup.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), "someTestPGroup");

		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "testSystem");
	}

	private void assertCorrectPresentationOf(SpiderDataGroup pGroup) {
		SpiderDataGroup presentationOf = pGroup.extractGroup("presentationOf");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordType"), "metadataGroup");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), "someTestGroup");
	}

	@Test
	public void testGroupConstructorForOutput() {
		PGroupConstructor constructor = new PGroupConstructor(authToken);
		List<SpiderDataElement> childReferences = createChildren();
		SpiderDataGroup pGroup = constructor
				.constructPGroupWithIdDataDividerPresentationOfChildrenAndMode("someTestPGroup",
						"testSystem", "someTestGroup", childReferences, "output");
		assertEquals(pGroup.getAttributes().get("type"), "pGroup");
		assertCorrectRecordInfo(pGroup);
		assertCorrectPresentationOf(pGroup);

		assertCorrectOutputChildReferences(pGroup);
	}

	private void assertCorrectOutputChildReferences(SpiderDataGroup pGroup) {
		SpiderDataGroup childReferences = pGroup.extractGroup("childReferences");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 0, "0",
				"identifierTypeCollectionVarText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 1, "1",
				"identifierTypeOutputPCollVar", "presentationCollectionVar");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 2, "2",
				"identifierValueTextVarText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 3, "3",
				"identifierValueOutputPVar", "presentationVar");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 4, "4",
				"identifierResLinkText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 5, "5",
				"identifierOutputPResLink", "presentationResourceLink");

		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 6, "6",
				"identifierLinkText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 7, "7",
				"identifierOutputPLink", "presentationRecordLink");


		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 8, "8",
				"identifierChildGroupText", "coraText");
		assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(childReferences, 9, "9",
				"identifierChildOutputPGroup", "presentationGroup");

		assertEquals(childReferences.getChildren().size(), 10);

	}

	@Test(expectedExceptions = DataException.class)
	public void testGroupConstructorWithNoIdentifiedChildren() {
		PGroupConstructor constructor = new PGroupConstructor(authToken);
		List<SpiderDataElement> childReferences = new ArrayList<SpiderDataElement>();

		SpiderDataGroup childRef = createMetadataChildRefWithIdAndRepeatId(
				"identifierChildGroupWithUnclearEnding", "5");
		childReferences.add(childRef);

		constructor.constructPGroupWithIdDataDividerPresentationOfChildrenAndMode("someTestPGroup",
				"testSystem", "someTestGroup", childReferences, "output");
	}
}
