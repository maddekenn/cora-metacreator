package se.uu.ub.cora.metacreator;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;

public class MetadataCompleterTest {

	@Test
	public void testCompleteTextsNoTextIdsExist() {
		MetadataCompleter metaCompleter = new MetadataCompleter();
		DataGroup metadataGroup = createItemWithNoTexts();
		metaCompleter.completeSpiderDataGroupWithTexts(metadataGroup);

		assertEquals(metadataGroup.getFirstAtomicValueWithNameInData("textId"), "someIdText");
		assertEquals(metadataGroup.getFirstAtomicValueWithNameInData("defTextId"), "someIdDefText");
	}

	private DataGroup createItemWithNoTexts() {
		DataGroup metadataGroup = DataGroup.withNameInData("metadata");
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", "someId"));
		metadataGroup.addChild(recordInfo);
		return metadataGroup;
	}

	@Test
	public void testCompleteTextsTextIdExists() {
		MetadataCompleter metaCompleter = new MetadataCompleter();
		DataGroup metadataGroup = createItemWithNoTexts();
		metadataGroup.addChild(DataAtomic.withNameInDataAndValue("textId", "someText"));

		metaCompleter.completeSpiderDataGroupWithTexts(metadataGroup);

		assertEquals(metadataGroup.getFirstAtomicValueWithNameInData("textId"), "someText");
		assertEquals(metadataGroup.getFirstAtomicValueWithNameInData("defTextId"), "someIdDefText");
	}

	@Test
	public void testCompleteTextsDefTextIdExists() {
		MetadataCompleter metaCompleter = new MetadataCompleter();
		DataGroup metadataGroup = createItemWithNoTexts();
		metadataGroup.addChild(DataAtomic.withNameInDataAndValue("defTextId", "someDefText"));

		metaCompleter.completeSpiderDataGroupWithTexts(metadataGroup);

		assertEquals(metadataGroup.getFirstAtomicValueWithNameInData("textId"), "someIdText");
		assertEquals(metadataGroup.getFirstAtomicValueWithNameInData("defTextId"), "someDefText");
	}

	@Test
	public void testCompleteTextsTextIdAndDefTextIdExist() {
		MetadataCompleter metaCompleter = new MetadataCompleter();
		DataGroup metadataGroup = createItemWithNoTexts();
		metadataGroup.addChild(DataAtomic.withNameInDataAndValue("textId", "someText"));
		metadataGroup.addChild(DataAtomic.withNameInDataAndValue("defTextId", "someDefText"));

		metaCompleter.completeSpiderDataGroupWithTexts(metadataGroup);

		assertEquals(metadataGroup.getFirstAtomicValueWithNameInData("textId"), "someText");
		assertEquals(metadataGroup.getFirstAtomicValueWithNameInData("defTextId"), "someDefText");
	}

	@Test
	public void testCompleteLinkedTextsNoTextIdsExist() {
		MetadataCompleter metaCompleter = new MetadataCompleter();
		DataGroup metadataGroup = createItemWithNoTexts();
		metaCompleter.completeSpiderDataGroupWithLinkedTexts(metadataGroup, "textSystemOne");

		DataGroup textIdGroup = metadataGroup.getFirstGroupWithNameInData("textId");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"), "someIdText");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"textSystemOne");
		DataGroup defTextIdGroup = metadataGroup.getFirstGroupWithNameInData("defTextId");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someIdDefText");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"textSystemOne");
	}

	@Test
	public void testCompleteLinkedTextsTextIdAndDefTextIdExist() {
		MetadataCompleter metaCompleter = new MetadataCompleter();
		DataGroup metadataGroup = createItemWithNoTexts();
		addTexts(metadataGroup);

		metaCompleter.completeSpiderDataGroupWithLinkedTexts(metadataGroup, "testOtherText");

		DataGroup textIdGroup = metadataGroup.getFirstGroupWithNameInData("textId");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someExistingText");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"), "testText");
		DataGroup defTextIdGroup = metadataGroup.getFirstGroupWithNameInData("defTextId");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someExistingDefText");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"testText");
	}

	private void addTexts(DataGroup metadataGroup) {
		DataGroup textIdGroup = DataGroup.withNameInData("textId");
		textIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "testText"));
		textIdGroup
				.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "someExistingText"));
		metadataGroup.addChild(textIdGroup);

		DataGroup defTextIdGroup = DataGroup.withNameInData("defTextId");
		defTextIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "testText"));
		defTextIdGroup.addChild(
				DataAtomic.withNameInDataAndValue("linkedRecordId", "someExistingDefText"));
		metadataGroup.addChild(defTextIdGroup);
	}
}
