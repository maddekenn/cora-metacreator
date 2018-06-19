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

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public final class PCollVarChildRefConstructor extends PChildRefConstructor {

	static final String PRESENTATION_COLLECTION_VAR = "presentationCollectionVar";

	private PCollVarChildRefConstructor(SpiderDataGroup metadataChildReference, String mode) {
		this.metadataChildReference = metadataChildReference;
		this.mode = mode;
	}

	public static PChildRefConstructor usingMetadataChildReferenceAndMode(
			SpiderDataGroup metadataChildReference, String mode) {
		return new PCollVarChildRefConstructor(metadataChildReference, mode);
	}

	@Override
	protected String constructIdFromMetdataRefId(String metadataRefId) {
		String id = metadataRefId.substring(0, metadataRefId.indexOf("CollectionVar"));

		id += possibleOutputString();
		id += "PCollVar";
		return id;
	}

	@Override
	protected String getPresentationRecordType() {
		return PRESENTATION_COLLECTION_VAR;
	}

}
