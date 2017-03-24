package se.uu.ub.cora.metacreator.collection;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

import static org.testng.Assert.assertEquals;

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
    public void testCollectionDoesNotExist(){

        SpiderDataGroup itemCollection = DataCreator.createItemCollectionWithId("someCollection");
        CollectionVarFromItemCollectionCreator creator = new CollectionVarFromItemCollectionCreator();
        creator.useExtendedFunctionality(authToken, itemCollection);

        assertEquals(instanceFactory.spiderRecordCreators.size(), 1);

        SpiderRecordCreatorSpy spiderRecordCreatorSpy = instanceFactory.spiderRecordCreators.get(0);
        assertEquals(spiderRecordCreatorSpy.type, "metadataItemCollection");

        SpiderDataGroup record = spiderRecordCreatorSpy.record;
        assertEquals(record.getNameInData(), "metadata");
        assertCorrectRecordInfo(record);
        assertCorrectRefCollection(record);
    }

    private void assertCorrectRecordInfo(SpiderDataGroup record) {
        SpiderDataGroup recordInfo = record.extractGroup("recordInfo");
        assertEquals(recordInfo.extractAtomicValue("id"), "someCollectionVar");

        SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
        assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "test");
    }

    private void assertCorrectRefCollection(SpiderDataGroup record) {
        SpiderDataGroup refCollection = record.extractGroup("refCollection");
        assertEquals(refCollection.extractAtomicValue("linkedRecordType"), "metadataItemCollection");
        assertEquals(refCollection.extractAtomicValue("linkedRecordId"), "someCollection");
    }

    private void assertCorrectTexts(SpiderDataGroup record) {
        assertEquals(record.extractAtomicValue("textId"), "someCollectionVarText");
        assertEquals(record.extractAtomicValue("defTextId"), "someCollectionVarDefText");
        assertEquals(record.extractAtomicValue("nameInData"), "someNameInData");
    }

    @Test
    public void testCollectionAlreadyExist(){

        SpiderDataGroup itemCollection = DataCreator.createItemCollectionWithId("alreadyExistCollection");
        CollectionVarFromItemCollectionCreator creator = new CollectionVarFromItemCollectionCreator();
        creator.useExtendedFunctionality(authToken, itemCollection);

        assertEquals(instanceFactory.spiderRecordCreators.size(), 0);

    }
}
