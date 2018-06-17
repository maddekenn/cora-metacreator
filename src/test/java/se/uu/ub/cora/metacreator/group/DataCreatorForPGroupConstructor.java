package se.uu.ub.cora.metacreator.group;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class DataCreatorForPGroupConstructor {

	static List<SpiderDataElement> createChildren() {
		List<SpiderDataElement> childReferences = new ArrayList<>();

		SpiderDataGroup childRef = createMetadataChildRefWithIdAndRepeatId(
				"identifierTypeCollectionVar", "0");
		childReferences.add(childRef);

		SpiderDataGroup childRef2 = createMetadataChildRefWithIdAndRepeatId(
				"identifierValueTextVar", "1");
		childReferences.add(childRef2);

		SpiderDataGroup childRef3 = createMetadataChildRefWithIdAndRepeatId("identifierResLink",
				"2");
		childReferences.add(childRef3);

		SpiderDataGroup childRef4 = createMetadataChildRefWithIdAndRepeatId("identifierLink", "3");
		childReferences.add(childRef4);

		SpiderDataGroup childRef5 = createMetadataChildRefWithIdAndRepeatId("identifierChildGroup",
				"4");
		childReferences.add(childRef5);
		SpiderDataGroup childRef6 = createMetadataChildRefWithIdAndRepeatId(
				"identifierChildGroupWithUnclearEnding", "5");
		childReferences.add(childRef6);
		SpiderDataGroup childRef7 = createMetadataChildRefWithIdAndRepeatId(
				"identifierChildHasNoPresentationTextVar", "6");
		childReferences.add(childRef7);
		return childReferences;
	}

	static SpiderDataGroup createMetadataChildRefWithIdAndRepeatId(String childRefId,
			String repeatId) {
		SpiderDataGroup childRef = SpiderDataGroup.withNameInData("childReference");
		childRef.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMin", "1"));
		childRef.addChild(SpiderDataAtomic.withNameInDataAndValue("repeatMax", "1"));
		childRef.setRepeatId(repeatId);

		SpiderDataGroup ref = SpiderDataGroup.withNameInData("ref");
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "metadata"));
		ref.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", childRefId));
		childRef.addChild(ref);
		return childRef;
	}

}
