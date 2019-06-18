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

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataElement;
import se.uu.ub.cora.data.DataGroup;

public class DataCreatorForPresentationsConstructor {

	static List<DataElement> createChildren() {
		List<DataElement> childReferences = new ArrayList<>();

		DataGroup childRef = createMetadataChildRefWithIdAndRepeatId("identifierTypeCollectionVar",
				"0");
		childReferences.add(childRef);

		DataGroup childRef2 = createMetadataChildRefWithIdAndRepeatId("identifierValueTextVar",
				"1");
		childReferences.add(childRef2);

		DataGroup childRef3 = createMetadataChildRefWithIdAndRepeatId("identifierResLink", "2");
		childReferences.add(childRef3);

		DataGroup childRef4 = createMetadataChildRefWithIdAndRepeatId("identifierLink", "3");
		childReferences.add(childRef4);

		DataGroup childRef5 = createMetadataChildRefWithIdAndRepeatId("identifierChildGroup", "4");
		childReferences.add(childRef5);
		DataGroup childRef6 = createMetadataChildRefWithIdAndRepeatId(
				"identifierChildGroupWithUnclearEnding", "5");
		childReferences.add(childRef6);
		DataGroup childRef7 = createMetadataChildRefWithIdAndRepeatId(
				"identifierChildHasNoPresentationTextVar", "6");
		childReferences.add(childRef7);
		return childReferences;
	}

	static DataGroup createMetadataChildRefWithIdAndRepeatId(String childRefId, String repeatId) {
		DataGroup childRef = DataGroup.withNameInData("childReference");
		childRef.addChild(DataAtomic.withNameInDataAndValue("repeatMin", "1"));
		childRef.addChild(DataAtomic.withNameInDataAndValue("repeatMax", "1"));
		childRef.setRepeatId(repeatId);

		DataGroup ref = DataGroup.withNameInData("ref");
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "metadata"));
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", childRefId));
		childRef.addChild(ref);
		return childRef;
	}

}
