package se.uu.ub.cora.metacreator.search;

import se.uu.ub.cora.metacreator.RecordCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class SearchCreator implements ExtendedFunctionality {
	private String implementingTextType;

	public SearchCreator(String implementingTextType) {
		this.implementingTextType = implementingTextType;
	}

	public static SearchCreator forImplementingTextType(String implementingTextType) {
		return new SearchCreator(implementingTextType);
	}

	@Override
	public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
		RecordCreatorHelper recordCreatorHelper = RecordCreatorHelper
				.withAuthTokenSpiderDataGroupAndImplementingTextType(authToken, spiderDataGroup,
						implementingTextType);
		recordCreatorHelper.createTextsIfMissing();
	}
}
