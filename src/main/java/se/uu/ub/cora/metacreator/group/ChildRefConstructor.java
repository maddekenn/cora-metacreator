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

import se.uu.ub.cora.metacreator.PresentationChildReference;
import se.uu.ub.cora.metacreator.RecordIdentifier;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public abstract class ChildRefConstructor {

	protected SpiderDataGroup metadataChildReference;
	protected String mode;

	protected abstract String constructIdFromMetdataRefId(String metadataRefId);

	public PresentationChildReference getChildRef() {
		String metadataRefId = getMetadataRefId(metadataChildReference);
		String id = constructIdFromMetdataRefId(metadataRefId);

		SpiderDataGroup ref = SpiderDataGroup.withNameInData("ref");
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType",
				getPresentationRecordType()));
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", id));
		ref.addAttributeByIdWithValue("type", "presentation");

		return PresentationChildReference.usingRefGroupAndRecordIdentifier(ref,
				RecordIdentifier.usingTypeAndId(getPresentationRecordType(), id));
	}

	private String getMetadataRefId(SpiderDataGroup metadataChildReference) {
		SpiderDataGroup metadataRef = metadataChildReference.extractGroup("ref");
		return metadataRef.extractAtomicValue("linkedRecordId");
	}

	protected String possibleOutputString() {
		if ("output".equals(mode)) {
			return "Output";
		}
		return "";
	}

	protected abstract String getPresentationRecordType();

}
