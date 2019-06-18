package se.uu.ub.cora.metacreator.recordtype;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;

public class MetadataGroupCreatorTest {

	@Test
	public void testCreateMetadataGroup() {
		MetadataGroupCreator creator = MetadataGroupCreator
				.withIdAndNameInDataAndDataDivider("myRecordTypeGroup", "myRecordType", "cora");
		DataGroup metadataGroup = creator.createGroup("recordInfoGroup");

		DataGroup recordInfo = metadataGroup.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "myRecordTypeGroup");

		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordType"), "system");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId"), "cora");

		assertEquals(metadataGroup.getFirstAtomicValueWithNameInData("nameInData"), "myRecordType");
		DataGroup textIdGroup = metadataGroup.getFirstGroupWithNameInData("textId");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"myRecordTypeGroupText");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"), "coraText");
		DataGroup defTextIdGroup = metadataGroup.getFirstGroupWithNameInData("defTextId");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"myRecordTypeGroupDefText");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"coraText");

		assertCorrectChildReferences(metadataGroup);

		assertEquals(metadataGroup.getAttributes().get("type"), "group");
	}

	private void assertCorrectChildReferences(DataGroup metadataGroup) {
		DataGroup childRefs = metadataGroup.getFirstGroupWithNameInData("childReferences");
		assertEquals(childRefs.getChildren().size(), 1);

		DataGroup childRef = (DataGroup) childRefs.getFirstChildWithNameInData("childReference");
		DataGroup ref = (DataGroup) childRef.getFirstChildWithNameInData("ref");
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordId"), "recordInfoGroup");
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordType"), "metadataGroup");

		DataAtomic repeatMin = (DataAtomic) childRef.getFirstChildWithNameInData("repeatMin");
		assertEquals(repeatMin.getValue(), "1");

		DataAtomic repeatMax = (DataAtomic) childRef.getFirstChildWithNameInData("repeatMax");
		assertEquals(repeatMax.getValue(), "1");
	}
}
