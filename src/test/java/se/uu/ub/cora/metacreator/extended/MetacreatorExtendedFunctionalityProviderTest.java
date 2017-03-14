/*
 * Copyright 2016 Olov McKie
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

package se.uu.ub.cora.metacreator.extended;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.DependencyProviderSpy;
import se.uu.ub.cora.metacreator.recordtype.RecordTypeMetaCompleter;
import se.uu.ub.cora.metacreator.text.PVarFromTextVarCreator;
import se.uu.ub.cora.metacreator.text.TextVarMetaCompleter;
import se.uu.ub.cora.spider.dependency.SpiderDependencyProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class MetacreatorExtendedFunctionalityProviderTest {
	private MetacreatorExtendedFunctionalityProvider functionalityProvider;

	@BeforeMethod
	public void setUp() {
		SpiderDependencyProvider dependencyProvider = new DependencyProviderSpy(
				new HashMap<>());
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
	public void testGetFunctionalityForCreateBeforeReturn() {
		List<ExtendedFunctionality> functionalityForCreateBeforeReturn = functionalityProvider
				.getFunctionalityForCreateBeforeReturn("metadataTextVariable");
		assertEquals(functionalityForCreateBeforeReturn.size(), 1);
		assertTrue(functionalityForCreateBeforeReturn.get(0) instanceof PVarFromTextVarCreator);
	}

	@Test
	public void testGetFunctionalityForCreateBeforeReturnNot() {
		List<ExtendedFunctionality> functionalityForCreateBeforeReturn = functionalityProvider
				.getFunctionalityForCreateBeforeReturn("metadataTextVariableNOT");
		assertEquals(functionalityForCreateBeforeReturn.size(), 0);
	}

	@Test
	public void testEnsureListIsRealList() {
		assertTrue(functionalityProvider
				.ensureListIsRealList(Collections.emptyList()) instanceof ArrayList);
		List<ExtendedFunctionality> list = new ArrayList<>();
		list.add(null);
		assertEquals(functionalityProvider.ensureListIsRealList(list), list);
	}

	@Test
	public void testGetFunctionalityForCreateBeforeMetadataValidationForRecordType() {
		List<ExtendedFunctionality> functionalityForCreateBeforeMetadataValidation = functionalityProvider
				.getFunctionalityForCreateBeforeMetadataValidation("recordType");
		assertEquals(functionalityForCreateBeforeMetadataValidation.size(), 1);
		assertTrue(functionalityForCreateBeforeMetadataValidation
				.get(0) instanceof RecordTypeMetaCompleter);
	}
}
