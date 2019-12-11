package se.uu.ub.cora.metacreator.numbervar;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class NumberVarCompleter implements ExtendedFunctionality {

	private String implementingTextType;

	public static NumberVarCompleter forImplementingTextType(String implementingTextType) {
		return new NumberVarCompleter(implementingTextType);
	}

	private NumberVarCompleter(String implementingTextType) {
		this.implementingTextType = implementingTextType;
	}

	@Override
	public void useExtendedFunctionality(String authToken, DataGroup spiderDataGroup) {
		MetadataCompleter completer = new MetadataCompleter();
		completer.completeDataGroupWithLinkedTexts(spiderDataGroup, implementingTextType);
	}

	public String getImplementingTextType() {
		return implementingTextType;
	}

}
