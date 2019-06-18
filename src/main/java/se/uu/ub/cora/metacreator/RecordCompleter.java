package se.uu.ub.cora.metacreator;

import se.uu.ub.cora.data.DataGroup;

public abstract class RecordCompleter {
	protected String implementingTextType;
	protected DataGroup spiderDataGroup;

	public RecordCompleter(String implementingTextType) {
		this.implementingTextType = implementingTextType;
	}

	protected void addValuesToDataGroup() {
		addTexts();
	}

	private void addTexts() {
		MetadataCompleter completer = new MetadataCompleter();
		completer.completeSpiderDataGroupWithLinkedTexts(spiderDataGroup, implementingTextType);
	}

	public String getImplementingTextType() {
		return implementingTextType;
	}
}
