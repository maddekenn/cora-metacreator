package se.uu.ub.cora.metacreator.collection;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class ItemCollectionCompleterTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String authToken;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		authToken = "testUser";
	}

	@Test
	public void testCollectionWithNoTexts() {
		ItemCollectionCompleter completer = ItemCollectionCompleter
				.forTextLinkedRecordType("textSystemOne");

		SpiderDataGroup itemCollection = DataCreator
				.createItemCollectionWithId("testItemCollection");

		completer.useExtendedFunctionality(authToken, itemCollection);
		SpiderDataGroup textIdGroup = itemCollection.extractGroup("textId");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordId"), "testItemCollectionText");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "textSystemOne");

		SpiderDataGroup defTextIdGroup = itemCollection.extractGroup("defTextId");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordId"),
				"testItemCollectionDefText");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "textSystemOne");
	}

	@Test
	public void testCollectionWithTexts() {
		ItemCollectionCompleter completer = ItemCollectionCompleter
				.forTextLinkedRecordType("textSystemOne");

		SpiderDataGroup itemCollection = DataCreator
				.createItemCollectionWithId("testItemCollection");
		DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(itemCollection,
				"textId", "textSystemOne", "anExistingText");
		DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(itemCollection,
				"defTextId", "textSystemOne", "anExistingDefText");

		completer.useExtendedFunctionality(authToken, itemCollection);
		SpiderDataGroup textIdGroup = itemCollection.extractGroup("textId");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordId"), "anExistingText");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "textSystemOne");

		SpiderDataGroup defTextIdGroup = itemCollection.extractGroup("defTextId");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordId"), "anExistingDefText");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "textSystemOne");
	}
}
