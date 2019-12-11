package se.uu.ub.cora.metacreator;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class TextCreator implements ExtendedFunctionality {

	private String implementingTextType;

	public TextCreator(String implementingTextType) {
		this.implementingTextType = implementingTextType;
	}

	public static TextCreator forImplementingTextType(String implementingTextType) {
		return new TextCreator(implementingTextType);
	}

	@Override
	public void useExtendedFunctionality(String authToken, DataGroup spiderDataGroup) {
		RecordCreatorHelper recordCreatorHelper = RecordCreatorHelper
				.withAuthTokenSpiderDataGroupAndImplementingTextType(authToken, spiderDataGroup,
						implementingTextType);
		recordCreatorHelper.createTextsIfMissing();
	}

	public String getImplementingTextType() {
		return implementingTextType;
	}
}
