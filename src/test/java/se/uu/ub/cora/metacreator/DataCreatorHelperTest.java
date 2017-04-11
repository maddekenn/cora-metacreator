package se.uu.ub.cora.metacreator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class DataCreatorHelperTest {

	@Test
	public void testPrivateConstructor() throws Exception {
		Constructor<DataCreatorHelper> constructor = DataCreatorHelper.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
	}

	@Test(expectedExceptions = InvocationTargetException.class)
	public void testPrivateConstructorInvoke() throws Exception {
		Constructor<DataCreatorHelper> constructor = DataCreatorHelper.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testCreateRecordInfo() {
		SpiderDataGroup recordInfo = DataCreatorHelper
				.createRecordInfoWithIdAndDataDivider("someId", "test");
		assertEquals(recordInfo.extractAtomicValue("id"), "someId");
		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordType"), "system");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "test");
	}

	@Test
	public void testExtractDataDivider() {
		SpiderDataGroup mainDataGroup = DataCreator.createTextVarGroupWithIdAndTextIdAndDefTextId(
				"someId", "someTextId", "someDefTextId");
		String dataDivider = DataCreatorHelper.extractDataDividerStringFromDataGroup(mainDataGroup);
		assertEquals(dataDivider, "cora");
	}

	@Test
	public void testExtractId() {
		SpiderDataGroup mainDataGroup = DataCreator.createTextVarGroupWithIdAndTextIdAndDefTextId(
				"someId", "someTextId", "someDefTextId");
		String id = DataCreatorHelper.extractIdFromDataGroup(mainDataGroup);
		assertEquals(id, "someId");
	}

}
