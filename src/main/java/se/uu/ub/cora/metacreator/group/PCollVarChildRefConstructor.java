package se.uu.ub.cora.metacreator.group;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PCollVarChildRefConstructor extends ChildRefConstructor {

	static final String PRESENTATION_COLLECTION_VAR = "presentationCollectionVar";

	private PCollVarChildRefConstructor(SpiderDataGroup metadataChildReference, String mode) {
		this.metadataChildReference = metadataChildReference;
		this.mode = mode;
	}

	public static ChildRefConstructor usingMetadataChildReferenceAndMode(
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
