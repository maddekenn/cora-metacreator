package se.uu.ub.cora.metacreator;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class MetadataCompleterTest {

	@Test
	public void testCompleteTextsNoTextIdsExist() {
		MetadataCompleter metaCompleter = new MetadataCompleter();
		SpiderDataGroup metadataGroup = createItemWithNoTexts();
		metaCompleter.completeSpiderDataGroupWithTexts(metadataGroup);

		assertEquals(metadataGroup.extractAtomicValue("textId"), "someIdText");
		assertEquals(metadataGroup.extractAtomicValue("defTextId"), "someIdDefText");
	}

	private SpiderDataGroup createItemWithNoTexts() {
		SpiderDataGroup metadataGroup = SpiderDataGroup.withNameInData("metadata");
		SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
		recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", "someId"));
		metadataGroup.addChild(recordInfo);
		return metadataGroup;
	}

	@Test
	public void testCompleteTextsTextIdExists() {
		MetadataCompleter metaCompleter = new MetadataCompleter();
		SpiderDataGroup metadataGroup = createItemWithNoTexts();
		metadataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("textId", "someText"));

		metaCompleter.completeSpiderDataGroupWithTexts(metadataGroup);

		assertEquals(metadataGroup.extractAtomicValue("textId"), "someText");
		assertEquals(metadataGroup.extractAtomicValue("defTextId"), "someIdDefText");
	}

	@Test
	public void testCompleteTextsDefTextIdExists() {
		MetadataCompleter metaCompleter = new MetadataCompleter();
		SpiderDataGroup metadataGroup = createItemWithNoTexts();
		metadataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("defTextId", "someDefText"));

		metaCompleter.completeSpiderDataGroupWithTexts(metadataGroup);

		assertEquals(metadataGroup.extractAtomicValue("textId"), "someIdText");
		assertEquals(metadataGroup.extractAtomicValue("defTextId"), "someDefText");
	}

	@Test
	public void testCompleteTextsTextIdAndDefTextIdExist() {
		MetadataCompleter metaCompleter = new MetadataCompleter();
		SpiderDataGroup metadataGroup = createItemWithNoTexts();
		metadataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("textId", "someText"));
		metadataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("defTextId", "someDefText"));

		metaCompleter.completeSpiderDataGroupWithTexts(metadataGroup);

		assertEquals(metadataGroup.extractAtomicValue("textId"), "someText");
		assertEquals(metadataGroup.extractAtomicValue("defTextId"), "someDefText");
	}

	@Test
	public void testCompleteLinkedTextsNoTextIdsExist() {
		MetadataCompleter metaCompleter = new MetadataCompleter();
		SpiderDataGroup metadataGroup = createItemWithNoTexts();
		metaCompleter.completeSpiderDataGroupWithLinkedTexts(metadataGroup);

		SpiderDataGroup textIdGroup = metadataGroup.extractGroup("textId");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordId"), "someIdText");
		SpiderDataGroup defTextIdGroup = metadataGroup.extractGroup("defTextId");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordId"), "someIdDefText");
	}

	@Test
	public void testCompleteLinkedTextsTextIdAndDefTextIdExist() {
		MetadataCompleter metaCompleter = new MetadataCompleter();
		SpiderDataGroup metadataGroup = createItemWithNoTexts();
		addTexts(metadataGroup);

		metaCompleter.completeSpiderDataGroupWithLinkedTexts(metadataGroup);

		SpiderDataGroup textIdGroup = metadataGroup.extractGroup("textId");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordId"), "someExistingText");
		SpiderDataGroup defTextIdGroup = metadataGroup.extractGroup("defTextId");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordId"), "someExistingDefText");
	}

	private void addTexts(SpiderDataGroup metadataGroup) {
		SpiderDataGroup textIdGroup = SpiderDataGroup.withNameInData("textId");
		textIdGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "text"));
		textIdGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", "someExistingText"));
		metadataGroup.addChild(textIdGroup);

		SpiderDataGroup defTextIdGroup = SpiderDataGroup.withNameInData("defTextId");
		defTextIdGroup
				.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "text"));
		defTextIdGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", "someExistingDefText"));
		metadataGroup.addChild(defTextIdGroup);
	}
}
