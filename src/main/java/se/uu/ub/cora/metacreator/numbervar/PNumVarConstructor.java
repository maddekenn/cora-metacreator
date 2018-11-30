package se.uu.ub.cora.metacreator.numbervar;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class PNumVarConstructor {

	private String numberVarId;
	private String dataDividerString;

	public PNumVarConstructor(String numberVarId, String dataDividerString) {
		this.numberVarId = numberVarId;
		this.dataDividerString = dataDividerString;
	}

	public SpiderDataGroup createInputPNumVar() {
		String pVarId = constructPNumVarIdWithEnding("PNumVar");
		return createNumVarUsingIdAndMode(pVarId, "input");
	}

	private String constructPNumVarIdWithEnding(String suffix) {
		String prefix = numberVarId.substring(0, numberVarId.indexOf("NumberVar"));
		return prefix + suffix;
	}

	private SpiderDataGroup createNumVarUsingIdAndMode(String pVarId, String mode) {
		SpiderDataGroup pNumDataGroup = createPNumVarSpiderDataGroup();
		createAndAddRecordInfoToPNumGroupUsingId(pNumDataGroup, pVarId);
		createAndAddPresentationOf(pNumDataGroup);
		pNumDataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("mode", mode));
		return pNumDataGroup;
	}

	private SpiderDataGroup createPNumVarSpiderDataGroup() {
		SpiderDataGroup pNumDataGroup = SpiderDataGroup.withNameInData("presentation");
		pNumDataGroup.addAttributeByIdWithValue("type", "pNumVar");
		return pNumDataGroup;
	}

	private void createAndAddRecordInfoToPNumGroupUsingId(SpiderDataGroup pNumDataGroup,
			String pVarId) {
		SpiderDataGroup recordInfo = DataCreatorHelper.createRecordInfoWithIdAndDataDivider(pVarId,
				dataDividerString);
		pNumDataGroup.addChild(recordInfo);
	}

	private void createAndAddPresentationOf(SpiderDataGroup pNumDataGroup) {
		SpiderDataGroup presentationOf = SpiderDataGroup.withNameInData("presentationOf");
		presentationOf.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType",
				"metadataNumberVariable"));
		presentationOf
				.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", numberVarId));
		pNumDataGroup.addChild(presentationOf);
	}

	public SpiderDataGroup createOutputPVar() {
		String pVarId = constructPNumVarIdWithEnding("OutputPNumVar");
		return createNumVarUsingIdAndMode(pVarId, "output");
	}

}
