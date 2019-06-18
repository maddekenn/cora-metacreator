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

import se.uu.ub.cora.data.DataGroup;

public final class PLinkChildRefConstructor extends PChildRefConstructor {
	static final String PRESENTATION_RECORD_LINK = "presentationRecordLink";
	private static final String LINK = "Link";
	private static final String PLINK = "PLink";

	private PLinkChildRefConstructor(DataGroup metadataChildReference, String mode) {
		this.metadataChildReference = metadataChildReference;
		this.mode = mode;
	}

	public static PChildRefConstructor usingMetadataChildReferenceAndMode(
			DataGroup metadataChildReference, String mode) {
		return new PLinkChildRefConstructor(metadataChildReference, mode);
	}

	@Override
	protected String getMetadataRefIdEnding() {
		return LINK;
	}

	@Override
	protected String getPresentationIdEnding() {
		return PLINK;
	}

	@Override
	protected String getPresentationRecordType() {
		return PRESENTATION_RECORD_LINK;
	}
}
