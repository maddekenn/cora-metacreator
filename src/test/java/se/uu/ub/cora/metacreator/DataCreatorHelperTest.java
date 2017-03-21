package se.uu.ub.cora.metacreator;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class DataCreatorHelperTest {


    @Test
    public void testCreateRecordInfo(){
        SpiderDataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider("someId", "test");
        assertEquals(recordInfo.extractAtomicValue("id"), "someId");
        SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
        assertEquals(dataDivider.extractAtomicValue("linkedRecordType"), "system");
        assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "test");
    }

    @Test
    public void testExtractDataDivider(){
        SpiderDataGroup mainDataGroup = DataCreator.createTextVarGroupWithIdAndTextIdAndDefTextId("someId", "someTextId", "someDefTextId");
        String dataDivider = DataCreatorHelper.extractDataDividerFromDataGroup(mainDataGroup);
        assertEquals(dataDivider, "cora");

    }
}
