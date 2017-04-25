package se.uu.ub.cora.metacreator.extended;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.collection.CollectionVariableCompleter;
import se.uu.ub.cora.metacreator.collection.ItemCollectionCompleter;
import se.uu.ub.cora.metacreator.collection.ItemCollectionCreator;
import se.uu.ub.cora.metacreator.collectionitem.CollectionItemCompleter;
import se.uu.ub.cora.metacreator.TextCreator;
import se.uu.ub.cora.metacreator.dependency.DependencyProviderSpy;
import se.uu.ub.cora.metacreator.group.GroupCompleter;
import se.uu.ub.cora.metacreator.recordlink.RecordLinkCompleter;
import se.uu.ub.cora.metacreator.recordtype.RecordTypeCreator;
import se.uu.ub.cora.metacreator.recordtype.RecordTypeMetaCompleter;
import se.uu.ub.cora.metacreator.search.SearchCompleter;
import se.uu.ub.cora.metacreator.search.SearchCreator;
import se.uu.ub.cora.metacreator.textvar.TextVarCompleter;
import se.uu.ub.cora.spider.dependency.SpiderDependencyProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class MetacreatorExtendedBeforeMetadataValidationTest {
	private MetacreatorExtendedFunctionalityProvider functionalityProvider;

	@BeforeMethod
	public void setUp() {
		SpiderDependencyProvider dependencyProvider = new DependencyProviderSpy(new HashMap<>());
		functionalityProvider = new MetacreatorExtendedFunctionalityProvider(dependencyProvider);
	}

	@Test
	public void testGetFunctionalityForCreateBeforeMetadataValidationForTextVariable() {
		List<ExtendedFunctionality> functionalityForCreateBeforeMetadataValidation = functionalityProvider
				.getFunctionalityForCreateBeforeMetadataValidation("metadataTextVariable");
		assertEquals(functionalityForCreateBeforeMetadataValidation.size(), 2);
		assertTrue(functionalityForCreateBeforeMetadataValidation
				.get(0) instanceof TextVarCompleter);
		assertTrue(functionalityForCreateBeforeMetadataValidation.get(1) instanceof TextCreator);
	}

	@Test
	public void testGetFunctionalityForCreateBeforeMetadataValidationNot() {
		List<ExtendedFunctionality> functionalityForCreateBeforeMetadataValidation = functionalityProvider
				.getFunctionalityForCreateBeforeMetadataValidation("metadataTextVariableNOT");
		assertEquals(functionalityForCreateBeforeMetadataValidation.size(), 0);
	}

	@Test
	public void testGetFunctionalityForCreateBeforeMetadataValidationForSearch() {
		List<ExtendedFunctionality> functionalityForCreateBeforeMetadataValidation = functionalityProvider
				.getFunctionalityForCreateBeforeMetadataValidation("search");
		assertEquals(functionalityForCreateBeforeMetadataValidation.size(), 2);
		assertTrue(
				functionalityForCreateBeforeMetadataValidation.get(0) instanceof SearchCompleter);
		assertTrue(functionalityForCreateBeforeMetadataValidation.get(1) instanceof SearchCreator);
	}

	@Test
	public void testGetFunctionalityForCreateBeforeMetadataValidationForRecordType() {
		List<ExtendedFunctionality> functionalityForCreateBeforeMetadataValidation = functionalityProvider
				.getFunctionalityForCreateBeforeMetadataValidation("recordType");
		assertEquals(functionalityForCreateBeforeMetadataValidation.size(), 2);
		assertTrue(functionalityForCreateBeforeMetadataValidation
				.get(0) instanceof RecordTypeMetaCompleter);
		assertTrue(
				functionalityForCreateBeforeMetadataValidation.get(1) instanceof RecordTypeCreator);
	}

	@Test
	public void testGetFunctionalityForCreateBeforeMetadataValidationForCollectionItem() {
		List<ExtendedFunctionality> functionalityForCreateBeforeMetadataValidation = functionalityProvider
				.getFunctionalityForCreateBeforeMetadataValidation("metadataCollectionItem");
		assertEquals(functionalityForCreateBeforeMetadataValidation.size(), 2);
		assertTrue(functionalityForCreateBeforeMetadataValidation
				.get(0) instanceof CollectionItemCompleter);
		assertTrue(functionalityForCreateBeforeMetadataValidation.get(1) instanceof TextCreator);
	}

	@Test
	public void testGetFunctionalityForCreateBeforeMetadataValidationForItemCollection() {
		List<ExtendedFunctionality> functionalityForCreateBeforeMetadataValidation = functionalityProvider
				.getFunctionalityForCreateBeforeMetadataValidation("metadataItemCollection");

		assertEquals(functionalityForCreateBeforeMetadataValidation.size(), 2);
		assertTrue(functionalityForCreateBeforeMetadataValidation
				.get(0) instanceof ItemCollectionCompleter);

		assertTrue(functionalityForCreateBeforeMetadataValidation
				.get(1) instanceof ItemCollectionCreator);
	}

	@Test
	public void testGetFunctionalityForCreateBeforeMetadataValidationForMetadataGroup() {
		List<ExtendedFunctionality> functionalityForCreateBeforeMetadataValidation = functionalityProvider
				.getFunctionalityForCreateBeforeMetadataValidation("metadataGroup");
		assertEquals(functionalityForCreateBeforeMetadataValidation.size(), 2);
		assertTrue(functionalityForCreateBeforeMetadataValidation.get(0) instanceof GroupCompleter);
		assertTrue(functionalityForCreateBeforeMetadataValidation.get(1) instanceof TextCreator);
	}

	@Test
	public void testGetFunctionalityForCreateBeforeMetadataValidationForMetadataRecordLink() {
		List<ExtendedFunctionality> functionalityForCreateBeforeMetadataValidation = functionalityProvider
				.getFunctionalityForCreateBeforeMetadataValidation("metadataRecordLink");
		assertEquals(functionalityForCreateBeforeMetadataValidation.size(), 2);
		assertTrue(functionalityForCreateBeforeMetadataValidation
				.get(0) instanceof RecordLinkCompleter);
		assertTrue(functionalityForCreateBeforeMetadataValidation.get(1) instanceof TextCreator);
	}

	@Test
	public void testGetFunctionalityForCreateBeforeMetadataValidationForCollectionVariable() {
		List<ExtendedFunctionality> functionalityForCreateBeforeMetadataValidation = functionalityProvider
				.getFunctionalityForCreateBeforeMetadataValidation("metadataCollectionVariable");
		assertEquals(functionalityForCreateBeforeMetadataValidation.size(), 2);
		assertTrue(functionalityForCreateBeforeMetadataValidation
				.get(0) instanceof CollectionVariableCompleter);
		assertTrue(functionalityForCreateBeforeMetadataValidation.get(1) instanceof TextCreator);
	}
}
