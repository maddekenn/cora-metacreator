package se.uu.ub.cora.metacreator.group;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PNumberVarChildRefConstructor extends PChildRefConstructor {

	private static final String PRESENTATION_ID_ENDING = "PNumVar";
	private static final String METADATA_REF_ENDING = "NumberVar";
	private static final String PRESENTATION_RECORD_TYPE = "presentationNumberVar";

	private PNumberVarChildRefConstructor(SpiderDataGroup metadataChildReference, String mode) {
		this.metadataChildReference = metadataChildReference;
		this.mode = mode;
	}

	public static PChildRefConstructor usingMetadataChildReferenceAndMode(
			SpiderDataGroup metadataChildReference, String mode) {
		return new PNumberVarChildRefConstructor(metadataChildReference, mode);
	}

	@Override
	protected String getMetadataRefIdEnding() {
		return METADATA_REF_ENDING;
	}

	@Override
	protected String getPresentationIdEnding() {
		return PRESENTATION_ID_ENDING;
	}

	@Override
	protected String getPresentationRecordType() {
		return PRESENTATION_RECORD_TYPE;
	}

}
