package se.uu.ub.cora.metacreator.group;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PChildRefConstructorSpy extends PChildRefConstructor {

	public String metadataRefId;

	public PChildRefConstructorSpy(SpiderDataGroup metadataChildReference, String mode) {
		this.metadataChildReference = metadataChildReference;
		this.mode = mode;
	}

	@Override
	protected String constructIdFromMetdataRefId(String metadataRefId) {
		this.metadataRefId = metadataRefId;
		return "someRefId";
	}

	@Override
	protected String getPresentationRecordType() {
		// TODO Auto-generated method stub
		return null;
	}

}
