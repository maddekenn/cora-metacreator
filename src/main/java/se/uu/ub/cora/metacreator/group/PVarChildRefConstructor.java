package se.uu.ub.cora.metacreator.group;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PVarChildRefConstructor extends ChildRefConstructor {
	static final String PRESENTATION_VAR = "presentationVar";

	private PVarChildRefConstructor(SpiderDataGroup metadataChildReference, String mode) {
		this.metadataChildReference = metadataChildReference;
		this.mode = mode;
	}

	public static ChildRefConstructor usingMetadataChildReferenceAndMode(
			SpiderDataGroup metadataChildReference, String mode) {
		return new PVarChildRefConstructor(metadataChildReference, mode);
	}

	@Override
	protected String constructIdFromMetdataRefId(String metadataRefId) {
		String id = metadataRefId.substring(0, metadataRefId.indexOf("TextVar"));

		id += possibleOutputString();
		id += "PVar";
		return id;
	}

	@Override
	protected String getPresentationRecordType() {
		return PRESENTATION_VAR;
	}
}
