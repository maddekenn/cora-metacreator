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

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.spider.record.DataException;

public class PChildRefConstructorFactorySpy implements PChildRefConstructorFactory {

	public List<PChildRefConstructor> factored = new ArrayList<>();
	public String mode="";

	@Override
	public PChildRefConstructor factor(DataGroup metadataChildReference, String mode) {
		this.mode = mode;
		PChildRefConstructor pChildRefConstructor = new PChildRefConstructorSpy(
				metadataChildReference, mode);
		DataGroup ref = metadataChildReference.getFirstGroupWithNameInData("ref");
        if("identifierChildGroupWithUnclearEnding".equals(ref.getFirstAtomicValueWithNameInData("linkedRecordId"))){
			throw new DataException("Not possible to construct childReferenceId from metadataId");
		}
		factored.add(pChildRefConstructor);
		return pChildRefConstructor;
	}

}
