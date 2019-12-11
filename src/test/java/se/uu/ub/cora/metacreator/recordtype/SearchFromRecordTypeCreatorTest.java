package se.uu.ub.cora.metacreator.recordtype;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class SearchFromRecordTypeCreatorTest {

	private SpiderInstanceFactorySpy instanceFactory;
	private String authToken;

	@BeforeMethod
	public void setup() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		authToken = "someToken";
	}

	@Test
	public void testUseExtendedFunctionality() {
		SearchFromRecordTypeCreator searchCreator = new SearchFromRecordTypeCreator();

		DataGroup recordType = DataCreator
				.createDataGroupForRecordTypeWithId("myRecordType");
		DataCreator.addAllValuesToDataGroup(recordType, "myRecordType");

		searchCreator.useExtendedFunctionality(authToken, recordType);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 1);
	}

	@Test
	public void testUseExtendedFunctionalitySearchAlreadyExists() {
		SearchFromRecordTypeCreator searchCreator = new SearchFromRecordTypeCreator();

		DataGroup recordType = DataCreator
				.createDataGroupForRecordTypeWithId("myRecordTypeExists");
		DataCreator.addAllValuesToDataGroup(recordType, "myRecordTypeExists");

		searchCreator.useExtendedFunctionality(authToken, recordType);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
	}
}
