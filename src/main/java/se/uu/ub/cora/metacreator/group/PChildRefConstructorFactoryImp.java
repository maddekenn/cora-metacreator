/*
 * Copyright 2018, 2019 Uppsala University Library
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

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.spider.record.DataException;

public class PChildRefConstructorFactoryImp implements PChildRefConstructorFactory {

	@Override
	public PChildRefConstructor factor(DataGroup metadataChildReference, String mode) {
		String metadataRefId = getMetadataRefId(metadataChildReference);
		if (metadataChildIsTextVariable(metadataRefId)) {
			return PVarChildRefConstructor
					.usingMetadataChildReferenceAndMode(metadataChildReference, mode);
		} else if (metadataChildIsNumberVariable(metadataRefId)) {
			return PNumberVarChildRefConstructor
					.usingMetadataChildReferenceAndMode(metadataChildReference, mode);
		} else if (metadataChildIsCollectionVar(metadataRefId)) {
			return PCollVarChildRefConstructor
					.usingMetadataChildReferenceAndMode(metadataChildReference, mode);
		} else if (metadataChildIsResourceLink(metadataRefId)) {
			return PResLinkChildRefConstructor
					.usingMetadataChildReferenceAndMode(metadataChildReference, mode);
		} else if (metadataChildIsRecordLink(metadataRefId)) {
			return PLinkChildRefConstructor
					.usingMetadataChildReferenceAndMode(metadataChildReference, mode);
		} else if (metadataChildIsGroup(metadataRefId)) {
			return PGroupChildRefConstructor
					.usingMetadataChildReferenceAndMode(metadataChildReference, mode);
		}
		throw new DataException("Not possible to construct childReferenceId from metadataId");
	}

	private String getMetadataRefId(DataGroup metadataChildReference) {
		DataGroup metadataRef = metadataChildReference.getFirstGroupWithNameInData("ref");
		return metadataRef.getFirstAtomicValueWithNameInData("linkedRecordId");
	}

	private boolean metadataChildIsTextVariable(String metadataRefId) {
		return metadataRefId.endsWith("TextVar");
	}

	private boolean metadataChildIsNumberVariable(String metadataRefId) {
		return metadataRefId.endsWith("NumberVar");
	}

	private boolean metadataChildIsCollectionVar(String metadataRefId) {
		return metadataRefId.endsWith("CollectionVar");
	}

	private boolean metadataChildIsResourceLink(String metadataRefId) {
		return metadataRefId.endsWith("ResLink");
	}

	private boolean metadataChildIsRecordLink(String metadataRefId) {
		return metadataRefId.endsWith("Link");
	}

	private boolean metadataChildIsGroup(String metadataRefId) {
		return metadataRefId.endsWith("Group");
	}

}
