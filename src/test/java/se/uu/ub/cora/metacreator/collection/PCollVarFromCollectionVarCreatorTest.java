package se.uu.ub.cora.metacreator.collection;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class PCollVarFromCollectionVarCreatorTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String authToken;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		authToken = "testUser";
	}

	@Test
	public void testPCollVarsDoesNotExist() {
		SpiderDataGroup collectionVar = DataCreator
				.createCollectionVariableWithIdDataDividerAndNameInData("someTestCollectionVar",
						"testSystem", "some");

		PCollVarFromCollectionVarCreator creator = new PCollVarFromCollectionVarCreator();
		creator.useExtendedFunctionality(authToken, collectionVar);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 2);
		assertCorrectlyCreatedInputPCollVar();
		assertCorrectlyCreatedOutputPCollVar();
	}

	private void assertCorrectlyCreatedInputPCollVar() {
		SpiderRecordCreatorSpy spiderRecordCreatorSpy = instanceFactory.spiderRecordCreators.get(0);
		assertEquals(spiderRecordCreatorSpy.type, "presentationCollectionVar");
		SpiderDataGroup record = spiderRecordCreatorSpy.record;

		assertEquals(record.getNameInData(), "presentation");
		assertEquals(record.extractAtomicValue("mode"), "input");

		assertCorrectPresentationOf(record);
		assertCorrectRecordInfo(record, "someTestPCollVar");
		assertEquals(record.getAttributes().get("type"), "pCollVar");
	}

	private void assertCorrectPresentationOf(SpiderDataGroup record) {
		SpiderDataGroup presentationOf = record.extractGroup("presentationOf");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), "someTestCollectionVar");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordType"),
				"metadataCollectionVariable");
	}

	private void assertCorrectRecordInfo(SpiderDataGroup record, String expextedId) {
		SpiderDataGroup recordInfo = record.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), expextedId);
		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "testSystem");
	}

	private void assertCorrectlyCreatedOutputPCollVar() {
		SpiderRecordCreatorSpy spiderRecordCreatorSpy = instanceFactory.spiderRecordCreators.get(1);
		assertEquals(spiderRecordCreatorSpy.type, "presentationCollectionVar");
		SpiderDataGroup record = spiderRecordCreatorSpy.record;
		assertEquals(record.getNameInData(), "presentation");
		assertEquals(record.extractAtomicValue("mode"), "output");

		assertCorrectPresentationOf(record);
		assertCorrectRecordInfo(record, "someTestOutputPCollVar");
		assertEquals(record.getAttributes().get("type"), "pCollVar");
	}

	@Test
	public void testPCollVarsAlreadyExist() {
		SpiderDataGroup collectionVar = DataCreator
				.createCollectionVariableWithIdDataDividerAndNameInData("someExistingCollectionVar",
						"testSystem", "someExisting");

		PCollVarFromCollectionVarCreator creator = new PCollVarFromCollectionVarCreator();
		creator.useExtendedFunctionality(authToken, collectionVar);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
	}
}
