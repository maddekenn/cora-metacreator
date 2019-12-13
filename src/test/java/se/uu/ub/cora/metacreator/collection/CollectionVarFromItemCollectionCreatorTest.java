package se.uu.ub.cora.metacreator.collection;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class CollectionVarFromItemCollectionCreatorTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String authToken;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		authToken = "testUser";
	}

	@Test
	public void testCollectionDoesNotExist() {

		DataGroup itemCollection = DataCreator.createItemCollectionWithId("someCollection");
		CollectionVarFromItemCollectionCreator creator = new CollectionVarFromItemCollectionCreator();
		creator.useExtendedFunctionality(authToken, itemCollection);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 1);

		SpiderRecordCreatorSpy spiderRecordCreatorSpy = instanceFactory.spiderRecordCreators.get(0);
		assertEquals(spiderRecordCreatorSpy.type, "metadataCollectionVariable");

		DataGroup record = spiderRecordCreatorSpy.record;
		assertEquals(record.getNameInData(), "metadata");
		assertCorrectRecordInfo(record);
		assertCorrectRefCollection(record);
	}

	private void assertCorrectRecordInfo(DataGroup record) {
		DataGroup recordInfo = record.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "someCollectionVar");

		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId"), "test");
	}

	private void assertCorrectRefCollection(DataGroup record) {
		DataGroup refCollection = record.getFirstGroupWithNameInData("refCollection");
		assertEquals(refCollection.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"metadataItemCollection");
		assertEquals(refCollection.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someCollection");
	}

	@Test
	public void testCollectionAlreadyExist() {

		DataGroup itemCollection = DataCreator.createItemCollectionWithId("alreadyExistCollection");
		CollectionVarFromItemCollectionCreator creator = new CollectionVarFromItemCollectionCreator();
		creator.useExtendedFunctionality(authToken, itemCollection);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 0);

	}
}
