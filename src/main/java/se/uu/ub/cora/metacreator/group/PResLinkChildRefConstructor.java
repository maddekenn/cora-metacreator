package se.uu.ub.cora.metacreator.group;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PResLinkChildRefConstructor extends ChildRefConstructor {
	static final String PRESENTATION_RESOURCE_LINK = "presentationResourceLink";

	private PResLinkChildRefConstructor(SpiderDataGroup metadataChildReference, String mode) {
		this.metadataChildReference = metadataChildReference;
		this.mode = mode;
	}

	public static ChildRefConstructor usingMetadataChildReferenceAndMode(
			SpiderDataGroup metadataChildReference, String mode) {
		return new PResLinkChildRefConstructor(metadataChildReference, mode);
	}

	@Override
	protected String constructIdFromMetdataRefId(String metadataRefId) {
		String id = metadataRefId.substring(0, metadataRefId.indexOf("ResLink"));

		id += possibleOutputString();
		id += "PResLink";
		return id;
	}

	@Override
	protected String getPresentationRecordType() {
		return PRESENTATION_RESOURCE_LINK;
	}
}
