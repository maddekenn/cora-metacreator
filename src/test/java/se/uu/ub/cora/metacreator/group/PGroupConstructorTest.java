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

    private void assertCorrectChildReferences(SpiderDataGroup pGroup) {
        SpiderDataGroup childReferences = pGroup.extractGroup("childReferences");
        assertCorrectChildInListByIndexWithRepeatIdAndId(childReferences, 0, "0", "identifierTypePCollVar");

    }

    private void assertCorrectChildInListByIndexWithRepeatIdAndId(SpiderDataGroup childReferences, int index, String repeatId, String id) {
        SpiderDataGroup firstChild = (SpiderDataGroup)childReferences.getChildren().get(index);
        assertEquals(firstChild.getNameInData(), "childReference");
        assertEquals(firstChild.getRepeatId(), repeatId);

        SpiderDataGroup firstChildRef = firstChild.extractGroup("ref");
        assertEquals(firstChildRef.extractAtomicValue("linkedRecordType"), "presentation");
        assertEquals(firstChildRef.extractAtomicValue("linkedRecordId"), id);
    }

    private List<SpiderDataGroup> createChildren(){
        List<SpiderDataGroup> childReferences = new ArrayList();

        SpiderDataGroup childRef1 = SpiderDataGroup.withNameInData("childReference");
        childRef1.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMin", "1"));
        childRef1.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMax", "1"));
        childRef1.setRepeatId("0");

        SpiderDataGroup ref1 = SpiderDataGroup.withNameInData("ref");
        ref1.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "metadata"));
        ref1.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", "identifierTypeCollectionVar"));
        childRef1.addChild(ref1);

        childReferences.add(childRef1);
        return childReferences;
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
