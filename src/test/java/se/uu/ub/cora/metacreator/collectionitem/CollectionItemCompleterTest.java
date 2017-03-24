package se.uu.ub.cora.metacreator.collectionitem;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class CollectionItemCompleterTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String userId;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		userId = "testUser";
	}

	@Test
	public void testWithNoTexts() {
		// CollectionItemCompleter completer = CollectionItemCompleter
		// .forImplementingTextType("textSystemOne");
		CollectionItemCompleter completer = new CollectionItemCompleter();

		SpiderDataGroup item = DataCreator
				.createCollectionItemGroupWithIdAndTextIdAndDefTextId("firstItem", "", "");
		completer.useExtendedFunctionality(userId, item);

		assertEquals(item.extractAtomicValue("textId"), "firstItemText");
		assertEquals(item.extractAtomicValue("defTextId"), "firstItemDefText");

	}

	@Test
	public void testWithTextsInData() {
		// CollectionItemCompleter completer = CollectionItemCompleter
		// .forImplementingTextType("textSystemOne");

		CollectionItemCompleter completer = new CollectionItemCompleter();

		SpiderDataGroup item = DataCreator.createCollectionItemGroupWithIdAndTextIdAndDefTextId(
				"firstItem", "someTextId", "someDefTextId");
		completer.useExtendedFunctionality(userId, item);
		assertEquals(item.extractAtomicValue("textId"), "someTextId");
		assertEquals(item.extractAtomicValue("defTextId"), "someDefTextId");
	}
}
