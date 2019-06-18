/*
 * Copyright 2018 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.uu.ub.cora.metacreator.group;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.spider.record.DataException;

public class PChildRefConstructorFactoryTest {
	PChildRefConstructorFactory factory;
	String mode = "input";

	@BeforeMethod
	public void setUp() {
		mode = "input";
		factory = new PChildRefConstructorFactoryImp();
	}

	@Test
	public void testFactorPVarChildRefConstructor() {
		DataGroup metadataChildReference = DataCreatorForPresentationsConstructor
				.createMetadataChildRefWithIdAndRepeatId("identifierValueTextVar", "5");
		PChildRefConstructor constructor = factory.factor(metadataChildReference, mode);
		assertTrue(constructor instanceof PVarChildRefConstructor);
		assertEquals(constructor.getMode(), mode);
		assertEquals(constructor.getMetadataChildReference(), metadataChildReference);
	}

	@Test
	public void testFactorPCollVarChildRefConstructor() {

		DataGroup metadataChildReference = DataCreatorForPresentationsConstructor
				.createMetadataChildRefWithIdAndRepeatId("identifierTypeCollectionVar", "5");
		PChildRefConstructor constructor = factory.factor(metadataChildReference, "output");
		assertTrue(constructor instanceof PCollVarChildRefConstructor);
	}

	@Test
	public void testFactorPResLinkChildRefConstructor() {
		DataGroup metadataChildReference = DataCreatorForPresentationsConstructor
				.createMetadataChildRefWithIdAndRepeatId("identifierResLink", "5");
		PChildRefConstructor constructor = factory.factor(metadataChildReference, mode);
		assertTrue(constructor instanceof PResLinkChildRefConstructor);
		assertEquals(constructor.getMode(), mode);
		assertEquals(constructor.getMetadataChildReference(), metadataChildReference);
	}

	@Test
	public void testFactorPLinkChildRefConstructor() {
		DataGroup metadataChildReference = DataCreatorForPresentationsConstructor
				.createMetadataChildRefWithIdAndRepeatId("identifierLink", "5");
		PChildRefConstructor constructor = factory.factor(metadataChildReference, mode);
		assertTrue(constructor instanceof PLinkChildRefConstructor);
		assertEquals(constructor.getMode(), mode);
		assertEquals(constructor.getMetadataChildReference(), metadataChildReference);
	}

	@Test
	public void testFactorPGroupChildRefConstructor() {
		DataGroup metadataChildReference = DataCreatorForPresentationsConstructor
				.createMetadataChildRefWithIdAndRepeatId("identifierChildGroup", "5");
		PChildRefConstructor constructor = factory.factor(metadataChildReference, mode);
		assertTrue(constructor instanceof PGroupChildRefConstructor);
		assertEquals(constructor.getMode(), mode);
		assertEquals(constructor.getMetadataChildReference(), metadataChildReference);
	}

	@Test(expectedExceptions = DataException.class)
	public void testFactorUnknownChildRefConstructor() {
		DataGroup metadataChildReference = DataCreatorForPresentationsConstructor
				.createMetadataChildRefWithIdAndRepeatId("identifierChildNonExistingType", "5");
		factory.factor(metadataChildReference, mode);
	}

}
