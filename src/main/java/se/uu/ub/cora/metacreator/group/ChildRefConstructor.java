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
