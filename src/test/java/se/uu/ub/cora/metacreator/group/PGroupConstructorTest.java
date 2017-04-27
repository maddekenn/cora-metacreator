package se.uu.ub.cora.metacreator.group;

import org.testng.annotations.Test;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class PGroupConstructorTest {

    @Test
    public void testConstructor(){
        PGroupConstructor constructor = new PGroupConstructor();
        List<SpiderDataGroup> childReferences = createChildren();
        SpiderDataGroup pGroup = constructor.constructPGroupWithIdDataDividerPresentationOfAndChildren("someTestPGroup",
                "testSystem", "someTestGroup", childReferences);
        assertEquals(pGroup.getAttributes().get("type"), "pGroup");
        assertCorrectRecordInfo(pGroup);
        assertCorrectPresentationOf(pGroup);

        assertCorrectChildReferences(pGroup);
    }

    private List<SpiderDataGroup> createChildren(){
        List<SpiderDataGroup> childReferences = new ArrayList();

        SpiderDataGroup childRef = createChildRefWithIdAndRepeatId("identifierTypeCollectionVar", "0");
        childReferences.add(childRef);

        SpiderDataGroup childRef2 = createChildRefWithIdAndRepeatId("identifierValueTextVar", "1");
        childReferences.add(childRef2);

        SpiderDataGroup childRef3 = createChildRefWithIdAndRepeatId("identifierResourceResLink", "2");
        childReferences.add(childRef3);

        SpiderDataGroup childRef4 = createChildRefWithIdAndRepeatId("identifierLink", "3");
        childReferences.add(childRef4);

        SpiderDataGroup childRef5 = createChildRefWithIdAndRepeatId("identifierChildGroup", "4");
        childReferences.add(childRef5);
        return childReferences;
    }

    private SpiderDataGroup createChildRefWithIdAndRepeatId(String childRefId, String repeatId) {
        SpiderDataGroup childRef = SpiderDataGroup.withNameInData("childReference");
        childRef.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMin", "1"));
        childRef.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMax", "1"));
        childRef.setRepeatId(repeatId);

        SpiderDataGroup ref = SpiderDataGroup.withNameInData("ref");
        ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "metadata"));
        ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", childRefId));
        childRef.addChild(ref);
        return childRef;
    }

    private void assertCorrectChildReferences(SpiderDataGroup pGroup) {
        SpiderDataGroup childReferences = pGroup.extractGroup("childReferences");
        assertCorrectChildInListByIndexWithRepeatIdAndId(childReferences, 0, "0", "identifierTypePCollVar");
        assertCorrectChildInListByIndexWithRepeatIdAndId(childReferences, 1, "1", "identifierValuePVar");
        assertCorrectChildInListByIndexWithRepeatIdAndId(childReferences, 2, "2", "identifierResourcePResLink");
        assertCorrectChildInListByIndexWithRepeatIdAndId(childReferences, 3, "3", "identifierPLink");
        assertCorrectChildInListByIndexWithRepeatIdAndId(childReferences, 4, "4", "identifierChildPGroup");

    }

    private void assertCorrectChildInListByIndexWithRepeatIdAndId(SpiderDataGroup childReferences, int index, String repeatId, String id) {
        SpiderDataGroup firstChild = (SpiderDataGroup)childReferences.getChildren().get(index);
        assertEquals(firstChild.getNameInData(), "childReference");
        assertEquals(firstChild.getRepeatId(), repeatId);

        SpiderDataGroup firstChildRef = firstChild.extractGroup("ref");
        assertEquals(firstChildRef.extractAtomicValue("linkedRecordType"), "presentation");
        assertEquals(firstChildRef.extractAtomicValue("linkedRecordId"), id);
    }

    private void assertCorrectRecordInfo(SpiderDataGroup pGroup) {
        SpiderDataGroup recordInfo = pGroup.extractGroup("recordInfo");
        assertEquals(recordInfo.extractAtomicValue("id"), "someTestPGroup");

        SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
        assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "testSystem");
    }

    private void assertCorrectPresentationOf(SpiderDataGroup pGroup) {
        SpiderDataGroup presentationOf = pGroup.extractGroup("presentationOf");
        assertEquals(presentationOf.extractAtomicValue("linkedRecordType"), "metadataGroup");
        assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), "someTestGroup");
    }

}
