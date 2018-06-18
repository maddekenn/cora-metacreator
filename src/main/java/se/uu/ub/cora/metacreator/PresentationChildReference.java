package se.uu.ub.cora.metacreator;

import se.uu.ub.cora.spider.data.SpiderDataGroup;

public final class PresentationChildReference {

	public SpiderDataGroup ref;
	public RecordIdentifier recordIdentifier;

	private PresentationChildReference(SpiderDataGroup ref, RecordIdentifier recordIdentifier) {
		this.ref = ref;
		this.recordIdentifier = recordIdentifier;
	}

	public static PresentationChildReference usingRefGroupAndRecordIdentifier(SpiderDataGroup ref,
			RecordIdentifier recordIdentifier) {
		return new PresentationChildReference(ref, recordIdentifier);
	}

}
