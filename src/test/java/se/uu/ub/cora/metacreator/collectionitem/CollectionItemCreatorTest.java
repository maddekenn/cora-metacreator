package se.uu.ub.cora.metacreator.collectionitem;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class CollectionItemCreatorTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String authToken;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		authToken = "testUser";
	}

	@Test
	public void testNonExistingTexts() {
		CollectionItemCreator creator = CollectionItemCreator
				.forImplementingTextType("textSystemOne");
		SpiderDataGroup item = DataCreator.createCollectionItemGroupWithIdAndTextIdAndDefTextId(
				"firstItem", "nonExistingText", "nonExistingDefText");

		creator.useExtendedFunctionality(authToken, item);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 2);
		assertCorrectTextCreatedWithUserIdAndTypeAndId(0, "nonExistingText");
		assertCorrectTextCreatedWithUserIdAndTypeAndId(1, "nonExistingDefText");
	}

	private void assertCorrectTextCreatedWithUserIdAndTypeAndId(int createdTextNo,
			String createdIdForText) {
		SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
				.get(createdTextNo);
		assertEquals(spiderRecordCreator.userId, authToken);
		assertEquals(spiderRecordCreator.type, "textSystemOne");
		SpiderDataGroup createdTextRecord = spiderRecordCreator.record;
		SpiderDataGroup recordInfo = createdTextRecord.extractGroup("recordInfo");
		String id = recordInfo.extractAtomicValue("id");
		assertEquals(id, createdIdForText);
	}

	@Test
	public void testWithExistingTextsInStorage() {
		CollectionItemCreator creator = CollectionItemCreator
				.forImplementingTextType("textSystemOne");

		SpiderDataGroup item = DataCreator.createCollectionItemGroupWithIdAndTextIdAndDefTextId(
				"firstItem", "someExistingTextId", "someExistingDefTextId");
		creator.useExtendedFunctionality(authToken, item);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
	}
}
