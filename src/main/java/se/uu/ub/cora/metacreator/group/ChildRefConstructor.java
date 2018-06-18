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

	public final PresentationChildReference getChildRef() {
		String id = getId();
		SpiderDataGroup ref = createRefUsingId(id);
		RecordIdentifier recordIdentifier = RecordIdentifier
				.usingTypeAndId(getPresentationRecordType(), id);
		return PresentationChildReference.usingRefGroupAndRecordIdentifier(ref, recordIdentifier);
	}

	private String getId() {
		String metadataRefId = getMetadataRefId(metadataChildReference);
		return constructIdFromMetdataRefId(metadataRefId);
	}

	private String getMetadataRefId(SpiderDataGroup metadataChildReference) {
		SpiderDataGroup metadataRef = metadataChildReference.extractGroup("ref");
		return metadataRef.extractAtomicValue("linkedRecordId");
	}

	protected abstract String constructIdFromMetdataRefId(String metadataRefId);

	private SpiderDataGroup createRefUsingId(String id) {
		SpiderDataGroup ref = SpiderDataGroup.withNameInData("ref");
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType",
				getPresentationRecordType()));
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", id));
		ref.addAttributeByIdWithValue("type", "presentation");
		return ref;
	}

	protected final String possibleOutputString() {
		if ("output".equals(mode)) {
			return "Output";
		}
		return "";
	}

	protected abstract String getPresentationRecordType();

	public String getMode() {
		// for test
		return mode;
	}

	public SpiderDataGroup getMetadataChildReference() {
		// for test
		return metadataChildReference;
	}

}
