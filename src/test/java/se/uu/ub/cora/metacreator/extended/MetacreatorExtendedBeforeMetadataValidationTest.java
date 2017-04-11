package se.uu.ub.cora.metacreator.extended;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.metacreator.collection.ItemCollectionCompleter;
import se.uu.ub.cora.metacreator.collection.ItemCollectionCreator;
import se.uu.ub.cora.metacreator.collectionitem.CollectionItemCompleter;
import se.uu.ub.cora.metacreator.collectionitem.CollectionItemCreator;
import se.uu.ub.cora.metacreator.dependency.DependencyProviderSpy;
import se.uu.ub.cora.metacreator.recordtype.RecordTypeCreator;
import se.uu.ub.cora.metacreator.recordtype.RecordTypeMetaCompleter;
import se.uu.ub.cora.metacreator.search.SearchCompleter;
import se.uu.ub.cora.metacreator.search.SearchCreator;
import se.uu.ub.cora.metacreator.text.TextVarMetaCompleter;
import se.uu.ub.cora.spider.dependency.SpiderDependencyProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

import java.util.HashMap;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class MetacreatorExtendedBeforeMetadataValidationTest {
    private MetacreatorExtendedFunctionalityProvider functionalityProvider;

    @BeforeMethod
    public void setUp() {
        SpiderDependencyProvider dependencyProvider = new DependencyProviderSpy(new HashMap<>());
        functionalityProvider = new MetacreatorExtendedFunctionalityProvider(dependencyProvider);
    }

    @Test
    public void testGetFunctionalityForCreateBeforeMetadataValidation() {
        List<ExtendedFunctionality> functionalityForCreateBeforeMetadataValidation = functionalityProvider
                .getFunctionalityForCreateBeforeMetadataValidation("metadataTextVariable");
        assertEquals(functionalityForCreateBeforeMetadataValidation.size(), 1);
        assertTrue(functionalityForCreateBeforeMetadataValidation
                .get(0) instanceof TextVarMetaCompleter);
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
        assertTrue(functionalityForCreateBeforeMetadataValidation
                .get(0) instanceof SearchCompleter);
        assertTrue(functionalityForCreateBeforeMetadataValidation
                .get(1) instanceof SearchCreator);
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
        assertTrue(functionalityForCreateBeforeMetadataValidation
                .get(1) instanceof CollectionItemCreator);
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
}
