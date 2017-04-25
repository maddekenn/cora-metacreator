package se.uu.ub.cora.metacreator.collection;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class CollectionVariableCompleterTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String authToken;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		authToken = "testUser";
	}

	@Test
	public void testCollectionVariableWithNoTexts() {
		CollectionVariableCompleter completer = CollectionVariableCompleter
				.forTextLinkedRecordType("someLinkedRecordType");

		SpiderDataGroup collectionVariable = DataCreator
				.createCollectionVariableWithIdDataDividerAndNameInData("someCollectionVar",
						"testSystem", "someType");
		collectionVariable.removeChild("textId");
		collectionVariable.removeChild("defTextId");

		completer.useExtendedFunctionality(authToken, collectionVariable);

		SpiderDataGroup textIdGroup = collectionVariable.extractGroup("textId");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordId"), "someCollectionVarText");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "someLinkedRecordType");

		SpiderDataGroup defTextIdGroup = collectionVariable.extractGroup("defTextId");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordId"),
				"someCollectionVarDefText");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "someLinkedRecordType");
	}
}
