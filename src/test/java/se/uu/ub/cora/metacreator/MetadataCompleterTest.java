package se.uu.ub.cora.metacreator;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class MetadataCompleterTest {

	@Test
	public void testCompleteTextsNoTextIdsExist() {
		MetadataCompleterImp metaCompleter = new MetadataCompleterImp();
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
		MetadataCompleterImp metaCompleter = new MetadataCompleterImp();
		SpiderDataGroup metadataGroup = createItemWithNoTexts();
		metadataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("textId", "someText"));

		metaCompleter.completeSpiderDataGroupWithTexts(metadataGroup);

		assertEquals(metadataGroup.extractAtomicValue("textId"), "someText");
		assertEquals(metadataGroup.extractAtomicValue("defTextId"), "someIdDefText");
	}

	@Test
	public void testCompleteTextsDefTextIdExists() {
		MetadataCompleterImp metaCompleter = new MetadataCompleterImp();
		SpiderDataGroup metadataGroup = createItemWithNoTexts();
		metadataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("defTextId", "someDefText"));

		metaCompleter.completeSpiderDataGroupWithTexts(metadataGroup);

		assertEquals(metadataGroup.extractAtomicValue("textId"), "someIdText");
		assertEquals(metadataGroup.extractAtomicValue("defTextId"), "someDefText");
	}

	@Test
	public void testCompleteTextsTextIdAndDefTextIdExist() {
		MetadataCompleterImp metaCompleter = new MetadataCompleterImp();
		SpiderDataGroup metadataGroup = createItemWithNoTexts();
		metadataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("textId", "someText"));
		metadataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("defTextId", "someDefText"));

		metaCompleter.completeSpiderDataGroupWithTexts(metadataGroup);

		assertEquals(metadataGroup.extractAtomicValue("textId"), "someText");
		assertEquals(metadataGroup.extractAtomicValue("defTextId"), "someDefText");
	}
}
